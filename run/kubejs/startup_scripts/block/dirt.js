StartupEvents.registry('block', e => {
    e.create('dirt', 'tfc:dirt')
        .texture('tfc:block/charcoal_pile')
        .grass(g => {
            g.texture('tfc:block/wood/planks/oak');
        })
        .path(p => {
            p.texture('tfc:block/metal/smooth/red_steel');
        })
        .farmland(f => {
            f.texture('tfc:block/wood/log/palm');
        })
        .rooted(r => {
            r.texture('tfc:block/sand/pink');
        });
})
