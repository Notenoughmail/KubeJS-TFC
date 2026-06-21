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
        .withPlacement(p => p.tag('tfc:feature/boulders').modifiers(m => {
            let { minecraft, tfc } = m;
            minecraft
                .rarityFilter(12)
                .inSquare()
                .heightmap('world_surface_wg');
            tfc.flatEnough(0.4, 4, 2);
        }));
    e.create('baby_boulder', 'tfc:baby_boulder')
        .states({
            'tfc:rock/raw/dacite': [
                'minecraft:obsidian'
            ]
        })
        .withPlacement(p => p.tag('tfc:feature/boulders').modifiers(m => {
            let { minecraft, tfc } = m;
            minecraft
                .inSquare()
                .heightmap('world_surface_wg');
            tfc.flatEnough(0.2, 3, 1);
        }));

    e.create('nether_boulder', 'tfc:boulder')
        .states({
            'minecraft:netherrack': [
                'minecraft:iron_block'
            ]
        })
        .withPlacement(p => p.modifiers(m => {
            let { minecraft } = m;
            minecraft
                .inSquare()
                .countOnEveryLayer(1)
                .biome();
        }));
})
