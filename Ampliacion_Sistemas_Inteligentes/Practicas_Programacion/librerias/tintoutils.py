# === tintoutils.py  — utilidades comunes Tema 2 ==============================
from __future__ import annotations
import re
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from dataclasses import dataclass
from time import perf_counter
from typing import Any, Callable, Dict, List, Optional, Tuple

#################################
######################------------------------------------------------------
# Utilidades para los cuadernos 2.2-2.6
######################----------------------------------
########################################

__all__ = [
    # núcleo
    "run_and_time",
    # multi-run + resumen
    "multi_run", "summarize_runs",
    # gráficas
    "plot_convergence", "plot_best_so_far", "boxplot_fbest", "anytime_curve",
]

# ========= Núcleo: ejecución cronometrada y formato homogéneo =========
def run_and_time(algorithm_fn: Callable[..., Dict[str, Any]],
                 objective: Callable[[np.ndarray], float],
                 init_or_builder: Callable[[np.random.Generator], Any] | Any,
                 rng: np.random.Generator,
                 max_iter: int = 200,
                 **algo_kwargs) -> Tuple[Dict[str, Any], float]:
    """
    Ejecuta un algoritmo con firma libre y devuelve:
      - results: dict con claves {'x','f','x_best','f_best','meta'}
      - elapsed: tiempo en segundos
    Requisito: el algoritmo debe aceptar (objective, init_or_builder, rng, max_iter, **kwargs)
               y devolver un dict con al menos 'f' y 'f_best'.
    """
    t0 = perf_counter()
    results = algorithm_fn(objective, init_or_builder, rng, max_iter=max_iter, **algo_kwargs)
    elapsed = perf_counter() - t0

    # Normaliza faltantes
    results.setdefault("x_best", None)
    results.setdefault("meta", {})
    results["meta"].setdefault("iters", len(results.get("f", [])))
    return results, elapsed

# ======================= Multi-run y resumen ===========================
def multi_run(algorithm_fn: Callable[..., Dict[str, Any]],
              name: str,
              objective: Callable[[np.ndarray], float],
              init_builder: Callable[[np.random.Generator], Any],
              runs: int = 20,
              max_iter: int = 500,
              seed: int = 1234,
              **algo_kwargs) -> pd.DataFrame:
    """
    Lanza 'runs' ejecuciones independientes y devuelve un DataFrame con columnas:
      algo, f_best, iters, elapsed
    """
    rows: List[Dict[str, Any]] = []
    for r in range(runs):
        rng = np.random.default_rng(seed + r)
        init = init_builder(rng)
        res, elapsed = run_and_time(algorithm_fn, objective, init, rng, max_iter=max_iter, **algo_kwargs)
        rows.append({
            "algo": name,
            "f_best": float(res["f_best"]),
            "iters": int(res["meta"].get("iters", len(res["f"]))),
            "elapsed": float(elapsed),
        })
    return pd.DataFrame(rows)

def summarize_runs(df: pd.DataFrame) -> pd.DataFrame:
    """
    Resumen por 'algo' con medias y dispersión. Mantiene nombres históricos.
    """
    return (
        df.groupby("algo")
          .agg(f_best_mean=("f_best", "mean"),
               f_best_std =("f_best", "std"),
               iters_mean =("iters", "mean"),
               elapsed_mean=("elapsed", "mean"),
               runs       =("algo", "size"))
          .sort_values("f_best_mean")
          .reset_index()
    )

# ======================= Gráficas reutilizables ========================
def plot_convergence(results: Dict[str, Dict[str, Any]],
                     title: str = "Convergencia (f vs iter)",
                     ylabel: str = "f(x)") -> None:
    plt.figure(figsize=(7,5))
    for label, res in results.items():
        f = np.asarray(res["f"])
        plt.plot(f, label=label)
    plt.xlabel("Iteración"); plt.ylabel(ylabel); plt.title(title)
    plt.legend(); plt.grid(alpha=0.3); plt.tight_layout(); plt.show()

def plot_best_so_far(results: Dict[str, Dict[str, Any]],
                     title: str = "Convergencia suavizada (mejor acumulado)",
                     ylabel: str = "Mejor f(x)") -> None:
    plt.figure(figsize=(7,5))
    for label, res in results.items():
        f = np.asarray(res["f"])
        fbest = np.minimum.accumulate(f)
        plt.plot(fbest, label=label)
    plt.xlabel("Iteración"); plt.ylabel(ylabel); plt.title(title)
    plt.legend(); plt.grid(alpha=0.3); plt.tight_layout(); plt.show()

