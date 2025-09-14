// requires artisanal

ServerEvents.recipes(e => {
    let { artisanal } = e.recipes;

    artisanal.scalable_pot(['minecraft:dirt', 'minecraft:cobblestone'], Fluid.of('minecraft:lava', 20), 100, 5)
        .fluidOutput(Fluid.of('minecraft:milk', 50))
        .id('kubejs:scalable_pot');

    artisanal.damage_and_catalyst_shapeless_crafting(
        e.shapeless('minecraft:gold_ingot', [
            'tfc:sandpaper',
            '#artisanal:crafting_catalysts',
            '#tfc:saws'
        ])
    ).id('kubejs:damage_and_catalyst_shapeless_crafting');

    artisanal.distillery(450, 20)
        .inputs('minecraft:iron_ingot', TFC.fluidStackIngredient('#minecraft:water', 50))
        .results('minecraft:sculk', Fluid.of('minecraft:lava', 20))
        .leftovers('minecraft:beacon', Fluid.of('minecraft:milk', 15))
        .id('kubejs:distillery');

    // Weird quern interaction
    artisanal.juicing(Fluid.of('minecraft:lava', 50), 'tfc:food/barley_grain')
        .id('kubejs:juicing');

    // Config controlled
    artisanal.only_if_flux_makes_limewater_instant_barrel()
        .outputs('minecraft:diamond', Fluid.of('minecraft:lava', 50))
        .sound('minecraft:ambient.cave')
        .inputFluid(Fluid.of('tfc:limewater', 500))
        .id('kubejs:only_if_flux_makes_limewater_instant_barrel');

    // Removes specified slot, damages all other slots
    artisanal.specific_no_remainder_damage_shaped('minecraft:stick', [
        'S S',
        ' A ',
        'S S'
    ], {
        S: '#tfc:hammers',
        A: 'minecraft:water_bucket'
    }, 1, 1).id('kubejs:specific_no_remainder_damage_shaped');

    // Removes specified slot
    artisanal.specific_no_remainder_shaped('minecraft:iron_sword', [
        'SA',
        'AS'
    ], {
        S: 'minecraft:water_bucket',
        A: 'minecraft:oak_log'
    }, 1, 1).id('kubejs:specific_no_remainder_shaped');

    // Removes primary ingredient
    artisanal.specific_no_remainder_shapeless('minecraft:deepslate', [
        'minecraft:water_bucket',
        'minecraft:milk_bucket',
        'minecraft:stone'
    ], 'minecraft:milk_bucket').id('kubejs:specific_no_remainder_shapeless');
})

ServerEvents.tags('block', e => {
    e.add('minecraft:replaceable', [
        'artisanal:fluid/lard',
        'artisanal:fluid/schmaltz',
        'artisanal:fluid/soap',
        'artisanal:fluid/soapy_water',
        'artisanal:fluid/sugarcane_juice',
        'artisanal:fluid/alkalized_sugarcane_juice',
        'artisanal:fluid/clarified_sugarcane_juice',
        'artisanal:fluid/molasses',
        'artisanal:fluid/condensed_milk',
        'artisanal:fluid/condensed_goat_milk',
        'artisanal:fluid/condensed_yak_milk',
        'artisanal:fluid/apple_juice',
        'artisanal:fluid/carrot_juice',
        'artisanal:fluid/lemon_juice',
        'artisanal:fluid/diluted_lemon_juice',
        'artisanal:fluid/orange_juice',
        'artisanal:fluid/peach_juice',
        'artisanal:fluid/pineapple_juice',
        'artisanal:fluid/tomato_juice',
        'artisanal:fluid/screwdriver', // WTF
        'artisanal:fluid/sour_crude_oil',
        'artisanal:fluid/sweet_crude_oil',
        'artisanal:fluid/kerosene',
        'artisanal:fluid/sulfuric_acid',
        'artisanal:fluid/mercury',
        'artisanal:fluid/filtered_sugarcane_juice'
    ])
})

ServerEvents.tags('fluid', e => {
    e.add('tfc:usable_in_barrel', [
        'artisanal:sulfuric_acid',
        'artisanal:lemon_juice'
    ]);
})