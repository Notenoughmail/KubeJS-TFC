TFCEvents.data(e => {
    e.drinkable('tfc:metal/steel', d => {
        d.consumerChance(1);
        d.thirst(3);
        d.intoxication(500000);
        d.effect('minecraft:night_vision');
        d.effect('minecraft:glowing', effect => {
            effect.duration(400);
            effect.amplifier(4);
            effect.chance(0.5);
        });
    });
    e.drinkable('tfc:metal/copper', d => {
        d.food(f => {
            f.protein(3);
        });
    }, 'kubejs:drinkable')
})
