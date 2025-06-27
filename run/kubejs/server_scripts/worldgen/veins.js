TFCEvents.worldgenData(e => {
    e.clusterVein(
        'cluster_vein',
        [
            e.blockToWeightedBlockState(
                [
                    'tfc:rock/raw/gabbro',
                    'tfc:rock/raw/dacite',
                    'tfc:rock/raw/basalt'
                ],
                [
                    '5 minecraft:dirt',
                    'minecraft:gold_block'
                ]
            ),
            e.blockToWeightedBlockState(
                [
                    'tfc:rock/raw/andesite',
                    'tfc:rock/raw/diorite'
                ],
                [
                    '8 minecraft:gold_block',
                    '3 minecraft:gravel'
                ]
            )
        ],
        1,
        1,
        -64,
        100,
        30,
        optional => {
            optional.indicator(
                30,
                1,
                1,
                10,
                [ 'minecraft:diamond_block' ]
            );
        },
        placement => {}
    );

    e.pipeVein(
        'pipe_vein',
        [
        ],
        1,
        1,
        -64,
        100,
        30,
        5,
        2,
        4,
        1,
        7,
        1,
        optional => {
            optional.indicator(
                30,
                1,
                1,
                10,
                [ 'minecraft:iron_block' ]
            );
        },
        placement => {}
    );

    e.discVein(
        'disc_vein',
        [
            e.blockToWeightedBlockState(
                [
                    'tfc:rock/raw/andesite',
                    'tfc:rock/raw/basalt'
                ],
                [
                    '18 minecraft:iron_block',
                    'minecraft:gravel'
                ]
            )
        ],
        1,
        1,
        -64,
        100,
        14,
        3,
        optional => {
            optional.indicator(
                30,
                1,
                1,
                10,
                [ 'minecraft:gravel' ]
            );
        },
        placement => {}
    );

    let raws = [
        'tfc:dirt/loam',
        'tfc:dirt/sandy_loam',
        'tfc:dirt/silt',
        'tfc:dirt/silty_loam',
        'tfc:grass/loam',
        'tfc:grass/sandy_loam',
        'tfc:grass/silt',
        'tfc:grass/silty_loam',
        'tfc:sand/white',
        'tfc:sand/black',
        'tfc:sand/yellow',
        'tfc:sand/pink',
        'tfc:sand/brown',
        'tfc:sand/green',
        'tfc:sand/red',
        'tfc:raw_sandstone/white',
        'tfc:raw_sandstone/black',
        'tfc:raw_sandstone/yellow',
        'tfc:raw_sandstone/pink',
        'tfc:raw_sandstone/brown',
        'tfc:raw_sandstone/green',
        'tfc:raw_sandstone/red'
    ];
    TFC.misc.rock.keySet().forEach(rock => {
        raws.push(
            `tfc:rock/raw/${rock}`.toString(),
            `tfc:rock/gravel/${rock}`.toString(),
            `tfc:rock/hardened/${rock}`.toString()
        );
    });
    e.clusterVein(
        'big_cluster',
        [
            e.blockToWeightedBlockState(
                raws,
                [ 'minecraft:green_stained_glass' ]
            ),
            e.blockToWeightedBlockState(
                [ 'minecraft:air' ],
                [ 'minecraft:red_stained_glass' ]
            )
        ],
        8,
        1,
        60,
        90,
        16,
        optional => {
            optional.biomes(
                'tfc:land'
            );
        },
        placement => {
            placement.heightMap(
                'world_surface_wg'
            );
        }
    );
})
