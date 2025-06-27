function tfc(tfc, mc) {
    tfc.alloy('tfc:zinc', [
        TFC.alloyPart('tfc:copper', 0.2, 0.3),
        TFC.alloyPart('tfc:cast_iron', 0.5, 0.7),
        TFC.alloyPart('kubejs:metal', 0.1, 0.3)
    ]).id('kubejs:alloy');

    tfc.welding(
        'minecraft:brick',
        'tfc:rock/cobble/dacite',
        'minecraft:clay'
    ).id('kubejs:welding');

    tfc.anvil(
        'minecraft:iron_block',
        'tfc:metal/double_ingot.wrought_iron',
        [
            'hit_not_last',
            'upset_any'
        ]
    ).bonus(true).id('kubejs:anvil');

    tfc.barrel_instant_fluid(
        Fluid.of('minecraft:water', 50),
        '#minecraft:lava',
        '#minecraft:lava'
    ).id('kubejs:instant_fluid_barrel');

    tfc.barrel_instant()
        .outputItem('minecraft:dirt')
        .inputs('3x minecraft:oak_log')
        .id('kubejs:instant_barrel');

    tfc.barrel_sealed(5000)
        .outputFluid(Fluid.of('minecraft:lava', 80))
        .inputItem('5x minecraft:cooked_porkchop')
        .onSeal(TFC.isp.of('minecraft:grass'))
        .id('kuebjs:sealed_barrel');

    tfc.blast_furnace(
        Fluid.of('tfc:metal/zinc', 5),
        'tfc:powder/flux',
        TFC.fluidStackIngredient(['tfc:metal/copper', 'tfc:metal/nickel'], 90)
    ).id('kubejs:blast_furnace');

    tfc.bloomery(
        '3x minecraft:dirt',
        '8x #minecraft:flowers',
        TFC.fluidStackIngredient('#tfc:alcohols', 50),
        5
    ).id('kubejs:bloomery');

    tfc.casting(
        '8x minecraft:cobblestone',
        'tfc:ceramic/ingot_mold',
        Fluid.of('minecraft:lava', 1000),
        1
    ).id('kubejs:casting');

    tfc.chisel(
        'minecraft:grass_block[snowy=true]',
        '#minecraft:flowers',
        'smooth'
    ).extraDrop('4x minecraft:blue_dye')
        .id('kubejs:chisel');

    tfc.landslide(
        'minecraft:dirt',
        TFC.blockIngredient('minecraft:stone')
    ).id('kubejs:landslide');

    tfc.collapse('minecraft:coarse_dirt')
        .id('kubejs:collapse');

    tfc.glassworking(
        '3x minecraft:red_stained_glass_pane',
        'minecraft:red_stained_glass',
        [
            'blow',
            'stretch',
            'stretch'
        ]
    ).id('kubejs:glassworking_0');
    tfc.glassworking(
        'minecraft:stone',
        'minecraft:cobblestone',
        [
            'kubejs_quench',
            'kubejs_powder'
        ]
    ).id('kubejs:glassworking_1');

    tfc.heating('minecraft:cobblestone', 1500)
        .results('minecraft:stone', Fluid.of('tfc:rum', 1000))
        .id('kubejs:heating');

    tfc.knapping(
        'minecraft:clay',
        'kubejs:knap',
        [
            'XXX',
            'X X',
            'XXX'
        ]
    ).outsideSlotRequired(false)
        .id('kubejs:knapping');

    tfc.loom(
        '4x minecraft:red_wool',
        '4x minecraft:blue_wool',
        4,
        'minecraft:block/purple_wool'
    ).id('kubejs:loom');

    tfc.pot_jam(
        '3x tfc:jar/banana',
        [
            'minecraft:stick',
            TFC.ingredient.notRotten('minecraft:cooked_porkchop')
        ],
        Fluid.of('minecraft:water', 500),
        50,
        400,
        'tfc:block/jar/banana'
    ).id('kubejs:jam_pot');

    tfc.pot(
        [
            'minecraft:dirt',
            'minecraft:stone'
        ],
        Fluid.of('minecraft:lava', 750),
        100,
        750
    ).outputs(
        [
            'minecraft:grass',
            'minecraft:red_stained_glass'
        ],
        Fluid.of('minecraft:water', 50)
    ).id('kubejs:simple_pot');

    tfc.pot_soup(
        [
            'minecraft:red_stained_glass',
            '#minecraft:flowers'
        ],
        Fluid.of('minecraft:water', 750),
        845,
        300
    ).id('kubejs:soup_pot');

    tfc.quern('minecraft:gravel', 'minecraft:cobblestone')
        .id('kubejs:quern');

    tfc.scraping(
        'minecraft:paper',
        '#minecraft:flowers',
        'minecraft:block/dirt',
        'minecraft:block/red_stained_glass'
    ).id('kubejs:scraping');

    tfc.sewing('minecraft:dirt', [
        0, 0, 0, 0, 0, 0, 0, 0, 0,
        1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 1, 0, 1, 0, 1, 0, 1, 0,
        1, 0, 1, 0, 1, 0, 1, 0, 1,
        0, 0, 0, 0, 0, 0, 0, 0, 0
    ], [
        -1, 1, 0, 0, 0, 0, 1, -1,
        1, 0, 0, 1, 1, 0, 0, 1,
        1, 0, 1, 0, 0, 1, 0, 1,
        -1, 1, 0, 0, 0, 0, 1, -1
    ]).id('kubejs:sewing');

    tfc.advanced_shaped_crafting('tfc:food/red_apple', [
        'FLK',
        'KLF'
    ], {
        F: '#minecraft:flowers',
        L: 'minecraft:dirt',
        K: 'tfc:food/red_apple'
    }, 0, 1).id('kubejs:adv_shaped');
    tfc.advanced_shaped_crafting(TFC.isp.of('tfc:metal/ingot/silver').addHeat(500), [
        'AAF'
    ], {
        A: 'minecraft:oak_log',
        F: '#tfc:saws'
    }, 0, 0)
        .damageIngredient('#tfc:saws', 5)
        .id('kubejs:ingredient_action_shaped');

    tfc.advanced_shapeless_crafting('minecraft:dirt', ['minecraft:stone', 'minecraft:cobblestone'])
        .id('kubejs:adv_shapeless_0');
    tfc.advanced_shapeless_crafting(TFC.itemStackProvider.copyInput().simpleModifier('kubejs:test'), ['tfc:metal/shovel/bronze', 'tfc:metal/shovel/bismuth_bronze'], 'tfc:metal/shovel/bronze')
        .id('kubejs:test_modifier')
    tfc.advanced_shapeless_crafting(TFC.itemStackProvider.copyInput(), ['tfc:food/red_apple', 'minecraft:stone'], 'tfc:food/red_apple')
        .id('kubejs:adv_shapeless_1');
    tfc.advanced_shapeless_crafting(TFC.isp.of('tfc:food/green_apple').addTrait('tfc:wild'), ['tfc:metal/ingot/silver', 'minecraft:water_bucket', '#tfc:saws'])
        .damageIngredient('#tfc:saws', 10)
        .id('kubejs:ingredient_action_shapeless');

    tfc.damage_inputs_shaped_crafting(mc.crafting_shaped('minecraft:dirt', [
        'MMN'
    ], {
        M: 'minecraft:stone',
        N: '#tfc:knives'
    })).id('kubejs:dmg_shaped');

    tfc.damage_inputs_shapeless_crafting(mc.crafting_shapeless('minecraft:stone', ['#minecraft:flowers', '#minecraft:axes']))
        .id('kubejs:dmg_shapeless');

    tfc.extra_products_shaped_crafting('3x minecraft:red_stained_glass', mc.crafting_shaped('minecraft:dirt', [
        'GHJ'
    ], {
        G: '#minecraft:flowers',
        H: 'minecraft:stone',
        J: 'tfc:rock/raw/diorite'
    })).id('kubejs:extra_shaped');

    tfc.extra_products_shapeless_crafting('4x minecraft:green_stained_glass_pane', mc.crafting_shapeless('minecraft:red_stained_glass', ['minecraft:dirt', '#minecraft:flowers']))
        .id('kubejs:extra_shapeless');

    tfc.no_remainder_shaped_crafting(minecraft.crafting_shaped('3x minecraft:ice', [
        'SAS'
    ], {
        S: 'minecraft:ice',
        A: 'minecraft:water_bucket'
    })).id('kubejs:no_remain_shaped');

    tfc.no_remainder_shapeless_crafting(minecraft.crafting_shapeless('minecraft:obsidian', ['minecraft:lava_bucket', 'minecraft:water_bucket']))
        .id('kubejs:no_remain_shapeless');

    tfc.damage_inputs_shapeless_crafting(
            tfc.advanced_shapeless_crafting(
                TFC.itemStackProvider.of('2x minecraft:bread')
                    .meal(
                        food => { food.protein(1); },
                        [
                            portion => {
                                portion.ingredient('tfc:food/cod');
                                portion.nutrientModifier(0.5);
                            }
                        ]
                    ),
                [
                    TFC.ingredient.notRotten('tfc:food/cod'),
                    '#forge:tools/knives'
                ],
                'tfc:food/cod'
            )
    ).id('kubejs:isp_meal');
}

ServerEvents.recipes(e => {
    let { tfc, minecraft } = e.recipes

    tfc(tfc, minecraft);
})
