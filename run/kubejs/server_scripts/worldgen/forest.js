TFCEvents.worldgenData(event => {
    event.krummholz(
        'example_krummholz',
        'tfc:plant/spruce_krummholz',
        [ 1, 4 ],
        true,
        false,
        placement => {}
    );
    event.overlayTree(
        'example_tree',
        'tfc:white_cedar/base',
        'tfc:white_cedar/overlay',
        event.trunk('minecraft:dirt', 0, 3, false),
        0.3,
        event.treePlacement(1, 5, 'shallow_water'),
        null,
        placement => {}
    );
    event.forest(
        'example_forest',
        'kubejs:example_forest_entries',
        [
            event.forestTypesMapEntry('edge', { min: 0, max: 2}, null, 0.1, null, false, false, null),
            event.forestTypesMapEntry('normal', { min: 1, max: 9 }, { min: 2, max: 3 }, 0.5, null, false, false, null)
        ],
        null,
        placement => {}
    );
    event.forestEntry(
        'example_forest_entry',
        climate => {
            climate.minRain(325)
        },
        'tfc:wood/log/oak',
        'tfc:wood/leaves/oak',
        null,
        null,
        null,
        'tfc:tree/oak',
        'kubejs_tfc:example_tree',
        'tfc:tree/pine_large',
        null,
        80,
        53,
        null,
        62,
        true,
        placement => {}
    );
    event.randomTree(
        'willow_replica',
        [
            'tfc:willow/1',
            'tfc:willow/2',
            'tfc:willow/3'
        ],
        null,
        event.treePlacement(1, 3, 'shallow_water'),
        event.root([
            event.blockToWeightedBlockState([ 'tfc:grass/silt' ], [ 'tfc:rooted_dirt/silt' ]),
            event.blockToWeightedBlockState([ 'tfc:dirt/silt' ], [ 'tfc:rooted_dirt/silt' ])
        ], 4, 2, 15, null, null),
        placement => {}
    );
    event.stackedTree(
        'example_stacked_tree',
        [
            event.treeLayer([ 'tfc:oak/1', 'tfc:oak/2', 'tfc:oak/3' ], 1, 3),
            event.treeLayer([ 'tfc:ash/1', 'tfc:ash/2', 'tfc:ash/3', 'tfc:ash/4', 'tfc:ash/5' ], 2, 4)
        ],
        event.trunk('minecraft:oak_log', 1, 5, false),
        event.treePlacement(5, 2, null),
        null,
        placement => {}
    );
})