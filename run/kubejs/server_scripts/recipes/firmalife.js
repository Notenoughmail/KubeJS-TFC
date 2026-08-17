// requires firmalife

ServerEvents.recipes(e => {
    let { firmalife } = e.recipes;

    firmalife.drying(
        'minecraft:dirt',
        'minecraft:stone'
    ).id('kubejs:drying');

    firmalife.smoking(
        'minecraft:stone',
        'minecraft:deepslate'
    ).id('kubejs:smoking');

    firmalife.mixing_bowl([
        'minecraft:deepslate'
    ])
        .resultItem('5x minecraft:short_grass')
        .fluidIngredient(Fluid.of('minecraft:water', 1000))
        .id('kubejs:mixing_bowl');

    firmalife.oven('minecraft:spruce_log', 'minecraft:oak_log', 3, 3)
        .id('kubejs:oven');

    firmalife.stinky_soup(['#minecraft:flowers'], Fluid.of('minecraft:water', 50), 5, 5)
        .id('kubejs:stinky_soup');

    firmalife.vat(
        'minecraft:pink_wool',
        Fluid.of('minecraft:lava', 500),
        60,
        700
    )
        .outputItem('minecraft:dirt')
        .id('kubejs:vat');

    firmalife.stomping(
        'minecraft:dirt',
        'minecraft:stone',
        'tfc:block/charcoal_pile',
        'tfc:block/powder/charcoal',
        'tfc:block.charcoal.fall'
    ).id('kubejs:stomping');

    firmalife.press(
        'minecraft:smooth_stone',
        'minecraft:cobblestone',
        'tfc:block/charcoal_pile',
        'tfc:block/powder/charcoal',
        'tfc:block.charcoal.fall'
    ).id('kubejs:press');

    firmalife.bowl_pot(
        'minecraft:cooked_beef',
        [
            'minecraft:dirt',
            'minecraft:stone'
        ],
        Fluid.water(),
        20,
        100,
        {
            hunger: 50,
            nutrients: [
                0, 0, 5, 0, 0
            ],
            decay_modifier: 0.9
        }
    ).id('kubejs:bowl_pot_0');
})
