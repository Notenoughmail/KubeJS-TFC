ServerEvents.registry('worldgen/configured_feature', e => {
    e.create('boulder', 'tfc:boulder')
        .states({
            'tfc:rock/raw/conglomerate': [
                'minecraft:gold_block',
                'minecraft:gravel'
            ],
            'tfc:rock/raw/dacite': [
                'minecraft:iron_block'
            ]
        })
        .withPlacement(p => p.rarityFilter(12)
                            .inSquare()
                            .heightmap('world_surface_wg')
                            .flatEnough(0.4, 4, 2)
                            .tag('tfc:feature/boulders'));
    e.create('baby_boulder', 'tfc:baby_boulder')
        .states({
            'tfc:rock/raw/dacite': [
                'minecraft:obsidian'
            ]
        })
        .withPlacement(p => p.rarityFilter(5)
                            .inSquare()
                            .heightmap('world_surface_wg')
                            .flatEnough(0.2, 3, 1)
                            .tag('tfc:feature/boulders'));

    e.create('nether_boulder', 'tfc:boulder')
        .states({
            'minecraft:netherrack': [
                'minecraft:iron_block'
            ]
        })
        .withPlacement(p => p.rarityFilter(12)
                            .inSquare()
                            .jsonPlacement({
                                type: 'count_on_every_layer',
                                count: 1
                            })
                            .mcBiome());
})
