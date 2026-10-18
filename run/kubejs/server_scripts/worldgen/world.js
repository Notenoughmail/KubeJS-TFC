
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

const GEN_ROCKS = {}

TFC.data.rocks.forEach((id, _) => GEN_ROCKS[id.path] = id)

ServerEvents.registry('worldgen/world_preset', event => {
    event.create('kubejs:fun')
        .withDimension('minecraft:overworld', 'minecraft:overworld', 'tfc:overworld', generator => generator
            .noiseSettings('minecraft:overworld')
            .biomeSource('tfc:region', _ => {})
            .settings(settings => settings
                .withRocks(GEN_ROCKS)
                .bottomRocks('gneiss', 'schist', 'diorite', 'granite', 'gabbro')
                .layer('felsic', { granite: 'bottom' })
                .layer('intermediate', { diorite: 'bottom' })
                .layer('mafic', { gabbro: 'bottom' })
                .layer('igneous_extrusive', {
                    andesite: 'intermediate',
                    basalt: 'mafic',
                    dacite: 'intermediate',
                    rhyolite: 'felsic'
                })
                .layer('igneous_extrusive_x2', {
                    andesite: 'igneous_extrusive',
                    basalt: 'igneous_extrusive',
                    dacite: 'igneous_extrusive',
                    rhyolite: 'igneous_extrusive'
                })
                .layer('igneous_intrusive', {
                    diorite: 'intermediate',
                    gabbro: 'mafic',
                    granite: 'felsic'
                })
                .layer('high_grade', {
                    gneiss: 'bottom',
                    schist: 'bottom'
                })
                .layer('low_grade', {
                    phyllite: 'high_grade',
                    slate: 'high_grade'
                })
                .layer('marble', { marble: 'bottom' })
                .layer('quartzite', { quartzite: 'bottom' })
                .layer('sedimentary', {
                    chalk: 'marble',
                    chert: 'quartzite',
                    claystone: 'low_grade',
                    conglomerate: 'low_grade',
                    dolomite: 'marble',
                    limestone: 'marble',
                    shale: 'low_grade'
                })
                .layer('uplift', {
                    diorite: 'low_grade',
                    gabbro: 'low_grade',
                    gneiss: 'high_grade',
                    granite: 'low_grade',
                    marble: 'bottom',
                    phyllite: 'high_grade',
                    quartzite: 'bottom',
                    schist: 'high_grade',
                    slate: 'high_grade'
                })
                .oceanFloorLayers('igneous_extrusive')
                .landLayers('sedimentary', 'sedimentary', 'sedimentary', 'igneous_extrusive')
                .volcanicLayers('igneous_extrusive', 'igneous_extrusive_x2', 'igneous_intrusive')
                .upliftLayers('uplift', 'uplift', 'uplift', 'sedimentary')
            )
        )
        .withDimension('minecraft:the_nether', 'minecraft:the_nether', 'kubejs_tfc:wrapped', generator => generator
            .wrappedGenerator('minecraft:noise', wrapped => wrapped
                .noiseSettings('minecraft:nether')
                .biomeSource(
                    'minecraft:fixed',
                    source => source.biome('minecraft:nether_wastes')
                )
            )
            .key('nether')
            .settings(settings => settings
                // .withRocks(GEN_ROCKS)
                .withRock('nether', 'kubejs:netherrack', rock => rock
                    .raw('minecraft:netherrack')
                    .hardened('minecraft:blackstone')
                    .gravel('minecraft:gravel')
                    .cobble('minecraft:basalt')
                    .sand('minecraft:soul_sand')
                    .sandstone('minecraft:soul_soil')
                )
                .bottomRocks('nether')
                .layer('nether', { nether: 'bottom' })
                .oceanFloorLayers('nether')
                .landLayers('nether')
                .volcanicLayers('nether')
                .upliftLayers('nether')
            )
        )
        .addToNormalPresetList()
        .asDefaultPreset()
})