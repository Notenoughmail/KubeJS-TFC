ServerEvents.recipes(e => {
    let { tfc, minecraft } = e.recipes

    tfc.alloy(
        'minecraft:lava',
        [
            {
                min: 0.2,
                max: 0.8,
                fluid: 'minecraft:water'
            },
            {
                min: 0.2,
                max: 0.8,
                fluid: 'minecraft:milk'
            }
        ]
    ).id('kubejs:alloy');

    tfc.anvil(
        TFC.isp.of('tfc:food/green_apple')
            .chance(0.5),
        'minecraft:dirt',
        [
            'shrink_any'
        ]
    ).tier(2).id('kubejs:anvil');

    tfc.barrel_instant(
        Fluid.of('minecraft:water', 50)
    ).outputs('minecraft:cobblestone', Fluid.of('minecraft:milk', 20))
        .id('kubejs:barrel_instant');
    tfc.instant_barrel(
        Fluid.of('minecraft:lava', 800)
    ).inputItem('minecraft:dirt')
        .id('kubejs:instant_barrel_alias')

    tfc.barrel_instant_fluid(
        Fluid.water(600),
        Fluid.of('minecraft:milk', 50)
    ).outputFluid(Fluid.lava(20))
        .id('kubejs:barrel_instant_fluid');
    tfc.instant_fluid_barrel(
        Fluid.of('minecraft:milk', 400),
        Fluid.lava(1)
    ).outputFluid(Fluid.water(8))
        .id('kubejs:instant_fluid_barrel_alias');

    tfc.barrel_sealed(
        Fluid.water(40),
        200
    ).seal('minecraft:dirt', 'minecraft:cobblestone')
        .outputItem(TFC.isp.of('minecraft:cobblestone').addHeat(500))
        .id('kubejs:barrel_sealed');
    tfc.sealed_barrel(
        Fluid.lava(80),
        10
    ).outputs('minecraft:obsidian', Fluid.water(20))
        .id('kubejs:sealed_barrel_alias');

    tfc.blast_furnace(
        Fluid.of('tfc:metal/copper', 25),
        '#minecraft:flowers',
        Fluid.of('tfc:metal/cast_iron', 50)
    ).id('kubejs:blast_furnace');

    tfc.bloomery(
        TFC.isp.of('tfc:metal/ingot/wrought_iron')
            .chance(0.12),
        'minecraft:sand',
        Fluid.of('tfc:metal/copper', 30),
        300
    ).id('kubejs:bloomery');

    tfc.casting(
        'minecraft:gold_ingot',
        'kubejs:mold',
        Fluid.lava(100)
    ).id('kubejs:casting');

    tfc.chisel(
        'minecraft:oak_planks',
        'minecraft:oak_log',
        'kubejs:mode',
        'minecraft:gold_nugget'
    ).id('kubejs:chisel');

    tfc.collapse(
        'minecraft:gold_block',
        'minecraft:iron_block'
    ).id('kubejs:collapse');

    tfc.glassworking(
        'minecraft:tinted_glass',
        'minecraft:sand',
        [
            'kubejs:operation',
            'tfc:basin_pour'
        ]
    ).id('kubejs:glassworking');

    tfc.heating(
        'minecraft:iron_axe',
        5000
    ).outputs('minecraft:rose_bush', Fluid.of('tfc:metal/gold', 20))
        .useDurability()
        .id('kubejs:heating');

    tfc.knapping(
        'minecraft:obsidian',
        'tfc:clay',
        [
            'X X',
            ' X ',
            'X X'
        ]
    ).defaultOn()
        .id('kubejs:knapping');

    tfc.landslide(
        'minecraft:diamond_block'
    ).id('kubejs:landslide');

    tfc.loom(
        'minecraft:obsidian',
        'minecraft:black_wool',
        5,
        'minecraft:block/black_concrete'
    ).id('kubejs:loom');

    tfc.pot(
        [
            '#minecraft:flowers'
        ],
        Fluid.water(1000),
        50,
        200
    ).outputs('minecraft:bread', Fluid.of('minecraft:milk', 20))
        .id('kubejs:pot');

    tfc.pot_jam(
        'minecraft:obsidian',
        'minecraft:black_concrete',
        [
            'minecraft:black_concrete_powder'
        ],
        Fluid.of('minecraft:milk', 20),
        10,
        500,
        'minecraft:block/black_glazed_terracotta'
    ).id('kubejs:pot_jam');
    tfc.jam_pot(
        'minecraft:red_wool',
        'minecraft:red_concrete',
        [
            'minecraft:red_concrete_powder'
        ],
        Fluid.water(100),
        1,
        50,
        'minecraft:block/green_wool'
    ).id('kubejs:jam_pot_alias');

    tfc.pot_soup(
        [
            'minecraft:rotten_flesh',
            'minecraft:oak_log'
        ],
        Fluid.of('minecraft:milk', 20),
        90,
        10
    ).id('kubejs:pot_soup');
    tfc.soup_pot(
        [
            'minecraft:wheat'
        ],
        Fluid.water(20),
        2,
        94
    ).id('kubejs:soup_pot_alias');

    tfc.quern(
        TFC.isp.of('tfc:metal/ingot/copper')
            .addHeat(500),
        'tfc:metal/ingot/bismuth'
    ).id('kubejs:quern');

    tfc.scraping(
        'minecraft:pink_glazed_terracotta',
        'minecraft:paper',
        'minecraft:block/pink_glazed_terracotta',
        'minecraft:block/white_glazed_terracotta',
    ).extraDrop('minecraft:torch')
        .id('kubejs:scraping');

    tfc.sewing(
        'minecraft:dirt',
        [
            'XX XXX XX',
            'X X   X X',
            ' X XXX X ',
            'X X   X X',
            'XX XXX XX'
        ],
        [
            '## ## ##',
            '## ## ##',
            '## ## ##',
            '## ## ##'
        ]
    ).id('kubejs:sewing')

    tfc.welding(
        'minecraft:obsidian',
        'minecraft:water_bucket',
        'minecraft:lava_bucket'
    ).tier(4)
        .id('kubejs:welding');

    tfc.advanced_shaped_crafting(
        'minecraft:bread',
        [
            'SA'
        ],
        {
            S: 'minecraft:oak_log',
            A: 'minecraft:water_bucket'
        }
    ).inputPosition(0, 1)
        .id('kubejs:advanced_shaped_crafting');
    tfc.shaped(
        'minecraft:obsidian',
        [
            'SA'
        ],
        {
            S: 'minecraft:lava_bucket',
            A: 'minecraft:water_bucket'
        }
    ).remainder(TFC.isp.empty())
        .id('kubejs:shaped_alias');

    tfc.advanced_shapeless_crafting(
        'minecraft:blue_glazed_terracotta',
        [
            'minecraft:blue_concrete',
            TFC.ingredient.fluidContents(Fluid.lava(50))
        ]
    ).primaryIngredient(TFC.ingredient.fluidContents(Fluid.lava(50)))
        .remainder(TFC.isp.copyInputStack().addHeat(500))
        .id('kubejs:advanced_shapeless_crafting')
    tfc.shapeless(
        'minecraft:dirt',
        [
            '#minecraft:flowers',
            'minecraft:water_bucket'
        ]
    ).id('kubejs:shapeless_alias');

    if (e.addedRecipes.stream().filter(r => r.getId().startsWith('kubejs:')).toList().isEmpty()) {
        console.error('No added recipes, somehow')
    }
})
