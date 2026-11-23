# -*- coding: utf-8 -*-
"""Генератор курсової роботи: DOCX (для редагування) + PDF (для перевірки).

Формат відтворює зразок: A4, поля 2,5/1,5/2/2 см, Times New Roman 14 pt,
міжрядковий інтервал 1,5, абзацний відступ 1,25 см, вирівнювання за шириною.
"""
import os
import sys
from PIL import Image
from docx import Document
from docx.shared import Pt, Cm, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH, WD_LINE_SPACING, WD_BREAK
from docx.enum.section import WD_SECTION
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml.ns import qn
from docx.oxml import OxmlElement

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, HERE)

import content1, content2, content3, content4

BLOCKS = content1.BLOCKS + content2.BLOCKS + content3.BLOCKS + content4.BLOCKS

# ------------------------------------------------------------------ метрики
CM = 28.3464567          # points per cm
PAGE_W, PAGE_H = 21.0 * CM, 29.7 * CM
ML, MR, MT, MB = 2.5 * CM, 1.5 * CM, 2.0 * CM, 2.0 * CM
TEXT_W = PAGE_W - ML - MR
TEXT_H = PAGE_H - MT - MB
FONT = "Times New Roman"
BODY_PT = 14.0
LEAD = 24.15             # 1,5 інтервалу для 14 pt
FIRST_INDENT = 1.25 * CM
CODE_PT = 10.0
CODE_LEAD = 11.5
TABLE_LEAD = 20.0        # рядок таблиці: 12 pt, одинарний інтервал

FIX = {"\\u00B0": "\u00B0"}


def fix(s):
    for a, b in FIX.items():
        s = s.replace(a, b)
    return s


# ------------------------------------------------------- вимірювання тексту
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont as RLTTFont

_REG = {}


def _font(path, name):
    if name not in _REG:
        pdfmetrics.registerFont(RLTTFont(name, path))
        _REG[name] = name
    return name


REG_PATH = "/tmp/out_fonts/Tinos-400.ttf"
BOLD_PATH = "/tmp/out_fonts/Tinos-700.ttf"
MONO_PATH = "/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf"
_reg_font = _font(REG_PATH, "TNR")
_bold_font = _font(BOLD_PATH, "TNR-B")
_mono_font = _font(MONO_PATH, "Mono")


def width(text, size, bold=False):
    return pdfmetrics.stringWidth(text, _bold_font if bold else _reg_font, size)


def mono_width(text, size):
    return pdfmetrics.stringWidth(text, _mono_font, size)


def wrap_count(text, avail, size, bold=False, font="serif"):
    """Кількість рядків після переносу слів у межах avail."""
    meas = (lambda t: mono_width(t, size)) if font == "mono" else (lambda t: width(t, size, bold))
    total = 0
    for para in text.split("\n"):
        if not para.strip():
            total += 1
            continue
        line = ""
        lines = 1
        for word in para.split(" "):
            cand = word if not line else line + " " + word
            if meas(cand) <= avail or not line:
                line = cand
            else:
                lines += 1
                line = word
        total += lines
    return total


def body_lines(text, indent=FIRST_INDENT):
    avail = TEXT_W - indent
    words_sum = text
    n = 0
    line = ""
    lines = 1
    for word in words_sum.split(" "):
        cand = word if not line else line + " " + word
        if width(cand, BODY_PT) <= avail or not line:
            line = cand
        else:
            lines += 1
            line = word
    return lines


def img_height(path, draw_w_pt):
    with Image.open(path) as im:
        w, h = im.size
    return draw_w_pt * h / w


