StartupEvents.registry('block', e => {
    e.create('support', 'tfc:support')
        .textureAll('tfc:block/metal/block/wrought_iron')
        .horizontal(h => {
            h.textureAll('tfc:block/metal/block/wrought_iron');
        });
})
