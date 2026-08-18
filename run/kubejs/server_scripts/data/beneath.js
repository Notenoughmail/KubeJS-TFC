// requires beneath

BeneathEvents.data(e => {
    e.lostPage({
        cost: '#minecraft:flowers',
        costs: [
            1, 2, 3, 4, 5
        ],
        reward: 'minecraft:egg',
        rewards: [
            2, 4, 6, 8, 10
        ],
        punishments: [
            'none',
            'blaze_inferno',
            'corruption'
        ]
    });
    e.netherFertilizer({
        ingredient: '#minecraft:dirt',
        death: 5,
        flame: 1.2
    })
})
