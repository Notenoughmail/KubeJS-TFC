// NOTICE: This file is now deprecated as an example source, please use the wiki
// from now on. This file wil be removed sometime during the 0.2.1 update cycle

// This is a collection of the various dumb recipes made throughout dev testing
// This is provided here for people to look over/reverse engineer while the wiki is in progress

settings.logAddedRecipes = true
settings.logRemovedRecipes = true
settings.logSkippedRecipes = false
settings.logErroringRecipes = false

// console.warn(ItemProvider.of('3x minecraft:oak_log', ['tfc:copy_food', {type:'tfc:add_heat', temperature:4000}]))
// console.warn(ItemProvider.of('3x minecraft:oak_log', 'tfc:copy_food'))
// console.warn(ItemProvider.of('3x tfc:wood/barrel/acacia').simpleModifier('tfc:copy_food').copyForgingBonus())
// console.warn(ItemProvider.empty().copyFood().toJson())

onEvent('recipes', event => {

	event.recipes.tfc.clay_knapping('tfc:large_prepared_hide', ['XXX', 'X X', 'XXX']).id('kubejs:hole');
	event.recipes.tfcFireClayKnapping('tfc:large_prepared_hide', ['XXX', ' X ', 'XXX']).outsideSlotNotRequired().id('kubejs:i');
	event.recipes.tfc_leather_knapping('tfc:large_prepared_hide', ['X   X', ' X X ', '  X  ', ' X X ', 'X   X']).id('kubejs:x');
	event.recipes.tfc.rock_knapping('tfc:large_prepared_hide', ['XX', 'XX', 'XX'], '#tfc:sedimentary_rock').id('kubejs:rock');
	
	event.recipes.tfc.welding('2x tfc:metal/ingot/bronze', ['tfc:metal/ingot/black_bronze', 'tfc:metal/ingot/bismuth_bronze']).tier(3).id('kubejs:dumb_weld');
	event.recipes.tfc.welding(ItemProvider.of('2x tfc:metal/rod/bronze', [{type:'tfc:add_heat', temperature:4000}]), ['tfc:metal/rod/bismuth_bronze', 'tfc:metal/rod/bismuth_bronze']).id('kubejs:dumber_weld');
	
	event.recipes.tfc.alloy('tfc:steel', [
	    ['tfc:copper', 0.2, 0.3],
	    ['tfc:rose_gold', 0.4, 0.8],
	    ['tfc:black_steel', 0.2, 0.5]
	]).id('kubejs:alloy');
	
	event.recipes.tfc.scraping('5x minecraft:oak_log', 'tfc:metal/double_sheet/rose_gold').inputTexture('tfc:item/hide/large/scraped').outputTexture('tfc:item/hide/large/soaked').id('kubejs:scrape');
	
	event.recipes.tfc.quern('3x minecraft:bone', 'minecraft:bone_meal').id('kubejs:scrape');
	
	event.recipes.tfc.landslide('minecraft:diamond_block', 'minecraft:gold_block').id('kubejs:landslide');
	event.recipes.tfc.landslide(true, 'minecraft:dark_oak_log').id('kubejs:log_slide');
	event.recipes.tfc.landslide('minecraft:oak_log[axis=y]', 'minecraft:oak_log').id('kubejs:slide_rotate');
	
	event.recipes.tfc.collapse(true, 'minecraft:diamond_block').id('kubejs:collapse');
	
	event.recipes.tfc.anvil('tfc:metal/ingot/bronze', 'tfc:metal/rod/bismuth_bronze', ['draw_not_last', 'shrink_any']).id('kubejs:anvil');
	event.recipes.tfc.anvil(ItemProvider.of('2x tfc:metal/rod/bismuth_bronze', [{type:'tfc:add_heat', temperature:4000}]), 'tfc:metal/rod/bismuth_bronze', ['draw_not_last', 'shrink_any']).applyBonus().tier(5).id('kubejs:keep_bonus');
	event.recipes.tfc.anvil('tfc:metal/sheet/rose_gold', 'tfc:metal/double_sheet/rose_gold', ['draw_not_last', 'shrink_any']).tier(4).id('kubejs:flatten_sheet');
	
	event.recipes.tfc.chisel('minecraft:diamond_block', '#minecraft:saplings', 'slab').extraDrop('5x tfc:metal/rod/brass').itemIngredients('tfc:metal/chisel/copper', 'tfc:metal/chisel/black_steel', 'tfc:metal/chisel/blue_steel').id('kubejs:js_chisel');
	
	event.recipes.tfc.casting('minecraft:oak_log', 'tfc:large_prepared_hide', FluidIngredient.of(['tfc:metal/bismuth_bronze', '#tfc:sterling_silver'], 200), 1).id('kubejs:casting');
	
	event.recipes.tfc.bloomery('minecraft:oak_log', Fluid.of('tfc:metal/bismuth_bronze', 2), 'minecraft:bucket', 20).id('kubejs:bloom');
	
	event.recipes.tfc.blast_furnace(Fluid.of('tfc:metal/bismuth_bronze', 1), Fluid.of('tfc:metal/zinc', 1), 'minecraft:bucket').id('kubejs:blast');
	
	event.recipes.tfc.barrel_instant('5x minecraft:oak_log', '2x minecraft:diamond_block', FluidIngredient.of(['minecraft:water', '#tfc:sterling_silver'], 200)).id('kubejs:if_to_i_instant');  // Good
	event.recipes.tfc.barrel_instant(Fluid.of('tfc:corn_whiskey', 20), '2x minecraft:oak_log', Fluid.of('minecraft:milk', 50)).id('kubejs:if_to_f_instant');
	event.recipes.tfc.barrel_instant(['3x minecraft:diamond_block', Fluid.of('tfc:corn_whiskey', 20)], '2x minecraft:dark_oak_log', FluidIngredient.of(['minecraft:water'], 30)).id('kubejs:if_to_if_instant');
	event.recipes.tfc.barrel_instant(ItemProvider.of('6x tfc:metal/shovel/bismuth_bronze', [{type:'tfc:add_heat', temperature:2000}]), '3x minecraft:dark_oak_log', Fluid.water()).id('kubejs:if_to_i_isp')
 	
	event.recipes.tfc.barrel_instant_fluid(Fluid.of('minecraft:milk'), FluidIngredient.water(5), Fluid.of('tfc:metal/zinc', 5)).id('kubejs:fluid_instant');
	
	event.recipes.tfc.barrel_sealed([Fluid.of('minecraft:milk', 20), '3x minecraft:oak_log'], [FluidIngredient.of('minecraft:water', 200), '3x minecraft:oak_log'], 200).id('kubejs:barrel_seal');
	
	event.recipes.tfc.loom('minecraft:oak_log', 'minecraft:oak_planks');
	
	event.recipes.tfc.pot([Fluid.of('tfc:light_blue_dye', 50), 'minecraft:oak_log'], [Fluid.of('minecraft:water', 60), 'minecraft:cornflower'], 20, 50).id('kubejs:pot');
	
	event.recipes.tfc.heating(Fluid.of('minecraft:water', 50), 'minecraft:clay', 50).id('kubejs:clay_to_water');
	event.recipes.tfc.heating('tfc:fire_clay', 50).id('kubejs:delete_fire_clay');
})

// Tags added to fix ingredients and results not being allowed to perform a recipe
// https://terrafirmacraft.github.io/Documentation/1.18.x/data/tags/

onEvent('item.tags', event => {
	event.add('tfc:scrapable', 'tfc:metal/double_sheet/rose_gold')
})

onEvent('block.tags', event => {
    event.add('tfc:can_landslide', 'minecraft:oak_log')
    event.add('tfc:can_landslide', 'minecraft:dark_oak_log')
    event.add('tfc:can_landslide', 'minecraft:diamond_block')
    event.add('tfc:can_landslide', 'minecraft:gold_block')
    event.add('tfc:can_collapse', 'minecraft:diamond_block')
})