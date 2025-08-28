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
})
