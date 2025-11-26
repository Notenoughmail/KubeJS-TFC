StartupEvents.registry('block', e => {
    e.create('log', 'tfc:log')
        .texture('side', 'minecraft:block/warped_planks')
        .texture('end', 'minecraft:block/crimson_planks')
        .stripped(s => {
            s.useFullBlockForItemModel();
            s.texture('side', 'minecraft:block/bricks');
            s.texture('end', 'minecraft:block/mossy_cobblestone');
        });
    e.create('log_no_stripped', 'tfc:log')
        .texture('side', 'minecraft:block/dirt')
        .texture('end', 'minecraft:block/diorite')
        .stripped(null);

    e.create('leaves', 'tfc:leaves')
        .twig('kubejs:ore')
        .autumnIndex(23)
        .texture('minecraft:block/oak_leaves')
        .fallenLeaves(fallen => {
            fallen.texture('minecraft:block/spruce_leaves');
            fallen.noDynamicTinting();
        });
})
