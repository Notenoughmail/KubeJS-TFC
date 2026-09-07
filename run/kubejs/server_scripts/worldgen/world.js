
ServerEvents.registry('tfc:worldgen/rock_settings', event => {
    event.create('vanilla')
        .raw('minecraft:stone')
        .hardened('minecraft:deepslate')
        .gravel('minecraft:gravel')
        .cobble('minecraft:cobblestone')
        .sand('minecraft:sand')
        .sandstone('minecraft:sandstone')
        .mafic()
})