TFCEvents.data(e => {
    e.entityDamageResistance({
        entity: 'tfc:fish',
        damages: {
            piercing: -100000
        }
    }, 'kubejs:fish_pierce');
    e.itemDamageResistance({
        ingredient: 'minecraft:iron_leggings',
        damages: {
            slashing: 100
        }
    });
})
