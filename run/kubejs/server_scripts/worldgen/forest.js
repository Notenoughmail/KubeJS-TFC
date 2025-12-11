ServerEvents.registry('worldgen/configured_feature', e => {
    e.create('tfc:forest', 'tfc:forest')
        .entries('#kubejs:example_forest_entries')
        .withPlacement(p => {});

    e.create('willow_replica', 'tfc:forest_entry')
        .tag('kubejs:example_forest_entries')
        .trees('kubejs:tree/willow_replica', 'tfc:tree/pine_large')
        .bush('tfc:wood/log/oak', 'tfc:wood/leaves/oak')
        .soilDisc('kubejs:soil_disc')
    e.create('stacked_tree', 'tfc:forest_entry')
        .tag('kubejs:example_forest_entries')
        .trees('kubejs:tree/stacked_tree', 'tfc:tree/pine_large')
        .groundcover([ 'minecraft:crimson_fungus' ]);

    e.create('example_tree', 'tfc:overlay_tree')
        .structures('tfc:white_cedar/base', 'tfc:white_cedar/overlay')
        .overlayIntegrity(0.4)
        .treePlacement({
            width: 5,
            height: 1,
            groundType: 'shallow_water'
        })
        .withPlacement(p => {});

    e.create('tree/willow_replica', 'tfc:random_tree')
        .trees([
            'tfc:willow/1',
            'tfc:willow/2',
            'tfc:willow/3'
        ])
        .treePlacement({
            width: 3,
            height: 1,
            groundType: 'shallow_water'
        })
        .roots({
            blocks: {
                'tfc:grass/oxisoil': [
                    'tfc:rooted_dirt/oxisoil'
                ]
            },
            width: 5,
            height: 2,
            tries: 15
        })
        .withPlacement(p => {});

    e.create('tree/stacked_tree', 'tfc:stacked_tree')
        .layers([
            {
                templates: [
                    'tfc:oak/1',
                    'tfc:oak/2',
                    'tfc:oak/3'
                ],
                minCount: 1,
                maxCount: 2
            },
            {
                templates: [
                    'tfc:ash/1',
                    'tfc:ash/2',
                    'tfc:ash/3'
                ],
                minCount: 2,
                maxCount: 4
            }
        ])
        .treePlacement({
            width: 2,
            height: 2,
            groundType: 'normal'
        })
        .trunk({
            state: 'tfc:wood/log/oak[axis=y]',
            minHeight: 1,
            maxHeight: 5,
            wide: false
        })
        .withPlacement(p => {});

    e.create('example_krummholz', 'tfc:krummholz')
        .krummholz('tfc:plant/spruce_krummholz')
        .height([ 1, 4 ])
        .spawnsOnStone()
        .withPlacement(p => {})
})
