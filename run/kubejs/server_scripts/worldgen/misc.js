TFCEvents.worldgenData(e => {
    e.simpleBlockState(
        'kubejs:dirt_simple_block',
        'tfc:wild_crop/barely',
        p => {
            p.heightMap('world_surface_wg');
            p.jsonPlacement({
                type: 'minecraft:block_predicate_filter',
                predicate: {
                    type: 'minecraft:would_survive',
                    state: {
                        Name: 'tfc:wild_crop/barley'
                    }
                }
            });
            p.jsonPlacement({
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
        p => {
            p.rarityFilter(1);
            p.flatEnough(f => {
                f.flatness(0.5);
                f.maxDepth(4);
                f.radius(2);
            });
            p.inSquare();
            p.climate(c => {});
        }
    );

    e.generic(
        'kubejs:nether_boulder',
        'tfc:loose_rock',
        {},
        p => {
            p.rarityFilter(15);
            p.inSquare();
            p.jsonPlacement({
                type: 'count_on_every_layer',
                count: 1
            });
            p.simplePlacement('minecraft:biome');
        }
    );
})
