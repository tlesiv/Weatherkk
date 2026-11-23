# -*- coding: utf-8 -*-
"""Генерація рисунків (схем) для курсової роботи."""
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.patches import FancyBboxPatch, FancyArrowPatch
from matplotlib import font_manager as fm
import os

FONT = "/tmp/out_fonts/Tinos-400.ttf"
FONT_B = "/tmp/out_fonts/Tinos-700.ttf"
MONO = "/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf"
for f in (FONT, FONT_B, MONO):
    if os.path.exists(f):
        fm.fontManager.addfont(f)
SERIF = "Tinos"
MONOF = "DejaVu Sans Mono"

INK = "#1c1c1c"
FILL_A = "#e6e6e6"
FILL_B = "#f3f3f3"
FILL_C = "#cfcfcf"
OUT = "figs"
os.makedirs(OUT, exist_ok=True)


def box(ax, x, y, w, h, text, fc=FILL_B, fs=10, bold=False, ec=INK, lw=1.0, r=0.012):
    p = FancyBboxPatch((x, y), w, h, boxstyle=f"round,pad=0.002,rounding_size={r}",
                       linewidth=lw, edgecolor=ec, facecolor=fc, zorder=2)
    ax.add_patch(p)
    ax.text(x + w / 2, y + h / 2, text, ha="center", va="center", fontsize=fs,
            color=INK, zorder=3, family=SERIF,
            fontweight="bold" if bold else "normal", linespacing=1.35)


def arrow(ax, p1, p2, style="-|>", lw=1.1, ls="-", color=INK, rad=0.0):
    a = FancyArrowPatch(p1, p2, arrowstyle=style, mutation_scale=12, linewidth=lw,
                        linestyle=ls, color=color, zorder=1,
                        connectionstyle=f"arc3,rad={rad}", shrinkA=1, shrinkB=1)
    ax.add_patch(a)


def canvas(w, h):
    fig, ax = plt.subplots(figsize=(w, h), dpi=220)
    ax.set_xlim(0, 1)
    ax.set_ylim(0, 1)
    ax.axis("off")
    fig.subplots_adjust(0, 0, 1, 1)
    return fig, ax


def save(fig, name):
    fig.savefig(os.path.join(OUT, name), dpi=220, facecolor="white",
                bbox_inches="tight", pad_inches=0.05)
    plt.close(fig)
    print("saved", name)


# ----------------------------------------------------------------- 1.3.1
def fig_android_arch():
    fig, ax = canvas(6.6, 4.3)
    layers = [
        ("Застосунки (системні та користувацькі)", 0.83, 0.075, FILL_C, True),
        ("Android Framework API\nActivity Manager · Window Manager · View System\n"
         "Content Providers · Notification Manager · Location Manager", 0.63, 0.145, FILL_A, False),
        ("System libraries (системні бібліотеки)\nSQLite · OpenGL ES · WebKit · Media Framework\nBionic libc · libc++",
         0.45, 0.135, FILL_B, False),
        ("Android Runtime (ART)\nDEX-байткод, компіляція AOT/JIT, збирання сміття",
         0.315, 0.10, FILL_B, False),
        ("Hardware Abstraction Layer (HAL)\nдрайвери камери, аудіо, сенсорів, Bluetooth",
         0.185, 0.095, FILL_A, False),
        ("Ядро Linux\nкерування процесами та пам’яттю · драйвери пристроїв\n"
         "мережевий стек · модель безпеки (SELinux)", 0.03, 0.115, FILL_C, False),
    ]
    for text, y, h, fc, bold in layers:
        box(ax, 0.03, y, 0.94, h, text, fc=fc, fs=9.0, bold=bold)
    # пунктирна рамка навколо бібліотек і runtime
    ax.add_patch(FancyBboxPatch((0.012, 0.303), 0.976, 0.297,
                                boxstyle="round,pad=0.002,rounding_size=0.008",
                                linewidth=1.0, edgecolor="#555555", facecolor="none",
                                linestyle=(0, (4, 3)), zorder=4))
    ax.annotate("native-рівень", xy=(0.955, 0.452), xytext=(1.06, 0.452),
                fontsize=8.6, family=SERIF, style="italic", color="#333333",
                rotation=90, ha="left", va="center",
                arrowprops=dict(arrowstyle="-", lw=0.8, color="#555555",
                                connectionstyle="arc3,rad=0.0"))
    save(fig, "arch_android.png")


# ----------------------------------------------------------------- 1.5.1
def fig_market():
    fig, ax = plt.subplots(figsize=(5.9, 3.5), dpi=220)
    vals = [72.7, 26.9, 0.4]
    labels = ["Android\n72,7 %", "iOS\n26,9 %", "Інші\n0,4 %"]
    colors = ["#8c8c8c", "#3f3f3f", "#d2d2d2"]
    wedges, texts = ax.pie(vals, labels=labels, colors=colors, startangle=90,
                           counterclock=False,
                           wedgeprops=dict(width=0.42, edgecolor="white", linewidth=1.6),
                           textprops=dict(fontsize=10.5, family=SERIF, color=INK))
    ax.text(0, 0, "мобільні ОС\nсвіт, 2025 р.", ha="center", va="center",
            fontsize=9.5, family=SERIF, color="#333333", linespacing=1.4)
    ax.set_aspect("equal")
    save(fig, "market_os.png")


