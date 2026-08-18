// requires firmalife

FirmaLifeEvents.data(e => {
    e.greenhouseType({
        ingredient: [
            'minecraft:green_wool',
            'minecraft:glass'
        ],
        tier: 25,
        translationKey: Text.darkPurple('Neat!')
    })
    e.plantable({
        ingredient: 'minecraft:rose_bush',
        planter: 'trellis',
        tier: 22,
        seed: 'minecraft:rose_bush',
        crop: 'minecraft:rose_bush',
        nutrient: {
            nitrogen: 2
        },
        textures: [
            'minecraft:block/stone',
            'minecraft:block/dirt'
        ]
    })
})
