# -*- coding: utf-8 -*-
"""Редагування блоків contentN.py за префіксом тексту з переформатуванням."""
import importlib
import sys

sys.path.insert(0, '.')


def _write(modname, blocks):
    from format_content import dump
    dump(modname, blocks)


def replace(modname, prefix, new_text, kind='p'):
    mod = importlib.import_module(modname)
    blocks = [list(b) for b in mod.BLOCKS]
    hits = 0
    for b in blocks:
        if b[0] == kind and b[1].startswith(prefix):
            b[1] = new_text
            hits += 1
    if hits != 1:
        raise SystemExit('%s: %r -> %d збігів' % (modname, prefix[:45], hits))
    _write(modname, blocks)


def apply(modname, pairs):
    mod = importlib.import_module(modname)
    blocks = [list(b) for b in mod.BLOCKS]
    for prefix, new in pairs:
        hits = 0
        for b in blocks:
            if b[0] == 'p' and b[1].startswith(prefix):
                b[1] = new
                hits += 1
        if hits != 1:
            raise SystemExit('%s: %r -> %d збігів' % (modname, prefix[:45], hits))
    _write(modname, blocks)
    print('ok', modname, len(blocks))
