ServerEvents.recipes(e => {
    let { tfc, minecraft } = e.recipes

    tfc.alloy(
        'minecraft:lava',
        [
            {
                min: 0.2,
                max: 0.8,
                fluid: 'minecraft:water'
            },
            {
                min: 0.2,
                max: 0.8,
                fluid: 'minecraft:milk'
            }
        ]
    ).id('kubejs:alloy');

    tfc.anvil(
        TFC.isp.of('tfc:food/green_apple')
            .chance(0.5),
        'minecraft:dirt',
        [
            'shrink_any'
        ]
    ).tier(2).id('kubejs:anvil');

    tfc.barrel_instant(
        Fluid.of('minecraft:water', 50)
    ).outputs('minecraft:cobblestone', Fluid.of('minecraft:milk', 20))
        .id('kubejs:barrel_instant');
    tfc.instant_barrel(
        Fluid.of('minecraft:lava', 800)
    ).inputItem('minecraft:dirt')
        .id('kubejs:instant_barrel_alias')

    // barrel_instant_fluid / instant_fluid_barrel
    // barrel_sealed / sealed_barrel
    // blast_furnace
    // bloomery
    // casting
    // chisel
    // collapse
    // glassworking
    // heating
    // knapping
    // landslide
    // loom
    // pot
    // pot_jam / jam_pot
    // pot_soup / soup_pot
    // quern
    // scraping
    // sewing
    // welding

    // advanced_shaped_crafting / shaped
    // advanced_shapeless_crafting / shapeless

    if (e.addedRecipes.stream().filter(r => r.getId().startsWith('kubejs:')).toList().isEmpty()) {
        console.error('No added recipes, somehow')
    }
})
