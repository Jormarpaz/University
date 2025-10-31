import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from time import perf_counter

# **Operadores de GA real**  
# En esta celda se definen los operadores clásicos de recombinación y mutación en codificación real:
# - **Cruce aritmético:** mezcla lineal de los padres con un único coeficiente aleatorio α.  
# - **Cruce BLX–α (Blend):** extiende el intervalo entre genes de los padres ±α·(p2–p1) para fomentar diversidad.  
# - **SBX (Simulated Binary Crossover):** simula el comportamiento de cruce binario sobre variables continuas.  
# - **Mutación gaussiana:** añade ruido normal con desviación σ.  
# - **Mutación no uniforme:** ajusta la amplitud de los cambios con el progreso de las iteraciones $Δ(t) = y·(1−r^{(1−t/T)^b})$.  

# ---------- Operadores GA real (cruces y mutaciones) ----------
def clip_bounds(x, bounds):
    """Recorte por límites componente a componente."""
    if bounds is None: return x
    lo = np.array([b[0] for b in bounds], dtype=float)
    hi = np.array([b[1] for b in bounds], dtype=float)
    return np.clip(x, lo, hi)

def cx_arithmetic(p1, p2, rng, bounds=None):
    """Cruce aritmético: hijos convexos de los padres."""
    alpha = float(rng.random())
    c1 = alpha*p1 + (1-alpha)*p2
    c2 = alpha*p2 + (1-alpha)*p1
    return clip_bounds(c1, bounds), clip_bounds(c2, bounds)

def cx_blx_alpha(p1, p2, rng, alpha=0.3, bounds=None):
    """BLX–α: muestrea en [min−αΔ, max+αΔ] por gen."""
    lo = np.minimum(p1, p2); hi = np.maximum(p1, p2); diff = hi - lo
    lo_ext = lo - alpha*diff; hi_ext = hi + alpha*diff
    c1 = rng.uniform(lo_ext, hi_ext); c2 = rng.uniform(lo_ext, hi_ext)
    return clip_bounds(c1, bounds), clip_bounds(c2, bounds)

def cx_sbx(p1, p2, rng, eta=15.0, bounds=None):
    """SBX (Simulated Binary Crossover)."""
    u = rng.random(size=p1.shape)
    beta = np.empty_like(u)
    m = (u <= 0.5)
    beta[m]  = (2*u[m])**(1.0/(eta+1.0))
    beta[~m] = (1.0/(2.0*(1.0-u[~m])))**(1.0/(eta+1.0))
    c1 = 0.5*((1+beta)*p1 + (1-beta)*p2)
    c2 = 0.5*((1-beta)*p1 + (1+beta)*p2)
    return clip_bounds(c1, bounds), clip_bounds(c2, bounds)

def mut_gaussian(x, rng, sigma=0.1, bounds=None):
    """Mutación gaussiana N(0, sigma^2) por gen."""
    y = x + rng.normal(0.0, sigma, size=x.shape)
    return clip_bounds(y, bounds)

def mut_nonuniform(x, rng, t, T, b=2.0, bounds=None):
    """
    Mutación no uniforme: amplitud decreciente con t/T.
    Δ(t,y) = y * (1 - r^{(1 - t/T)^b}), con r~U(0,1).
    """
    if bounds is None: return x
    lo = np.array([b_[0] for b_ in bounds], dtype=float)
    hi = np.array([b_[1] for b_ in bounds], dtype=float)
    y = x.copy()
    for i in range(len(x)):
        if rng.random() < 0.5:
            delta = (x[i]-lo[i])*(1 - rng.random()**((1 - t/float(T))**b))
            y[i] -= delta
        else:
            delta = (hi[i]-x[i])*(1 - rng.random()**((1 - t/float(T))**b))
            y[i] += delta
    return clip_bounds(y, bounds)

# ---------- Selección (minimización) y router ----------
def selection_tournament_indices(f, pop_size, rng, k=2):
    """Torneo de tamaño k: devuelve pop_size índices (con reemplazo de torneos)."""
    idx = np.arange(pop_size)
    winners = []
    for _ in range(pop_size):
        cand = rng.choice(idx, size=min(k, pop_size), replace=False)
        winners.append(cand[np.argmin(f[cand])])
    return np.asarray(winners, dtype=int)

def selection_ranking_indices(f, pop_size, rng, amax=1.9):
    """
    Linear ranking (Baker). amax∈(1,2].
    p_best = a/N, p_worst = (2-a)/N, sum(p)=1. Minimización (orden de f ascendente).
    """
    N = pop_size
    order = np.argsort(f)  # 0 = mejor
    i = np.arange(N)       # posición 0..N-1
    a = float(amax)
    p_pos = (2 - a)/N + 2*(N-1 - i)*(a - 1)/(N*(N-1))
    p = np.zeros(N, dtype=float)
    p[order] = p_pos
    p /= p.sum()
    return rng.choice(np.arange(N), size=N, replace=True, p=p)

# --- Genetic ---

def get_parent_indices(f, pop_size, rng, *, selection='tournament', tournament_k=2, ranking_amax=1.9):
    if selection == 'tournament':
        return selection_tournament_indices(f, pop_size, rng, k=tournament_k)
    if selection == 'ranking':
        return selection_ranking_indices(f, pop_size, rng, amax=ranking_amax)
    raise ValueError(f"selection desconocida: {selection!r}")

