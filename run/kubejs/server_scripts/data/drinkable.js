TFCEvents.data(e => {
    e.drinkable({
        ingredient: Fluid.lava(100),
        effects: [
            {
                type: 'minecraft:regeneration',
                duration: 50,
                amplifier: 2,
                chance: 1
            }
        ]
    });
})
