import numpy as np

def es_minimize(objective, x0_or_pop, rng, max_iter=300, **cfg):
    """
    Estrategia de Evolución (μ, λ) simple con mutación gaussiana.
    """
    bounds = cfg.get('bounds', None)
    if bounds is None:
        raise ValueError("ES: faltan 'bounds' en la configuración (lista de (lo, hi)).")
    mu = int(cfg.get('mu', 30))
    lam = int(cfg.get('lam', 200))
    sigma = float(cfg.get('sigma', 0.2))
    plus = cfg.get('elitism', 0) and cfg.get('mode', 'plus') in ('plus', 'μ+λ')

    # Inicialización
    if isinstance(x0_or_pop, np.ndarray) and x0_or_pop.ndim == 2:
        pop = x0_or_pop.copy()
        if pop.shape[0] < mu:
            d = pop.shape[1]
            lo = np.array([b[0] for b in bounds])
            hi = np.array([b[1] for b in bounds])
            extra = rng.uniform(lo, hi, size=(mu - pop.shape[0], d))
            parents = np.vstack([pop, extra])
        else:
            parents = pop[:mu].copy()
    else:
        x0 = np.asarray(x0_or_pop, float)
        d = x0.size
        lo = np.array([b[0] for b in bounds])
        hi = np.array([b[1] for b in bounds])
        parents = rng.uniform(lo, hi, size=(mu, d))

    def clip_bounds(X):
        if bounds is None:
            return X
        lo = np.array([b[0] for b in bounds])
        hi = np.array([b[1] for b in bounds])
        return np.clip(X, lo, hi)

    def eval_pop(P):
        return np.array([objective(ind) for ind in P], dtype=float)

    f_par = eval_pop(parents)
    best_idx = int(np.argmin(f_par))
    x_best = parents[best_idx].copy()
    f_best = float(f_par[best_idx])
    f_hist, xs_hist = [f_best], [x_best.copy()]

    for _ in range(max_iter):
        idx = rng.integers(0, mu, size=lam)
        off = parents[idx] + rng.normal(0, sigma, size=parents[idx].shape)
        off = clip_bounds(off)
        f_off = eval_pop(off)

        if plus:
            allX = np.vstack([parents, off])
            allF = np.concatenate([f_par, f_off])
            sel = np.argsort(allF)[:mu]
            parents, f_par = allX[sel], allF[sel]
        else:
            sel = np.argsort(f_off)[:mu]
            parents, f_par = off[sel], f_off[sel]

        if f_par[0] < f_best:
            f_best = float(f_par[0])
            x_best = parents[0].copy()
        f_hist.append(f_best)
        xs_hist.append(x_best.copy())

    return {'f': np.array(f_hist), 'x_best': x_best, 'f_best': f_best, 'xs': np.array(xs_hist)}