def boxplot_fbest(df: pd.DataFrame,
                  title: str = "Distribución f_best",
                  ylabel: str = "f_best",
                  order: Optional[List[str]] = None) -> None:
    plt.figure(figsize=(7,5))
    labels = order if order else sorted(df["algo"].unique())
    data = [df[df["algo"] == lab]["f_best"].values for lab in labels]
    plt.boxplot(data, labels=labels, showfliers=True)
    plt.title(title); plt.xlabel("Algoritmo"); plt.ylabel(ylabel)
    plt.grid(alpha=0.3); plt.tight_layout(); plt.show()

def anytime_curve(curves: Dict[str, Tuple[np.ndarray, np.ndarray]],
                  title: str = "Curva anytime (mejor f vs tiempo)",
                  xlabel: str = "Tiempo [s]",
                  ylabel: str = "Mejor f(x)") -> None:
    """
    curves[name] = (times_array, best_so_far_array)
    """
    plt.figure(figsize=(7,5))
    for name, (ts, bs) in curves.items():
        plt.plot(np.asarray(ts), np.asarray(bs), label=name)
    plt.xlabel(xlabel); plt.ylabel(ylabel); plt.title(title)
    plt.legend(); plt.grid(alpha=0.3); plt.tight_layout(); plt.show()

#################################
######################------------------------------------------------------
# Utilidades para el cuaderno 2.7
######################----------------------------------
########################################

# ---------------------------------------------------------------------------
# 1) Problemas de prueba y límites
# ---------------------------------------------------------------------------
def sphere(x: np.ndarray) -> float:
    x = np.asarray(x, float)
    return float(np.sum(x**2))

def rastrigin(x: np.ndarray) -> float:
    x = np.asarray(x, float)
    return float(10 * x.size + np.sum(x**2 - 10 * np.cos(2*np.pi*x)))

def ackley(x: np.ndarray) -> float:
    x = np.asarray(x, float)
    a, b, c = 20.0, 0.2, 2*np.pi
    d = x.size
    s1 = np.sqrt(np.sum(x**2) / d)
    s2 = np.sum(np.cos(c*x)) / d
    return float(-a*np.exp(-b*s1) - np.exp(s2) + a + np.e)

def make_bounds(D: int, lo: float=-5.0, hi: float=5.0):
    return [(lo, hi)] * D

# Conveniencias por defecto (D=10 en [-5,5])
D_DEFAULT = 10
B10 = make_bounds(D_DEFAULT)

# ---------------------------------------------------------------------------
# 2) Parseo de etiquetas de algoritmos (robusto)
#    Acepta 'a' o 'alpha' y devuelve SIEMPRE 'alpha'
# ---------------------------------------------------------------------------
def parse_algo_label(algo_label: str) -> dict:
    """
    De 'GA(pc=0.80,pm=0.15,a=0.30,N=80)' o '...alpha=0.30...' extrae:
      {'pc': float, 'pm': float, 'alpha': float, 'N': int}
    """
    pairs = dict(re.findall(r'(pc|pm|a|alpha|N)=([\d.]+)', str(algo_label)))
    # normaliza
    if 'a' in pairs and 'alpha' not in pairs:
        pairs['alpha'] = pairs.pop('a')
    return {
        'pc': float(pairs.get('pc', 0.0)),
        'pm': float(pairs.get('pm', 0.0)),
        'alpha': float(pairs.get('alpha', 0.0)),
        'N': int(float(pairs.get('N', 0)))
    }

# ---------------------------------------------------------------------------
# 3) Métricas
# ---------------------------------------------------------------------------
def auc_anytime(f_curve: np.ndarray) -> float:
    """
    Área bajo curva anytime normalizada por longitud.
    Menor es mejor (minimización, mínimo teórico ≈ 0).
    """
    f = np.asarray(f_curve, float)
    if f.size == 0:
        return np.nan
    return float(np.trapz(f, dx=1) / len(f))

def iters_to_eps(f_curve: np.ndarray, eps: float) -> int | None:
    """Primera iteración t con f[t] <= eps; None si no se alcanza."""
    f = np.asarray(f_curve, float)
    idx = np.where(f <= float(eps))[0]
    return int(idx[0]) if idx.size > 0 else None