# ------------------------------------------------------------- обчислення
def block_height(b):
    kind = b[0]
    if kind == "h1":
        return LEAD * 2                      # заголовок + порожній рядок
    if kind == "h2":
        return LEAD * 3                      # порожній + заголовок + порожній
    if kind == "p":
        return body_lines(fix(b[1])) * LEAD
    if kind == "list":
        h = 0
        for item in b[1]:
            avail = TEXT_W - FIRST_INDENT - 0.6 * CM
            lines = 0
            line = ""
            for word in ("\u2022 " + fix(item)).split(" "):
                cand = word if not line else line + " " + word
                if width(cand, BODY_PT) <= avail or not line:
                    line = cand
                else:
                    lines += 1
                    line = word
            lines += 1
            h += lines * LEAD
        return h
    if kind == "code":
        caption, code = b[1], fix(b[2])
        lines = wrap_count(code, TEXT_W, CODE_PT, font="mono")
        return LEAD + lines * CODE_LEAD + LEAD  # підпис + код + відступ
    if kind == "fig":
        cap_h = body_lines(fix(b[2])) * LEAD
        return img_height(os.path.join(HERE, b[1]), b[3] * CM) + 6 + cap_h + LEAD
    if kind == "ref":
        h = 0.0
        for i, item in enumerate(b[1], 1):
            txt = f"{i}.\t{fix(item)}"
            left = 1.25 * CM
            lines = 0
            line = ""
            for word in txt.replace("\t", "    ").split(" "):
                cand = word if not line else line + " " + word
                if width(cand, BODY_PT) <= TEXT_W - left or not line:
                    line = cand
                else:
                    lines += 1
                    line = word
            lines += 1
            h += lines * LEAD
        return h
    if kind == "table":
        cap_h = LEAD
        rows = b[3]
        widths = col_widths(b)
        h = 0
        for row in [b[2]] + rows:
            mx = 1
            for cell, wc in zip(row, widths):
                mx = max(mx, wrap_count(str(cell), wc - 8, 12.0))
            h += mx * TABLE_LEAD + 4
        return cap_h + h + LEAD
    raise ValueError(kind)


def col_widths(b):
    """Ширини колонок; кількість колонок береться із заголовка таблиці."""
    ncol = len(b[2])
    if b[1].startswith("Таблиця 1.2.1"):
        fracs = [0.28, 0.26, 0.24, 0.22]
    elif b[1].startswith("Таблиця 3.11.1"):
        fracs = [0.30, 0.46, 0.24]
    elif ncol == 3:
        fracs = [0.34, 0.44, 0.22]
    else:
        fracs = [1.0 / ncol] * ncol
    return [TEXT_W * f for f in fracs]


# ------------------------------------------------------------------- DOCX
def set_run_font(run, name, size, bold=False):
    run.font.name = name
    run.font.size = Pt(size)
    run.font.bold = bold
    run.font.color.rgb = RGBColor(0, 0, 0)
    rpr = run._element.get_or_add_rPr()
    rf = rpr.find(qn("w:rFonts"))
    if rf is None:
        rf = OxmlElement("w:rFonts")
        rpr.append(rf)
    for attr in ("w:ascii", "w:hAnsi", "w:cs", "w:eastAsia"):
        rf.set(qn(attr), name)


def para(doc, text="", align="just", indent=FIRST_INDENT, size=BODY_PT,
         bold=False, font=FONT, lead=1.5, before=0, after=0, keep=False,
         line_pt=None):
    p = doc.add_paragraph()
    pf = p.paragraph_format
    pf.alignment = {"just": WD_ALIGN_PARAGRAPH.JUSTIFY,
                    "center": WD_ALIGN_PARAGRAPH.CENTER,
                    "left": WD_ALIGN_PARAGRAPH.LEFT,
                    "right": WD_ALIGN_PARAGRAPH.RIGHT}[align]
    pf.first_line_indent = Pt(indent)
    pf.left_indent = Pt(0)
    pf.space_before = Pt(before)
    pf.space_after = Pt(after)
    if line_pt:
        pf.line_spacing = Pt(line_pt)
    else:
        pf.line_spacing = lead
    pf.widow_control = True
    if keep:
        pf.keep_with_next = True
    if text:
        r = p.add_run(fix(text))
        set_run_font(r, font, size, bold)
    return p


def hanging_para(doc, text, left=1.25, hang=0.6, align="just", size=BODY_PT):
    p = doc.add_paragraph()
    pf = p.paragraph_format
    pf.alignment = {"just": WD_ALIGN_PARAGRAPH.JUSTIFY,
                    "left": WD_ALIGN_PARAGRAPH.LEFT}[align]
    pf.left_indent = Cm(left)
    pf.first_line_indent = Cm(-hang)
    pf.space_before = Pt(0)
    pf.space_after = Pt(0)
    pf.line_spacing = 1.5
    r = p.add_run(fix(text))
    set_run_font(r, FONT, size)
    return p


def add_page_number_footer(section):
    footer = section.footer
    p = footer.paragraphs[0]
    p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    p.paragraph_format.first_line_indent = Pt(0)
    p.paragraph_format.space_before = Pt(0)
    p.paragraph_format.space_after = Pt(0)
    p.paragraph_format.line_spacing = 1.0
    run = p.add_run()
    set_run_font(run, FONT, 14)
    fld = OxmlElement("w:fldSimple")
    fld.set(qn("w:instr"), " PAGE ")
    r = OxmlElement("w:r")
    t = OxmlElement("w:t")
    t.text = "1"
    r.append(t)
    fld.append(r)
    p._p.append(fld)


