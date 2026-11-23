# -*- coding: utf-8 -*-
"""Переформатовує contentN.py у читабельний вигляд після редагувань.

Кожен блок записується як кортеж із першим елементом-тегом; довгі рядки
розбиваються на суміжні літерали з відступом, що вирівнює їх під текстом.
"""
import importlib
import sys

sys.path.insert(0, '.')

WIDTH = 92
CONT = 6          # відступ продовження рядка під текстом


def split_literal(s, indent):
    """Повертає список шматків рядка s, що вкладаються у WIDTH."""
    words = s.split(' ')
    lines, cur = [], ''
    for w in words:
        cand = (cur + ' ' + w) if cur else w
        if len(cand) + indent + 2 > WIDTH and cur:
            lines.append(cur + ' ')
            cur = w
        else:
            cur = cand
    lines.append(cur)
    return lines


def lit(s, indent, cont=None):
    cont = indent if cont is None else cont
    if len(s) + indent + 2 <= WIDTH and '\n' not in s:
        return repr(s)
    chunks = split_literal(s, cont)
    head = repr(chunks[0])
    pad = ' ' * cont
    rest = [pad + repr(c) for c in chunks[1:]]
    return ('\n').join([head] + rest)


def dump(modname, blocks=None):
    mod = importlib.import_module(modname)
    if blocks is not None:
        mod = type('M', (), {'__doc__': mod.__doc__, 'BLOCKS': blocks})
    out = ['# -*- coding: utf-8 -*-',
           '"""%s"""' % (mod.__doc__ or '').strip(),
           '',
           'BLOCKS = [']
    for b in mod.BLOCKS:
        tag = b[0]
        if tag in ('h1', 'h2', 'p'):
            body = lit(b[1], CONT)
            out.append('("%s", %s),' % (tag, body))
        elif tag == 'list':
            out.append('("list", [')
            for item in b[1]:
                out.append('    %s,' % lit(item, 4))
            out.append(']),')
        elif tag == 'code':
            cap = lit(b[1], CONT)
            code = lit(b[2], 1, cont=1)
            out.append('("code", %s,' % cap)
            out.append('%s),' % code)
        elif tag == 'fig':
            out.append('("fig", %s, %s, %s),' % (lit(b[1], CONT), lit(b[2], CONT), b[3]))
        elif tag == 'table':
            out.append('("table", %s,' % lit(b[1], CONT))
            out.append(' [%s],' % ', '.join(repr(h) for h in b[2]))
            rows = ['    [' + ', '.join(repr(c) for c in r) + '],' for r in b[3]]
            out.append(' [\n' + '\n'.join(rows) + '\n ]),')
        elif tag == 'ref':
            out.append('("ref", [')
            for item in b[1]:
                out.append('    %s,' % lit(item, 4))
            out.append(']),')
        out.append('')
    out.append(']')
    open(modname + '.py', 'w', encoding='utf-8').write('\n'.join(out) + '\n')
    print('written', modname, len(mod.BLOCKS), 'blocks')


if __name__ == '__main__':
    for name in (sys.argv[1:] or ['content1', 'content2', 'content3', 'content4']):
        dump(name)
