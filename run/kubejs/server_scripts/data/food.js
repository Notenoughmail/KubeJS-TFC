TFCEvents.data(e => {
    e.foodItem('minecraft:cooked_porkchop', d => {
        d.hunger(3);
        d.saturation(2);
        d.protein(4);
    });
    e.foodItem('minecraft:apple', d => {
        d.fruit(4);
        d.hunger(5);
    }, 'kubejs:food_item');
})
