ServerEvents.registry('worldgen/configured_feature', e => {

    e.create('soil_disc', 'tfc:soil_disc')
        .replacementStates({
            'tfc:dirt/oxisol': 'minecraft:netherrack'
        })
        .radius(2, 5)
        .height(2)
        .integrity(0.4);

    e.create('thin_spike', 'tfc:thin_spike')
        .state('kubejs:thin_spike')
        .radius(4)
        .tries(12)
        .height(2, 12)
        .withPlacement(p => p.count(4)
                            .inSquare()
                            .jsonPlacement({
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
                            }));

    e.create('dirt_single_block', 'kubejs_tfc:simple_block')
        .state('tfc:wild_crop/barley')
        .withPlacement(p => p.heightmap('world_surface_wg')
                            .jsonPlacement({
                                type: 'minecraft:block_predicate_filter',
                                predicate: {
                                    type: 'minecraft:would_survive',
                                    state: {
                                        Name: 'tfc:wild_crop/barley'
                                    }
                                }
                            })
                            .jsonPlacement({
                                type: 'minecraft:block_predicate_filter',
                                predicate: {
                                    type: 'tfc:replaceable'
                                }
                            }));

    e.create('dirt_patch', 'kubejs_tfc:random_patch')
        .tries(12)
        .spread(4, 3)
        .feature('kubejs:dirt_single_block')
        .withPlacement(p => p.rarityFilter(1)
                            .flatEnough(0.5, 4, 2)
                            .inSquare()
                            .climate(c => {}));

    e.create('nether_boulder', 'kubejs_tfc:generic')
        .of('tfc:loose_rock', {})
        .withPlacement(p => p.rarityFilter(15)
                            .inSquare()
                            .mcBiome()
                            .jsonPlacement({
                                type: 'count_on_every_layer',
                                count: 1
                            }));
})