# -*- coding: utf-8 -*-
"""Рендерер PDF, що відтворює формат зразка (A4, Times 14 pt, інтервал 1,5).

Використовується і як готовий PDF, і як точний вимірювач кількості сторінок
для DOCX-версії (метрики шрифту Tinos збігаються з Times New Roman).
"""
import os
import sys

from reportlab.pdfgen import canvas as pdfcanvas
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.lib.colors import black, HexColor, white
from reportlab.lib.utils import ImageReader

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)
import content1, content2, content3, content4

BLOCKS = content1.BLOCKS + content2.BLOCKS + content3.BLOCKS + content4.BLOCKS

CM = 28.3464567
PAGE_W, PAGE_H = 21.0 * CM, 29.7 * CM
ML, MR, MT, MB = 2.5 * CM, 1.5 * CM, 2.0 * CM, 2.0 * CM
TEXT_W = PAGE_W - ML - MR
TOP_Y = PAGE_H - MT               # верхня межа тексту (координати від низу)
TOC_LEAD = 22.0               # крок рядків змісту
BOT_Y = 118.0                # як у зразку: текст закінчується вище колонцифри
FONT, FONT_B = "TNR", "TNR-B"
MONO = "Mono"
BODY = 14.0
LEAD = 24.15
IND = 1.25 * CM
CODE_PT, CODE_LEAD = 10.0, 11.5

pdfmetrics.registerFont(TTFont(FONT, "/tmp/out_fonts/Tinos-400.ttf"))
pdfmetrics.registerFont(TTFont(FONT_B, "/tmp/out_fonts/Tinos-700.ttf"))
pdfmetrics.registerFont(TTFont(MONO, "/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf"))

FIX = {"\\u00B0": "\u00B0"}


def fix(s):
    for a, b in FIX.items():
        s = s.replace(a, b)
    return s


def sw(t, size, bold=False, mono=False):
    return pdfmetrics.stringWidth(t, MONO if mono else (FONT_B if bold else FONT), size)


# --------------------------------------------------------------- переноси
def break_lines(text, avail, size, bold=False, mono=False, first_avail=None):
    """Розбиває абзац на рядки; повертає список рядків."""
    words = text.split(" ")
    lines = []
    line = ""
    limit = first_avail if first_avail is not None else avail
    for w in words:
        cand = w if not line else line + " " + w
        if sw(cand, size, bold, mono) <= limit or not line:
            line = cand
        else:
            lines.append(line)
            line = w
            limit = avail
    lines.append(line)
    # захист від надто довгих «слів» (URL)
    out = []
    for ln in lines:
        while sw(ln, size, bold, mono) > avail and len(ln) > 1:
            cut = len(ln)
            while cut > 1 and sw(ln[:cut], size, bold, mono) > avail:
                cut -= 1
            out.append(ln[:cut])
            ln = ln[cut:]
        out.append(ln)
    return out


