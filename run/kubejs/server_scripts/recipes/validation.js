const err = (r) => console.error(`Recipe ${r} had no original recipe ingredient`);
const recipeTypesCheck = [ 'tfc', 'firmalife', 'artisanal', 'afc', 'kubejs_tfc' ]; // I do not care about vanilla recipe types

ServerEvents.recipes(e => {
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
        match: 'tfc:food/cooked_port',
        modifiers: 'tfc:salted'
    }}, r => bool = false);
    if (bool) console.error('No salted pork ISPs');
})
