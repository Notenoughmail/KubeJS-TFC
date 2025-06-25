StartupEvents.registry('block', e => {
    e.create('dirt', 'tfc:dirt')
        .textureAll('tfc:block/charcoal_pile')
            .grass(g => {
                g.textureAll('tfc:block/wood/planks/oak');
            })
            .path(p => {
                p.textureAll('tfc:block/metal/smooth/red_steel');
            })
            .farmland(f => {
                f.textureAll('tfc:block/wood/log/palm');
            })
            .rooted(r => {
                r.textureAll('tfc:block/sand/pink');
            });
})
