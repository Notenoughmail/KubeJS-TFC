const err = (r) => console.error(`Recipe ${r} had no original recipe ingredient`);
const recipeTypesCheck = [ 'tfc', 'firmalife', 'artisanal', 'afc', 'kubejs_tfc' ]; // I do not care about vanilla recipe types

ServerEvents.recipes(e => {
    /*

    e.forEachRecipe([
        { mod: 'tfc' },
        { mod: 'firmalife' },
        { mod: 'afc' },
        { mod: 'kubejs' },
        { mod: 'artisanal' }
    ], r => {
        if (recipeTypesCheck.indexOf(r.getType().namespace) >= 0 && r.originalRecipeIngredients.empty) {
            let inputComponents = r.inputValues();
            for (let i = 0 ; i < inputComponents.length ; i++) {
                let compClass = inputComponents[i].key.component.componentClass().simpleName;
                if (compClass == 'Ingredient' || compClass == 'InputItem' || compClass == 'ItemStackIngredient') {
                    let type = r.getType();
                    if (type == 'tfc:chisel') {
                        if (r.get('item_ingredient') != null && !r.get('item_ingredient').empty) {
                            err(r);
                        }
                    } else if (type == 'tfc:knapping') {
                        if (r.get('ingredient') != null && !r.get('ingredient').empty) {
                            err(r);
                        }
                    } else if (type == 'tfc:barrel_sealed') {
                        if (r.get('input_item') != null && !r.get('input_item').empty && r.id != 'artisanal:barrel/large_waterproof_hide_rendered_fat') { // Artisanal does not set this recipe to be conditional on firmaciv actually being present
                            err(r);
                        }
                    } else if (type == 'firmalife:vat') {
                        if (r.get('input_item') != null && !r.get('input_item').empty) {
                            err(r);
                        }
                    } else {
                        err(r);
                    }
                }
            }
        }
    });

    var bool = true;
    e.forEachRecipe({ tfc: {
        type: 'is_tfc'
    }}, r => bool = false);
    if (bool) console.error('No TFC recipes?');
    bool = true;
    e.forEachRecipe({ tfc: {
        type: 'has_isp'
    }}, r => bool = false);
    if (bool) console.error('No ISP recipes?');
    bool = true;
    e.forEachRecipe({ tfc: {
        type: 'block',
        block: 'tfc:rock/raw/dolomite'
    }}, r => bool = false);
    if (bool) console.error('No dolomite block ingredient');
    bool = true;
    /*
    e.forEachRecipe({ tfc: {
        type: 'fluid',
        fluid: 'minecraft:water'
    }}, r => false);
    if (bool) console.error('No water ingredient');
    bool = true;
    */ // Nothing actually uses the basic fluid component
    e.forEachRecipe({ tfc: {
        type: 'fluid_stack',
        fluid: Fluid.of('minecraft:milk', 20)
    }}, r => bool = false);
    if (bool) console.error('No milk ingredients');
    bool = true;
    e.forEachRecipe({ tfc: {
        type: 'alloy_contents',
        contents: 'tfc:copper'
    }}, r => bool = false);
    if (bool) console.error('No copper alloys');
    bool = true;
    e.forEachRecipe({ tfc: {
        type: 'alloy_result',
        result: 'tfc:rose_gold'
    }}, r => bool = false);
    if (bool) console.error('No rose gold alloying');
    bool = true;
    e.forEachRecipe({ tfc: {
        type: 'isp',
        match: 'tfc:powder/sulfur'
    }}, r => bool = false);
    if (bool) console.error('No sulfur ISPs');
    bool = true;
    e.forEachRecipe({ tfc: {
        type: 'isp',
        modifiers: 'tfc:copy_input'
    }}, r => bool = false);
    if (bool) console.error('No copy input ISPs');
    bool = true;
    e.forEachRecipe({ tfc: {
        type: 'isp',
        match: 'tfc:blowpipe_with_glass',
        modifiers: 'tfc:add_glass'
    }}, r => bool = false);
    if (bool) console.error('No glass addition ISPs');

    let { tfc, minecraft } = e.recipes;

    let alloy = tfc.alloy(
        'tfc:copper',
        [
            TFC.alloyPart('tfc:rose_gold', 0.2, 0.3),
            TFC.alloyPart('tfc:black_steel', 0.7, 0.8)
        ]
    ).id('kubejs:validation/alloy_part_replacement');

    if (!alloy.replaceInput(TFC.alloyPart('tfc:rose_gold', 0, 0), TFC.alloyPart('tfc:red_steel', 0.5, 0.6, false))) console.error('Did not replace alloy 1');
    if (!alloy.replaceInput(TFC.alloyPart('tfc:black_steel', 0, 0), TFC.alloyPart('tfc:gold', 0.4, 0.5, false))) console.error('Did not replace alloy 2');

    let landslide = tfc.landslide(
        'minecraft:deepslate'
    ).id('kubejs:validation/block_ingredient_replacement');

    if (!landslide.replaceInput(BlockStatePredicate.of('minecraft:deepslate'), TFC.blockIngredient(['minecraft:hay_block', 'minecraft:end_gateway']))) console.error('Did not replace block ingredient');

    let barrel = tfc.barrel_instant()
        .outputItem('tfc:food/green_apple')
        .inputFluid(TFC.fluidStackIngredient(['minecraft:water', 'minecraft:lava'], 500))
        .id('kubejs:validation/fluid_stack_ingredient_replacement');

    if (!barrel.replaceInput(Fluid.of('minecraft:lava', 50), TFC.fluidStackIngredient('minecraft:milk', 70))) console.error('Did not replace fluid stack ingredient');

    let nested = tfc.damage_inputs_shaped_crafting(
        minecraft.crafting_shaped(
            'minecraft:dirt',
            [
                'SPA'
            ],
            {
                S: 'minecraft:stone',
                P: 'minecraft:dirt',
                A: '#minecraft:flowers'
            }
        )
    ).id('kubejs:validation/nested_recipe_replacement');

    if (!nested.replaceInput('minecraft:stone', '#tfc:saws')) console.error('Did not replace nested recipe component');

    let extra = tfc.extra_products_shaped_crafting(
        [
            'minecraft:dirt'
        ],
        minecraft.crafting_shaped(
            'minecraft:stone',
            [
                'S'
            ],
            {
                S: 'minecraft:sponge'
            }
        )
    ).id('kubejs:validation/extra_product_replacement');

    if (!extra.replaceOutput('minecraft:dirt', 'minecraft:oak_log')) console.error('Did not replace extra output');
    if (!extra.replaceOutput('minecraft:stone', 'minecraft:birch_log')) console.error('Did not replace internal output');

    let basicISP = tfc.quern(
        'minecraft:dirt',
        'minecraft:stone'
    ).id('kubejs:validation/basic_isp_replacement');

    if (!basicISP.replaceOutput('minecraft:dirt', TFC.isp.of('tfc:metal/ingot/gold').addHeat(500))) console.error('Did not replace base ISP');

    let modISP = tfc.quern(
        TFC.isp.of('minecraft:dirt').addHeat(500),
        'minecraft:gravel'
    ).id('kubejs:validation/modifier_isp_replacement');

    if (!modISP.replaceOutput(TFC.isp.of('minecraft:dirt').addHeat(0), TFC.isp.of('minecraft:oak_log'))) console.error('Did not replace base ISP');

    let failISP = tfc.quern(
        TFC.isp.of('tfc:food/cherry').copyOldestFood(), // Do not add a food trait here; somehow, when in combination with the output replacement, that causes a server config to be loaded early due to TFC marking recipe outputs as non-decaying...
        'tfc:food/cherry'
    ).id('kubejs:validation/fail_isp_replacement');

    if (failISP.replaceOutput(TFC.isp.of('tfc:food/cherry').addBait(), 'minecraft:dirt')) console.error('Replaced ISP despite not all modifiers matching');

    */
})
