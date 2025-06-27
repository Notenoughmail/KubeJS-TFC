TFCEvents.worldgenData(e => {
    e.simpleBlockState(
        'kubejs:dirt_simple_block',
        'tfc:wild_crop/barely',
        placement => {
            placement.heightMap('world_surface_wg');
            placement.jsonPlacement({
                type: 'minecraft:block_predicate_filter',
                predicate: {
                    type: 'minecraft:would_survive',
                    state: {
                        Name: 'tfc:wild_crop/barley'
                    }
                }
            });
            placement.jsonPlacement({
                type: 'minecraft:block_predicate_filter',
                predicate: {
                    type: 'tfc:replaceable'
                }
            });
        }
    );

    e.randomPatch(
        'kubejs:dirt_patch',
        12,
        4,
        3,
        'kubejs:dirt_simple_block',
        placement => {
            placement.rarityFilter(1);
            placement.flatEnough(flat => {
                flat.flatness(0.5);
                flat.maxDepth(4);
                flat.radius(2);
            });
            placement.inSquare();
            placement.climate(c => {});
        }
    );

    e.generic(
        'kubejs:nether_boulder',
        'tfc:loose_rock',
        {},
        placement => {
            placement.rarityFilter(15);
            placement.inSquare();
            placement.jsonPlacement({
                type: 'count_on_every_layer',
                count: 1
            });
            placement.simplePlacement(
                'minecraft:biome'
            );
        }
    );

    e.thinSpike(
        'thin_spike',
        'kubejs:thin_spike',
        4,
        8,
        2,
        10,
        placement => {
            placement.jsonPlacement({
                type: 'minecraft:count',
                count: 4
            });
            placement.inSquare();
            placement.jsonPlacement({
                type: 'minecraft:height_range',
                height: {
                    type: 'biased_to_bottom',
                    min_inclusive: {
                        absolute: -32
                    },
                    max_inclusive: {
                        absolute: 100
                    }
                }
            });
        }
    );

    e.geode(
        'geode',
        'minecraft:oak_log[axis=z]',
        'tfc:rock/hardened/basalt',
        [
            '6 tfc:rock/raw/quartzite',
            'minecraft:cut_copper'
        ],
        placement => {
            placement.tfcBiome();
            placement.rarityFilter(5);
            placement.inSquare();
            placement.jsonPlacement({
                type: 'minecraft:height_range',
                height: {
                    type: 'uniform',
                    min_inclusive: {
                        absolute: -40
                    },
                    max_inclusive: {
                        absolute: 32
                    }
                }
            });
        }
    );
})
