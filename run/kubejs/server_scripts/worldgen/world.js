
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
                .noiseSettings('kubejs:nether')
                .biomeSource(
                    'minecraft:fixed',
                    source => source.biome('minecraft:nether_wastes')
                )
            )
            .key('nether')
            .settings(settings => settings
                // .withRocks(GEN_ROCKS)
                .withRock('nether', {
                    raw: 'minecraft:netherrack',
                    hardened: 'minecraft:netherrack',
                    gravel: 'minecraft:netherrack',
                    cobble: 'minecraft:netherrack',
                    sand: 'minecraft:netherrack',
                    sandstone: 'minecraft:netherrack'
                })
                .withRock('nether2', {
                    raw: 'minecraft:basalt',
                    hardened: 'minecraft:basalt',
                    gravel: 'minecraft:basalt',
                    cobble: 'minecraft:basalt',
                    sand: 'minecraft:basalt',
                    sandstone: 'minecraft:basalt'
                })
                .withRock('nether3', {
                    raw: 'minecraft:blackstone',
                    hardened: 'minecraft:blackstone',
                    gravel: 'minecraft:blackstone',
                    cobble: 'minecraft:blackstone',
                    sand: 'minecraft:blackstone',
                    sandstone: 'minecraft:blackstone'
                })
                .bottomRocks('nether3')
                .layer('nether2', { nether2: 'bottom' })
                .layer('nether', { nether: 'nether2' })
                .oceanFloorLayers('nether')
                .landLayers('nether')
                .volcanicLayers('nether')
                .upliftLayers('nether')
            )
        )
        .addToNormalPresetList()
        .asDefaultPreset()
})

ServerEvents.registry('worldgen/noise_settings', e => {
    e.create('kubejs:nether')
        .defaults('minecraft:stone', 'minecraft:lava')
        .surfaceRule({
            type: 'kubejs_tfc:rock',
            rock_block: 'hardened',
            fallback_state: 'minecraft:stone'
        })
        .noiseRouter({
            barrierNoise: 0,
            fluidLevelFloodednessNoise: 0,
            fluidLevelSpreadNoise: 0,
            lavaNoise: 0,
            temperature: {
                shifted_noise: {
                    shift_x: 'minecraft:shift_x',
                    shift_y: 0,
                    shift_z: 'minecraft:shift_z',
                    xz_scale: 0.25,
                    y_scale: 0,
                    noise: 'minecraft:temperature'
                }
            },
            vegetation: {
                shifted_noise: {
                    shift_x: 'minecraft:shift_x',
                    shift_y: 0,
                    shift_z: 'minecraft:shift_z',
                    xz_scale: 0.25,
                    y_scale: 0,
                    noise: 'minecraft:vegetation'
                }
            },
            continents: 0,
            erosion: 0,
            depth: 0,
            ridges: 0,
            initialDensityWithoutJaggedness: 0,
            finalDensity: {
                squeeze: {
                    mul: {
                        first: {
                            interpolated: {
                                shift: 'minecraft:jagged' // I am NOT figuring out what is happening in the nether here
                            }
                        },
                        second: 0.64
                    }
                }
            },
            veinToggle: 0,
            veinRidged: 0,
            veinGap: 0
        })
})
