TFCEvents.data(e => {
    e.knappingType({
        inputItem: Ingredient.of('minecraft:flint', 2),
        clickSound: 'minecraft:entity.warden.roar',
        spawnParticles: true,
        icon: 'minecraft:flint',
        amountToConsume: 2
    }, 'kubejs:knap');
})
