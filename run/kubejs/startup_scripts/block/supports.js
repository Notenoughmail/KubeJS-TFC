StartupEvents.registry('block', e => {
    e.create('support', 'tfc:support')
        .texture('tfc:block/metal/block/wrought_iron')
        .horizontal(h => {
            h.texture('tfc:block/metal/block/wrought_iron');
        });
})
