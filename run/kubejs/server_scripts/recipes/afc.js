// requires afc

ServerEvents.recipes(e => {
    let { afc } = e.recipes;

    afc.tree_tapping(
        Fluid.of('minecraft:milk', 50),
        'minecraft:oak_log'
    ).requiresNaturalLog(false)
        .tempRange(12, 60)
        .id('kubejs:tree_tapping');
})