def add_toc_line(doc, left_text, page_text, level=0):
    p = doc.add_paragraph()
    pf = p.paragraph_format
    pf.alignment = WD_ALIGN_PARAGRAPH.LEFT
    pf.left_indent = Cm(0.75 if level else 0)
    pf.first_line_indent = Cm(0)
    pf.space_before = Pt(0)
    pf.space_after = Pt(0)
    pf.line_spacing = 1.5
    # табуляція з заповнювачем
    tabs = p.paragraph_format.tab_stops
    tabs.add_tab_stop(Pt(TEXT_W), 2, 1)  # 2=RIGHT? використаємо XML нижче
    r = p.add_run(fix(left_text) + "\t" + page_text)
    set_run_font(r, FONT, BODY_PT)
    # правильні параметри табуляції: right + dots
    pPr = p._p.get_or_add_pPr()
    for old in pPr.findall(qn("w:tabs")):
        pPr.remove(old)
    tabs_el = OxmlElement("w:tabs")
    tab = OxmlElement("w:tab")
    tab.set(qn("w:val"), "right")
    tab.set(qn("w:leader"), "dot")
    tab.set(qn("w:pos"), str(int(TEXT_W)))
    tabs_el.append(tab)
    pPr.append(tabs_el)
    return p


def add_table(doc, caption, header, rows):
    para(doc, caption, align="left", indent=0, after=6, keep=True)
    widths = col_widths(("table", caption, header, rows))
    t = doc.add_table(rows=1, cols=len(header))
    t.style = "Table Grid"
    t.alignment = WD_TABLE_ALIGNMENT.CENTER
    t.autofit = False
    hdr = t.rows[0].cells
    for i, txt in enumerate(header):
        hdr[i].text = ""
        p = hdr[i].paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p.paragraph_format.first_line_indent = Pt(0)
        p.paragraph_format.space_before = Pt(2)
        p.paragraph_format.space_after = Pt(2)
        p.paragraph_format.line_spacing = 1.0
        r = p.add_run(fix(txt))
        set_run_font(r, FONT, 12, bold=True)
    for row in rows:
        cells = t.add_row().cells
        for i, txt in enumerate(row):
            cells[i].text = ""
            p = cells[i].paragraphs[0]
            p.alignment = WD_ALIGN_PARAGRAPH.LEFT
            p.paragraph_format.first_line_indent = Pt(0)
            p.paragraph_format.space_before = Pt(2)
            p.paragraph_format.space_after = Pt(2)
            p.paragraph_format.line_spacing = 1.0
            r = p.add_run(fix(str(txt)))
            set_run_font(r, FONT, 12)
    for row in t.rows:
        for i, cell in enumerate(row.cells):
            cell.width = Pt(widths[i])
    para(doc, "", indent=0)


