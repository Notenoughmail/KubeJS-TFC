ServerEvents.registry('worldgen/configured_feature', e => {

    let all = Utils.newMap();
    TFC.data.rocks.forEach((n, rock) => {
        all.put(rock.getBlock('raw').get(), [
            'minecraft:gold_ore',
            {
                value: 'minecraft:oak_fence',
                weight: 5
            }
        ]);
    });

    e.create('cluster_vein', 'tfc:cluster_vein')
        .replacementStates(all)
        .rarity(12)
        .density(1)
        .maxY(70)
        .seed(7852127852)
        .indicator({
            depth: 20,
            rarity: 6,
            states: [
                {
                    value: 'minecraft:torch',
                    weight: 5
                },
                'minecraft:coal_block'
            ]
        })
        .size(30)
        .withPlacement(p => p.tag('tfc:in_biome/veins'));

    e.create('pipe_vein', 'tfc:pipe_vein')
        .replacementStates(all)
        .rarity(12)
        .density(0.2)
        .minY(-30)
        .maxY(40)
        .seed(7862549652)
        .sign(0.5)
        .height(20)
        .radius(5)
        .skew(1, 3)
        .slant(1, 3)
        .withPlacement(p => p.tag('tfc:in_biome/veins'));

    e.create('disc_vein', 'tfc:disc_vein')
        .replacementStates(all)
        .rarity(12)
        .density(0.8)
        .minY(30)
        .maxY(80)
        .seed(98745321)
        .size(10)
        .height(5)
        .withPlacement(p => p.tag('tfc:in_biome/veins'));

    e.create('geode', 'tfc:geode')
        .outer('minecraft:oak_log[axis=z]')
        .middle('tfc:rock/hardened/basalt')
        .inner([
            'minecraft:cut_copper',
            {
                value: 'tfc:rock/hardened/quartzite',
                weight: 6
            }
        ])
        .withPlacement(p => p.tfcBiome()
                            .rarityFilter(3)
                            .inSquare()
                            .jsonPlacement({
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
                            })
                            .tag('tfc:in_biome/veins'));
})
