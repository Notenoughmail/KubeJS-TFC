StartupEvents.registry('item', e => {
    e.create('chisel', 'tfc:chisel')
        .texture('tfc:item/metal/chisel/copper');
    e.create('mace', 'tfc:mace')
        .texture('tfc:item/metal/mace/copper');
    e.create('propick', 'tfc:propick')
        .texture('tfc:item/metal/propick/copper');
    e.create('hoe', 'tfc:hoe')
        .texture('tfc:item/metal/hoe/copper');
    e.create('javelin', 'tfc:javelin')
        .texture('layer0', 'minecraft:block/dirt');
    e.create('tool', 'tfc:tool')
        .texture('tfc:block/rock/raw/andesite');
    e.create('hammer', 'tfc:hammer')
        .texture('tfc:item/metal/hammer/copper');
    e.create('fishing_rod', 'tfc:fishing_rod')
        .smallBait()
        .largeBait()
        .texture('minecraft:item/diamond_sword')
        .castTexture('minecraft:item/diamond_hoe');
})