# ---------------------------------------------------------------------------
# 4) Visualizaciones
# ---------------------------------------------------------------------------
def plot_anytime_mean(curves: dict, problem: str, ylabel="$f_{best}$ (promedio, escala log)"):
    """
    curves: dict con claves (problem, algo) y listas de curvas (misma longitud).
    Dibuja la media por algoritmo (trunca a la mínima longitud).
    """
    plt.figure(figsize=(6.5,4))
    algos = sorted({a for (p,a) in curves.keys() if p == problem})
    for algo in algos:
        Cs = curves[(problem, algo)]
        if not Cs: 
            continue
        L = min(len(c) for c in Cs)
        M = np.mean([np.asarray(c[:L], float) for c in Cs], axis=0)
        plt.plot(M, label=algo)
    plt.yscale('log')
    plt.xlabel("Iteración"); plt.ylabel(ylabel)
    plt.title(f"{problem} — Curva anytime promedio")
    plt.grid(alpha=0.2); plt.legend()
    plt.tight_layout(); plt.show()

def boxplot_fbest_log(df: pd.DataFrame, problem: str):
    """
    Boxplot de f_best por algoritmo (escala log).
    """
    d = df[df['problem'] == problem]
    order = d.groupby('algo')['f_best'].median().sort_values().index
    data = [d[d['algo']==a]['f_best'].values for a in order]
    plt.figure(figsize=(6.5,4))
    plt.boxplot(data, labels=order, showfliers=True)
    plt.yscale('log'); plt.grid(False)
    plt.ylabel("$f_{best}$ (menor es mejor)")
    plt.title(f"{problem} — Distribución de $f_{{best}}$")
    plt.xticks(rotation=10)
    plt.tight_layout(); plt.show()

def bar_leaderboard(summary: pd.DataFrame, problem: str, metric='f_best_median', title=None):
    """
    Ranking horizontal por métrica. Si la métrica es de error (f_best/auc) → escala log.
    """
    d = summary[summary['problem']==problem].sort_values(metric)
    plt.figure(figsize=(6.5,3.6))
    plt.barh(d['algo'], d[metric])
    if any(k in metric for k in ('f_best', 'auc')):
        plt.xscale('log')
    plt.xlabel(metric.replace('_',' '))
    plt.title(title or f"{problem} — Ranking por {metric}")
    plt.grid(axis='x', alpha=0.2)
    plt.tight_layout(); plt.show()

def plot_ecdf_tte_pretty(df: pd.DataFrame, problem: str, max_x: int | None=None):
    """
    ECDF de TTE (tiempo a umbral). Sólo runs exitosos (tte != NaN).
    Etiqueta con nº de éxitos por algoritmo.
    """
    d = df[(df['problem'] == problem) & (~df['tte'].isna())].copy()
    if d.empty:
        print(f"[ECDF] {problem}: no hay runs exitosos (tte=NaN).")
        return

    styles = [
        dict(step_ls='-',  marker='o',  ms=28, alpha=0.9),
        dict(step_ls='--', marker='s',  ms=28, alpha=0.9),
        dict(step_ls='-.', marker='^',  ms=28, alpha=0.9),
        dict(step_ls=':',  marker='D',  ms=28, alpha=0.9),
    ]
    algo_list = sorted(d['algo'].unique())
    shifts = np.linspace(-0.6, 0.6, num=max(2, len(algo_list)))

    plt.figure(figsize=(6.5,4))
    for i, algo in enumerate(algo_list):
        gi = d[d['algo'] == algo]
        xs = np.sort(gi['tte'].astype(int).values)
        if xs.size == 0:
            continue
        ys = np.arange(1, xs.size + 1) / xs.size

        st = styles[i % len(styles)]
        # línea escalonada (tomamos el color para los puntos)
        line = plt.step(xs + shifts[i], ys, where='post',
                        linestyle=st['step_ls'], label=None)
        color = line[0].get_color()
        plt.scatter(xs + shifts[i], ys, s=st['ms'], alpha=st['alpha'],
                    marker=st['marker'], color=color, edgecolor='k', linewidths=0.4, zorder=3)
        # entrada de leyenda con nº de éxitos
        plt.plot([], [], linestyle=st['step_ls'], color=color, label=f"{algo} (n={xs.size})")

    plt.xlabel("Iteraciones hasta $\\varepsilon$")
    plt.ylabel("Proporción de runs $\\leq x$")
    plt.title(f"{problem} — ECDF de TTE")
    if max_x is not None:
        plt.xlim(0, max_x)
    plt.ylim(0, 1.02)
    plt.yticks([0, 0.25, 0.5, 0.75, 1.0])
    plt.grid(alpha=0.2)
    plt.legend(loc="lower right", frameon=True)
    plt.tight_layout(); plt.show()
# ===========================================================================