class Doc:
    def __init__(self, path, measure_only=False):
        self.c = pdfcanvas.Canvas(path, pagesize=(PAGE_W, PAGE_H))
        self.c.setTitle("Курсова робота. Розробка мобільних застосунків під ОС Android")
        self.page = 1
        self.y = TOP_Y
        self.marks = {}
        self.measure_only = measure_only

    # ---------------------------------------------------------- сервіс
    def new_page(self):
        self.footer()
        self.c.showPage()
        self.page += 1
        self.y = TOP_Y

    def footer(self):
        c = self.c
        c.setFont(FONT, BODY)
        num = str(self.page)
        c.drawString(PAGE_W - MR - sw(num, BODY), 63.4, num)

    def need(self, h):
        if self.y - h < BOT_Y - 1:
            self.new_page()
            return True
        return False

    # ------------------------------------------------------- елементи
    def h1(self, text):
        self.new_page()
        self.marks[text] = self.page
        self.line_center(text, BODY, bold=True)
        self.y -= LEAD

    def h2(self, text):
        # заголовок підрозділу не можна залишати в кінці сторінки:
        # вимагаємо місця на порожній рядок, заголовок і три рядки тексту
        if self.y - (LEAD * 3 + 3 * LEAD) < BOT_Y:
            self.new_page()
        self.y -= LEAD * 0.0
        self.blank()
        self.marks[text] = self.page
        self.line_center(text, BODY, bold=True)
        self.blank()

    def blank(self):
        self.need(LEAD)
        self.y -= LEAD

    def line_center(self, text, size, bold=False):
        self.need(LEAD)
        c = self.c
        c.setFont(FONT_B if bold else FONT, size)
        c.drawCentredString(PAGE_W / 2, self.y - size, fix(text))
        self.y -= LEAD

    def para(self, text, indent=IND, align="just", size=BODY, bold=False,
             lead=LEAD, keep_lines=0):
        text = fix(text)
        lines = break_lines(text, TEXT_W, size, bold, first_avail=TEXT_W - indent)
        # не залишаємо «висячих» рядків: мінімум два рядки абзацу разом
        keep = max(2, keep_lines)
        if self.y - lead * min(keep, len(lines)) < BOT_Y:
            self.new_page()
        c = self.c
        for i, ln in enumerate(lines):
            self.need(lead)
            x = ML + (indent if i == 0 else 0)
            c.setFont(FONT_B if bold else FONT, size)
            if align == "just" and i < len(lines) - 1:
                self._draw_justified(ln, x, self.y - size, TEXT_W - (indent if i == 0 else 0),
                                     size, bold)
            elif align == "center":
                c.drawCentredString(PAGE_W / 2, self.y - size, ln)
            else:
                c.drawString(x, self.y - size, ln)
            self.y -= lead

    def _draw_justified(self, line, x, baseline, avail, size, bold):
        c = self.c
        words = line.split(" ")
        if len(words) == 1:
            c.drawString(x, baseline, line)
            return
        w_total = sum(sw(w, size, bold) for w in words)
        gap = (avail - w_total) / (len(words) - 1)
        if gap < 0 or gap > 12:          # надто великий проміжок — без вирівнювання
            c.drawString(x, baseline, line)
            return
        cx = x
        for w in words:
            c.drawString(cx, baseline, w)
            cx += sw(w, size, bold) + gap

    def bullets(self, items):
        for it in items:
            lines = break_lines("\u2022 " + fix(it), TEXT_W - IND - 0.45 * CM, BODY,
                                first_avail=TEXT_W - IND - 0.45 * CM)
            for i, ln in enumerate(lines):
                self.need(LEAD)
                self.c.setFont(FONT, BODY)
                if i < len(lines) - 1:
                    self._draw_justified(ln, ML + IND + (0 if i == 0 else 0.45 * CM),
                                         self.y - BODY,
                                         TEXT_W - IND - 0.45 * CM, BODY, False)
                else:
                    self.c.drawString(ML + IND + (0 if i == 0 else 0.45 * CM),
                                      self.y - BODY, ln)
                self.y -= LEAD

    def code(self, caption, code):
        # оцінюємо повну висоту лістингу: якщо він уміщується на сторінці,
        # не розриваємо його, а переносимо цілком
        lines = fix(code).split("\n")
        n_lines = 0
        for ln in lines:
            n_lines += len(break_lines(ln, TEXT_W - 0.8 * CM, CODE_PT, mono=True)) if ln else 1
        total = LEAD + n_lines * CODE_LEAD + LEAD
        if n_lines <= 18 and total <= TOP_Y - BOT_Y and self.y - total < BOT_Y:
            self.new_page()
        else:
            self.need(LEAD)
        self.c.setFont(FONT, BODY)
        self.c.drawString(ML, self.y - BODY, fix(caption))
        self.y -= LEAD
        for ln in fix(code).split("\n"):
            parts = break_lines(ln, TEXT_W - 0.8 * CM, CODE_PT, mono=True) if ln else [""]
            for i, part in enumerate(parts):
                self.need(CODE_LEAD)
                self.c.setFont(MONO, CODE_PT)
                self.c.setFillColor(HexColor("#111111"))
                x = ML + (0.8 * CM if i else 0)
                self.c.drawString(x, self.y - CODE_PT, part)
                self.y -= CODE_LEAD
        self.c.setFillColor(black)
        self.blank()

    def fig(self, path, caption, width_cm):
        full = os.path.join(HERE, path)
        ir = ImageReader(full)
        iw, ih = ir.getSize()
        w = width_cm * CM
        h = w * ih / iw
        cap_lines = break_lines(fix(caption), TEXT_W, BODY)
        need = h + 6 + len(cap_lines) * LEAD
        if self.y - need < BOT_Y:
            free = self.y - BOT_Y
            if free > (TOP_Y - BOT_Y) * 0.5:
                scale = (free - len(cap_lines) * LEAD - 12) / h
                w, h = w * scale, h * scale
                need = h + 6 + len(cap_lines) * LEAD
            if self.y - need < BOT_Y:
                self.new_page()
                if self.y - need < BOT_Y:
                    scale = (self.y - BOT_Y - len(cap_lines) * LEAD - 8) / h
                    w, h = w * scale, h * scale
        self.y -= 6
        self.c.drawImage(ir, (PAGE_W - w) / 2, self.y - h, width=w, height=h,
                         mask="auto")
        self.y -= h + 4
        for i, ln in enumerate(cap_lines):
            self.need(LEAD)
            self.c.setFont(FONT, BODY)
            self.c.drawCentredString(PAGE_W / 2, self.y - BODY, ln)
            self.y -= LEAD
        self.blank()

    def table(self, caption, header, rows):
        ncol = len(header)
        if ncol == 4:
            fracs = [0.28, 0.26, 0.24, 0.22]
        elif ncol == 3 and caption.startswith("Таблиця 3.11.1"):
            fracs = [0.30, 0.46, 0.24]
        elif ncol == 3 and caption.startswith("Таблиця 2.4.1"):
            fracs = [0.30, 0.38, 0.32]
        else:
            fracs = [1.0 / ncol] * ncol
        widths = [TEXT_W * f for f in fracs]
        data = [header] + [[str(c) for c in r] for r in rows]
        cell_pt = 12.0
        cell_lead = 14.0
        # таблицю не розриваємо: оцінюємо повну висоту і за потреби починаємо
        # нову сторінку, щоб шапка та всі рядки були разом
        total_h = LEAD
        for row in data:
            lines = max(len(break_lines(fix(str(txt)), w - 8, cell_pt,
                                         bold=(row is data[0])))
                        for txt, w in zip(row, widths))
            total_h += lines * cell_lead + 4
        total_h += LEAD
        if self.y - total_h < BOT_Y:
            self.new_page()
        self.c.setFont(FONT, BODY)
        self.c.drawString(ML, self.y - BODY, fix(caption))
        self.y -= LEAD
        for ri, row in enumerate(data):
            cells = [break_lines(fix(str(txt)), w - 8, cell_pt,
                                 bold=(ri == 0)) for txt, w in zip(row, widths)]
            hrow = max(len(cl) for cl in cells) * cell_lead + 4
            if self.y - hrow < BOT_Y:
                self.new_page()
            top = self.y - 2
            c = self.c
            c.setLineWidth(0.6)
            c.setStrokeColor(HexColor("#333333"))
            c.rect(ML, top - hrow, sum(widths), hrow, stroke=1, fill=0)
            x = ML
            for ci, cl in enumerate(cells):
                if ci:
                    c.line(x, top, x, top - hrow)
                for li, ln in enumerate(cl):
                    c.setFont(FONT_B if ri == 0 else FONT, cell_pt)
                    by = top - 2 - cell_lead * (li + 1) + (cell_lead - cell_pt) / 2 - 1
                    if ri == 0:
                        c.drawCentredString(x + widths[ci] / 2, by, ln)
                    else:
                        c.drawString(x + 4, by, ln)
                x += widths[ci]
            self.y = top - hrow
        self.blank()

    def refs(self, items):
        for i, item in enumerate(items, 1):
            head = f"{i}."
            body = fix(item)
            avail = TEXT_W - IND
            lines = break_lines(body, avail, BODY)
            for j, ln in enumerate(lines):
                self.need(LEAD)
                self.c.setFont(FONT, BODY)
                if j == 0:
                    self.c.drawString(ML, self.y - BODY, head)
                if j < len(lines) - 1:
                    self._draw_justified(ln, ML + IND, self.y - BODY, avail, BODY, False)
                else:
                    self.c.drawString(ML + IND, self.y - BODY, ln)
                self.y -= LEAD

    # -------------------------------------------------------- зміст
    def toc(self, entries, pages):
        self.c.setFont(FONT_B, BODY)
        self.c.drawCentredString(PAGE_W / 2, self.y - BODY, "ЗМІСТ")
        self.y -= LEAD
        for level, text, key in entries:
            num = str(pages.get(key, "1"))
            indent = 0.75 * CM if level else 0
            c = self.c
            c.setFont(FONT, BODY)
            label = fix(text)
            numw = sw(num, BODY)
            maxw = TEXT_W - indent - numw - 10
            rows = break_lines(label, TEXT_W, BODY, first_avail=maxw)
            if len(rows) > 1:
                # друга частина підпису переноситься на наступний рядок
                rest = " ".join(rows[1:])
                first_line = rows[0]
                if sw(rest, BODY) > TEXT_W - indent:
                    rest = rows[1][:len(rows[1])]
                rows = [first_line, rest]
            self.need(TOC_LEAD * len(rows))
            for ri, row in enumerate(rows):
                x = ML + indent
                c.drawString(x, self.y - BODY, row)
                if ri == len(rows) - 1:
                    end = PAGE_W - MR - numw - 4
                    start = x + sw(row, BODY) + 6
                    if end > start:
                        c.setDash(0.5, 3.2)
                        c.setLineWidth(0.8)
                        c.line(start, self.y - BODY + 2.2, end, self.y - BODY + 2.2)
                        c.setDash()
                    c.drawString(PAGE_W - MR - numw, self.y - BODY, num)
                self.y -= TOC_LEAD

    # -------------------------------------------------------- рендер
    def render_body(self, skip_h1=None):
        first = True
        for b in BLOCKS:
            kind = b[0]
            if kind == "h1":
                if skip_h1 and b[1] == skip_h1:
                    continue
                self.h1(b[1])
            elif kind == "h2":
                self.h2(b[1])
            elif kind == "p":
                self.para(b[1])
            elif kind == "list":
                self.bullets(b[1])
            elif kind == "code":
                self.code(b[1], b[2])
            elif kind == "fig":
                self.fig(b[1], b[2], b[3])
            elif kind == "table":
                self.table(b[1], b[2], b[3])
            elif kind == "ref":
                self.refs(b[1])
            first = False