# ----------------------------------------------------------------- 2.4.1
def fig_clean_arch():
    fig, ax = canvas(6.6, 2.3)
    box(ax, 0.02, 0.50, 0.26, 0.40, "Рівень подання (ui)\n\nCompose-екрани,\nViewModel, UiState",
        fc=FILL_B, fs=9.4)
    box(ax, 0.37, 0.42, 0.26, 0.56, "Доменний рівень\n(domain)\n\nмоделі, інтерфейс\nрепозиторію,\nuse case",
        fc=FILL_C, fs=9.4, bold=True)
    box(ax, 0.72, 0.50, 0.26, 0.40, "Рівень даних (data)\n\nRetrofit, DTO,\nмапери, репозиторій",
        fc=FILL_B, fs=9.4)
    arrow(ax, (0.285, 0.70), (0.365, 0.70), lw=1.3)
    arrow(ax, (0.715, 0.70), (0.635, 0.70), lw=1.3)
    ax.text(0.50, 0.09, "усі залежності спрямовані до центру: доменний рівень не знає нічого про Android,\n"
                        "мережу та інтерфейс користувача, тому його можна тестувати окремо",
            ha="center", va="center", fontsize=8.6, family=SERIF, style="italic",
            color="#333333", linespacing=1.5)
    save(fig, "clean_arch.png")


# ----------------------------------------------------------------- 2.4.2
def fig_mvvm():
    fig, ax = canvas(6.6, 2.5)
    box(ax, 0.02, 0.28, 0.20, 0.44, "View\n\nComposable-\nфункції", fc=FILL_A, fs=9.2)
    box(ax, 0.27, 0.28, 0.20, 0.44, "ViewModel\n\nStateFlow<\nUiState>", fc=FILL_C, fs=9.2, bold=True)
    box(ax, 0.52, 0.28, 0.20, 0.44, "Use case\n\nбізнес-\nправила", fc=FILL_A, fs=9.2)
    box(ax, 0.77, 0.28, 0.20, 0.44, "Repository\n\nдані", fc=FILL_A, fs=9.2)
    arrow(ax, (0.225, 0.58), (0.265, 0.58))
    arrow(ax, (0.265, 0.42), (0.225, 0.42))
    ax.text(0.245, 0.68, "подія", ha="center", fontsize=8.2, family=SERIF, style="italic")
    ax.text(0.245, 0.30, "стан", ha="center", fontsize=8.2, family=SERIF, style="italic")
    arrow(ax, (0.475, 0.50), (0.515, 0.50))
    arrow(ax, (0.725, 0.50), (0.765, 0.50))
    ax.text(0.50, 0.13, "односпрямований потік даних: подія → обробка → новий незмінний стан → перемальовування UI",
            ha="center", fontsize=8.4, family=SERIF, style="italic", color="#333333")
    save(fig, "mvvm.png")


# ----------------------------------------------------------------- 3.2.1
def fig_project_tree():
    tree = """com.example.weather
├── WeatherApplication.kt
├── data
│   ├── remote
│   │   ├── dto/ForecastResponse.kt
│   │   ├── WeatherApi.kt
│   │   ├── WeatherRemoteDataSource.kt
│   │   ├── RetrofitWeatherRemoteDataSource.kt
│   │   └── WeatherMapper.kt
│   └── repository/WeatherRepositoryImpl.kt
├── di
│   └── AppModule.kt
├── domain
│   ├── model/Weather.kt
│   ├── repository/WeatherRepository.kt
│   └── usecase/GetForecastUseCase.kt
└── ui
    ├── components
    │   ├── DialogSearch.kt
    │   ├── TodayWeather.kt
    │   ├── WeatherCards.kt
    │   ├── WeatherIcons.kt
    │   └── WeekWeather.kt
    ├── main
    │   ├── MainActivity.kt
    │   ├── MainViewModel.kt
    │   ├── WeatherApp.kt
    │   └── WeatherUiState.kt
    └── theme
        ├── Color.kt, Fonts.kt
        ├── Theme.kt, Type.kt"""
    fig, ax = plt.subplots(figsize=(6.2, 5.4), dpi=220)
    ax.axis("off")
    fig.subplots_adjust(0, 0, 1, 1)
    ax.text(0.01, 0.99, tree, ha="left", va="top", family=MONOF, fontsize=9.2,
            color="#141414", linespacing=1.55)
    save(fig, "project_tree.png")


# ----------------------------------------------------------------- 3.5.1
def fig_hilt():
    fig, ax = canvas(6.6, 2.1)
    items = [
        "Main\nViewModel\n@HiltViewModel",
        "GetForecast\nUseCase",
        "Weather\nRepository\n«інтерфейс»",
        "Weather\nRepository\nImpl",
        "WeatherRemote\nDataSource",
        "WeatherApi\nRetrofit",
    ]
    w, gap = 0.135, 0.032
    x = 0.02
    for i, t in enumerate(items):
        fc = FILL_C if i in (0, 3) else FILL_B
        box(ax, x, 0.36, w, 0.44, t, fc=fc, fs=8.4, bold=(i in (0, 3)))
        if i < len(items) - 1:
            arrow(ax, (x + w + 0.004, 0.58), (x + w + gap - 0.004, 0.58), lw=1.1)
        x += w + gap
    ax.text(0.50, 0.17, "стрілка означає «залежить від»: об’єкти створює та передає контейнер Hilt\n"
                        "згідно з правилами, оголошеними в модулі AppModule",
            ha="center", va="center", fontsize=8.5, family=SERIF, style="italic",
            color="#333333", linespacing=1.5)
    save(fig, "hilt_graph.png")


if __name__ == "__main__":
    fig_android_arch()
    fig_market()
    fig_clean_arch()
    fig_mvvm()
    fig_project_tree()
    fig_hilt()