def build_docx(path, toc_pages=None):
    doc = Document()
    st = doc.styles["Normal"]
    st.font.name = FONT
    st.font.size = Pt(BODY_PT)
    st.element.rPr.rFonts.set(qn("w:eastAsia"), FONT)
    st.paragraph_format.line_spacing = 1.5
    st.paragraph_format.space_before = Pt(0)
    st.paragraph_format.space_after = Pt(0)

    sec = doc.sections[0]
    sec.page_width = Pt(PAGE_W)
    sec.page_height = Pt(PAGE_H)
    sec.left_margin = Pt(ML)
    sec.right_margin = Pt(MR)
    sec.top_margin = Pt(MT)
    sec.bottom_margin = Pt(MB)
    sec.footer_distance = Cm(1.1)
    add_page_number_footer(sec)

    # ---------------------------------------------------------- ЗМІСТ
    para(doc, "ЗМІСТ", align="center", indent=0, bold=True)
    para(doc, "", indent=0)
    toc = toc_entries()
    for level, text, key in toc:
        add_toc_line(doc, text, str(toc_pages.get(key, 1)) if toc_pages else "1", level)

    # ---------------------------------------------------------- тіло
    first = True
    for b in BLOCKS:
        kind = b[0]
        if kind == "h1":
            p = doc.add_paragraph()
            p.paragraph_format.page_break_before = True if not first else False
            p.paragraph_format.first_line_indent = Pt(0)
            p.paragraph_format.space_before = Pt(0)
            p.paragraph_format.space_after = Pt(0)
            p.paragraph_format.line_spacing = 1.5
            p.alignment = WD_ALIGN_PARAGRAPH.CENTER
            r = p.add_run(fix(b[1]))
            set_run_font(r, FONT, BODY_PT, bold=True)
            para(doc, "", indent=0)
            first = False
        elif kind == "h2":
            para(doc, "", indent=0)
            para(doc, fix(b[1]), align="center", indent=0, bold=True, keep=True)
            para(doc, "", indent=0)
        elif kind == "p":
            para(doc, b[1])
        elif kind == "list":
            for i, item in enumerate(b[1], 1):
                mark = "\u2022" if b[1][0][0].islower() or True else f"{i})"
                hanging_para(doc, f"{mark} {item}", left=1.25, hang=0.6)
        elif kind == "code":
            para(doc, fix(b[1]), align="left", indent=0, keep=True)
            lines = fix(b[2]).split("\n")
            for i, ln in enumerate(lines):
                p = doc.add_paragraph()
                pf = p.paragraph_format
                pf.alignment = WD_ALIGN_PARAGRAPH.LEFT
                pf.first_line_indent = Pt(0)
                pf.left_indent = Pt(0)
                pf.space_before = Pt(0)
                pf.space_after = Pt(0)
                pf.line_spacing = Pt(CODE_LEAD)
                pf.keep_with_next = i < len(lines) - 1
                r = p.add_run(ln if ln else " ")
                set_run_font(r, "Courier New", CODE_PT)
            para(doc, "", indent=0)
        elif kind == "fig":
            p = doc.add_paragraph()
            p.alignment = WD_ALIGN_PARAGRAPH.CENTER
            p.paragraph_format.first_line_indent = Pt(0)
            p.paragraph_format.space_before = Pt(6)
            p.paragraph_format.space_after = Pt(6)
            p.paragraph_format.line_spacing = 1.0
            p.paragraph_format.keep_with_next = True
            r = p.add_run()
            r.add_picture(os.path.join(HERE, b[1]), width=Cm(b[3]))
            para(doc, fix(b[2]), align="center", indent=0)
            para(doc, "", indent=0)
        elif kind == "ref":
            for i, item in enumerate(b[1], 1):
                p = doc.add_paragraph()
                pf = p.paragraph_format
                pf.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
                pf.left_indent = Cm(1.25)
                pf.first_line_indent = Cm(-1.25)
                pf.space_before = Pt(0)
                pf.space_after = Pt(0)
                pf.line_spacing = 1.5
                pPr = p._p.get_or_add_pPr()
                tabs_el = OxmlElement("w:tabs")
                tab = OxmlElement("w:tab")
                tab.set(qn("w:val"), "left")
                tab.set(qn("w:pos"), str(int(1.25 * CM)))
                tabs_el.append(tab)
                pPr.append(tabs_el)
                r = p.add_run(f"{i}.\t" + fix(item))
                set_run_font(r, FONT, BODY_PT)
        elif kind == "table":
            add_table(doc, b[1], b[2], b[3])
    doc.save(path)


# --------------------------------------------------------------- зміст
def toc_entries():
    out = []
    for b in BLOCKS:
        if b[0] == "h1":
            out.append((0, b[1], b[1]))
        elif b[0] == "h2":
            out.append((1, b[1], b[1]))
    return out


def paginate():
    """Обчислює номер сторінки для кожного заголовка."""
    pages = {}
    page = 1
    used = 0.0
    started = False
    for b in BLOCKS:
        h = block_height(b)
        if b[0] == "h1":
            page += 1          # перша сторінка змісту вже зайнята
            used = 0.0
            started = True
            pages[b[1]] = page
            continue
        if b[0] == "h2":
            pages[b[1]] = page
        if h > TEXT_H and b[0] != "h1":
            # блок не вміщується на сторінку — розбиваємо
            remaining = h
            if used + LEAD > TEXT_H:
                page += 1
                used = 0.0
            while remaining > TEXT_H - used:
                remaining -= (TEXT_H - used)
                page += 1
                used = 0.0
            used += remaining
            continue
        if used + h > TEXT_H + 0.5:
            page += 1
            used = 0.0
        used += h
    return pages, page


if __name__ == "__main__":
    # ітеративне уточнення нумерації сторінок для змісту
    pages, total = paginate()
    out_docx = os.path.join(HERE, "Kursova_Weather_Android.docx")
    build_docx(out_docx, pages)
    pages2, total2 = paginate()
    if pages2 != pages:
        build_docx(out_docx, pages2)
    print("сторінок:", total2)
    for lvl, txt, key in toc_entries():
        print(" " * (2 if lvl else 0), txt[:60], "->", pages2.get(key))
    print("файл:", out_docx)
