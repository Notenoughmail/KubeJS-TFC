
ServerEvents.generateData('last', e => {

    e.json(
        'minecraft:worldgen/structure_set/igloos',
        {
            // TODO: Add placement to WorldJS once that's public
            placement: {
                type: 'tfc:climate',
                salt: 0,
                separation: 5,
                spacing: 6,
                climate: {
                    min_temperature: 50
                }
            },
            structures: [
                {
                    structure: 'minecraft:igloo',
                    weight: 1
                }
            ]
        }
    );
})
