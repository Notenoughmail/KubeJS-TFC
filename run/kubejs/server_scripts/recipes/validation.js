const err = (r) => console.error(`Recipe ${r} had no original recipe ingredient`)

ServerEvents.recipes(e => {
    e.forEachRecipe([
        { mod: 'tfc' },
        { mod: 'firmalife' },
        { mod: 'afc' },
        { mod: 'kubejs' }
    ], r => {
        if (r.originalRecipeIngredients.empty) {
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
