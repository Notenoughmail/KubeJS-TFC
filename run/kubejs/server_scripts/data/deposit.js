TFCEvents.data(e => {
    e.deposit({
        ingredient: 'minecraft:dirt',
        lootTable: 'minecraft:entity/creeper',
        modelStages: [
            'minecraft:block/cobblestone',
            'minecraft:block/stone',
            'minecraft:items/carrot'
        ]
    }, 'kubejs:deposit');
})
