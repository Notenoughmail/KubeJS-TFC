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

    firmalife.mixing_bowl()
        .outputItem('5x minecraft:grass')
        .fluidIngredient(Fluid.of('minecraft:water', 1000))
        .id('kubejs:mixing_bowl');

    firmalife.oven('minecraft:oak_log', 3, 3)
        .id('kubejs:oven_0');
    firmalife.oven('minecraft:spruce_log', 3, 3, 'minecraft:oak_log')
        .id('kubejs:oven_1');

    firmalife.stinky_soup(['#minecraft:flowers'], Fluid.of('minecraft:water', 50), 5, 5)
        .id('kubejs:stinky_soup');

    firmalife.vat()
        .outputItem('minecraft:dirt')
        .inputFluid(Fluid.of('minecraft:lava', 500))
        .length(60)
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

    firmalife.bowl_pot('minecraft:cooked_beef', [
        'minecraft:dirt',
        'minecraft:stone'
    ], 'minecraft:water', 20, 100, food => {
        food.hunger(50);
        food.saturation(2);
        food.protein(3);
    }).id('kubejs:bowl_pot_0');
    firmalife.bowl_pot('tfc:food/red_apple', [
        'minecraft:poppy',
        'minecraft:oak_log'
    ], 'minecraft:lava', 20, 100, {
        hunger: 3,
        fruit: 2,
        decay_modifier: 0.9
    }).id('kubejs:bowl_pot_1');
})
