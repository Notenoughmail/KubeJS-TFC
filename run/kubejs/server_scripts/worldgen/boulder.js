TFCEvents.worldgenData(e => {
    e.boulder(
        'boulder',
        [
            e.boulderState(
                'tfc:rock/raw/dacite',
                [
                    'minecraft:dirt',
                    'minecraft:stone',
                    'minecraft:cobblestone'
                ]
            ),
            e.boulderState(
                'tfc:rock/raw/quartzite',
                [
                    'minecraft:dirt',
                    'minecraft:stone',
                    'minecraft:cobblestone'
                ]
            ),
            e.boulderState(
                'tfc:rock/raw/dolomite',
                [
                    'minecraft:dirt',
                    'minecraft:stone',
                    'minecraft:cobblestone'
                ]
            )
        ],
        placement => {
            placement.rarityFilter(12);
            placement.inSquare();
            placement.heightMap('world_surface_wg');
            placement.flatEnough(flat => {
                flat.flatness(0.4);
            });
        }
    );

    e.boulder(
        'nether_boulder',
        [
            e.boulderState(
                'minecraft:netherrack',
                [ 'minecraft:iron_block' ]
            )
        ],
        placement => {
            placement.rarityFilter(12);
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

    e.babyBoulder(
        'baby_boulder',
        [
            e.boulderState(
                'tfc:rock/raw/dacite',
                [ 'minecraft:gold_block' ]
            ),
            e.boulderState(
                'tfc:rock/raw/quartzite',
                [ 'minecraft:gold_block' ]
            ),
            e.boulderState(
                'tfc:rock/raw/dolomite',
                [ 'minecraft:gold_block' ]
            )
        ],
        placement => {
            placement.rarityFilter(12);
            placement.inSquare();
            placement.heightMap('world_surface_wg');
            placement.flatEnough(flat => {
                flat.flatness(0.4);
            });
        }
    );
})
