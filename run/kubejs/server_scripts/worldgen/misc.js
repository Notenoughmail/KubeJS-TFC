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
        .withPlacement(p => p.modifiers(m => {
            let { minecraft } = m;
            minecraft
                .inSquare()
                .heightRange({
                    biased: {
                        min: -32,
                        max: 100
                    }
                });
        }));
})