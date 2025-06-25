StartupEvents.registry('block', e => {
    e.create('anvil', 'tfc:anvil')
        .tier(9)
        .defaultName(Text.literal('Neat!'))
        .textureAll('tfc:block/metal/smooth/gold');
})
