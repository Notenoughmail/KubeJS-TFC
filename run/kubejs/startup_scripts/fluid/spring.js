
StartupEvents.registry('fluid', e => {
    e.create('spring', 'tfc:spring')
        .stillTexture('minecraft:block/cobblestone')
        .flowingTexture('minecraft:block/stone')
        .healingAmount(5);
})