def toc_entries():
    out = []
    for b in BLOCKS:
        if b[0] == "h1":
            out.append((0, b[1], b[1]))
        elif b[0] == "h2":
            out.append((1, b[1], b[1]))
    return out


def build(path, pages_map=None):
    doc = Doc(path)
    if pages_map is not None:
        doc.toc(toc_entries(), pages_map)
    doc.page = 1
    doc.y = TOP_Y
    if pages_map is None:
        # прохід 1: зміст не друкується, тіло починається з першої сторінки
        doc.render_body(skip_h1="ВСТУП")
    else:
        doc.render_body()
    doc.footer()
    doc.c.save()
    return doc


if __name__ == "__main__":
    # прохід 1: дізнатися номери сторінок розділів (без змісту)
    d1 = build("/tmp/_pass1.pdf", None)
    # зміст займає рівно одну сторінку, тому додаємо +1 до нумерації
    pages = dict(d1.marks)
    d2 = build(os.path.join(HERE, "Kursova_Weather_Android.pdf"), pages)
    # перевірка: повторити, якщо нумерація змістилася
    pages2 = dict(d2.marks)
    if pages2 != pages:
        d2 = build(os.path.join(HERE, "Kursova_Weather_Android.pdf"), pages2)
        pages = pages2
    print("сторінок у документі:", d2.page)
    for lvl, txt, key in toc_entries():
        print(" " * (2 if lvl else 0), txt[:58], "->", pages.get(key))