def ga_real_minimize(objective, x0_or_pop, rng, max_iter=400, **cfg):
    bounds       = cfg.get('bounds', None)
    pop_size     = int(cfg.get('pop_size', 60))
    pc           = float(cfg.get('pc', 0.9))
    pm           = float(cfg.get('pm', 0.6))
    sigma        = float(cfg.get('sigma', 0.1))
    elitism      = int(cfg.get('elitism', 2))
    selection    = cfg.get('selection', 'tournament')
    tournament_k = int(cfg.get('tournament_k', 2))
    ranking_amax = float(cfg.get('ranking_amax', 1.7))
    cx_type      = cfg.get('crossover', 'blx')     # 'blx'|'arithmetic'|'sbx'
    nonuni       = cfg.get('nonuniform', None)     # {'prob', 'b', 'T'} o None
    mode         = cfg.get('mode', 'generational') # 'generational' | 'steady'
    # para steady: número de reemplazos por generación (pocos para "goteo" continuo)
    steady_replace = int(cfg.get('steady_replace', max(2, pop_size // 5)))

    # Población inicial
    if isinstance(x0_or_pop, np.ndarray) and x0_or_pop.ndim == 2:
        pop = x0_or_pop.copy()
        pop_size = pop.shape[0]
    else:
        x0 = np.asarray(x0_or_pop, float); d = x0.size
        if bounds is None:
            pop = np.vstack([x0] + [x0 + rng.normal(0,1,size=d) for _ in range(pop_size-1)])
        else:
            lo = np.array([b[0] for b in bounds], dtype=float)
            hi = np.array([b[1] for b in bounds], dtype=float)
            pop = rng.uniform(lo, hi, size=(pop_size, len(lo)))

    def _eval(P): return np.array([objective(ind) for ind in P], float)

    # Selector de cruce
    if cx_type == 'arithmetic':
        def cx_fn(a,b): return cx_arithmetic(a,b,rng,bounds=bounds)
    elif cx_type == 'blx':
        def cx_fn(a,b): return cx_blx_alpha(a,b,rng,alpha=cfg.get('alpha',0.2),bounds=bounds)
    elif cx_type == 'sbx':
        def cx_fn(a,b): return cx_sbx(a,b,rng,eta=cfg.get('eta',15.0),bounds=bounds)
    else:
        raise ValueError("crossover desconocido")

    # Eval inicial
    f = _eval(pop)
    best_idx = int(np.argmin(f)); x_best = pop[best_idx].copy(); f_best = float(f[best_idx])
    f_hist, xs_hist = [f_best], [x_best.copy()]

    # Helpers de reproducción (respetan pc/pm)
    def reproduce_pair(p1, p2, t, T):
        if rng.random() < pc:
            c1, c2 = cx_fn(p1, p2)
        else:
            c1, c2 = p1.copy(), p2.copy()
        if pm > 0.0 and rng.random() < pm:
            c1 = mut_gaussian(c1, rng, sigma, bounds=bounds)
        if pm > 0.0 and rng.random() < pm:
            c2 = mut_gaussian(c2, rng, sigma, bounds=bounds)
        if nonuni is not None and rng.random() < nonuni.get('prob', 0.0):
            b = nonuni.get('b', 2.0); Tnon = nonuni.get('T', max_iter)
            c1 = mut_nonuniform(c1, rng, t, Tnon, b, bounds)
            c2 = mut_nonuniform(c2, rng, t, Tnon, b, bounds)
        return c1, c2

    # Bucle principal
    for t in range(1, max_iter+1):
        if mode == 'generational':
            # 1) selección de padres (pop_size)
            parent_idx = get_parent_indices(f, pop_size, rng,
                                            selection=selection, tournament_k=tournament_k, ranking_amax=ranking_amax)
            parents = pop[parent_idx]

            # 2) reproducción a descendencia completa
            offspring = []
            for i in range(0, pop_size, 2):
                p1, p2 = parents[i], parents[(i+1) % pop_size]
                c1, c2 = reproduce_pair(p1, p2, t, max_iter)
                offspring.extend([c1, c2])
            offspring = np.asarray(offspring)[:pop_size]

            # 3) elitismo y reemplazo generacional
            if elitism > 0:
                elites = pop[np.argsort(f)[:elitism]]
                pop = np.vstack([elites, offspring[:pop_size-elitism]])
            else:
                pop = offspring

        elif mode == 'steady':
            # Reemplazo parcial (muestrea parejas y sustituye a los peores)
            # 1) seleccionar algunos padres
            parent_idx = get_parent_indices(f, pop_size, rng,
                                            selection=selection, tournament_k=tournament_k, ranking_amax=ranking_amax)
            parents = pop[parent_idx]
            # 2) generar ~steady_replace descendientes
            children = []
            for i in range(0, 2*steady_replace, 2):
                p1, p2 = parents[i % pop_size], parents[(i+1) % pop_size]
                c1, c2 = reproduce_pair(p1, p2, t, max_iter)
                children.extend([c1, c2])
            children = np.asarray(children)[:steady_replace]

            # 3) insertar hijos sustituyendo a los peores (mantén elite si elitism>0)
            worst_idx = np.argsort(-f)[:steady_replace]  # índices de f más altos (peores)
            if elitism > 0:
                # protege los mejores
                elite_idx = np.argsort(f)[:elitism]
                mask = np.isin(worst_idx, elite_idx, invert=True)
                replace_idx = worst_idx[mask]
                replace_idx = replace_idx[:children.shape[0]]
            else:
                replace_idx = worst_idx[:children.shape[0]]
            pop[replace_idx] = children

        else:
            raise ValueError("mode debe ser 'generational' o 'steady'")

        # evaluar y actualizar mejor
        f = _eval(pop)
        idx = int(np.argmin(f))
        if f[idx] < f_best:
            f_best = float(f[idx]); x_best = pop[idx].copy()
        f_hist.append(f_best); xs_hist.append(x_best.copy())

    return {'f': np.array(f_hist), 'x_best': x_best, 'f_best': f_best, 'xs': np.array(xs_hist)}
