ServerEvents.generateData('last', e => {
    /*
    e.json(
        'tfc:worldgen/world_preset/overworld',
        {
            dimensions: {
                'minecraft:overworld': {
                    type: 'minecraft:overworld',
                    generator: {
                        type: 'tfc:overworld',
                        biome_source: {
                            type: 'tfc:overworld'
                        },
                        settings: 'minecraft:overworld',
                        tfc_settings: {
                            spawn_distance: 4000,
                            spawn_center_x: 0,
                            spawn_center_z: 0,
                            grass_density: 0.5,
                            rock_layer_settings: {
                                rocks: { // TODO: 2.0.0 | To test this the entirety of the rock needs to be defined, there are no longer pre-defined settings
                                    granite: 'tfc:granite',
                                    diorite: 'tfc:diorite',
                                    gabbro: 'tfc:gabbro',
                                    shale: 'tfc:shale',
                                    claystone: 'tfc:claystone',
                                    limestone: 'tfc:limestone',
                                    conglomerate: 'tfc:conglomerate',
                                    dolomite: 'tfc:dolomite',
                                    chert: 'tfc:chert',
                                    chalk: 'tfc:chalk',
                                    rhyolite: 'tfc:rhyolite',
                                    basalt: 'tfc:basalt',
                                    andesite: 'tfc:andesite',
                                    dacite: 'tfc:dacite',
                                    quartzite: 'tfc:quartzite',
                                    slate: 'tfc:slate',
                                    phyllite: 'tfc:phyllite',
                                    schist: 'tfc:schist',
                                    gneiss: 'tfc:gneiss',
                                    marble: 'tfc:marble'
                                },
                                bottom: [
                                    'gneiss',
                                    'schist',
                                    'diorite',
                                    'granite',
                                    'gabbro'
                                ],
                                layers: [
                                    {
                                        id: 'felsic',
                                        layers: {
                                            granite: 'bottom'
                                        }
                                    },
                                    {
                                        id: 'intermediate',
                                        layers: {
                                            diorite: 'bottom'
                                        }
                                    },
                                    {
                                        id: 'mafic',
                                        layers: {
                                            gabbro: 'bottom'
                                        }
                                    },
                                    {
                                        id: 'igneous_extrusive',
                                        layers: {
                                            rhyolite: 'felsic',
                                            andesite: 'intermediate',
                                            dacite: 'intermediate',
                                            basalt: 'mafic'
                                        }
                                    },
                                    {
                                        id: 'igneous_extrusive_x2',
                                        layers: {
                                            rhyolite: 'igneous_extrusive',
                                            andesite: 'igneous_extrusive',
                                            dacite: 'igneous_extrusive',
                                            basalt: 'igneous_extrusive'
                                        }
                                    },
                                    {
                                        id: 'phyllite',
                                        layers: {
                                            phyllite: 'bottom',
                                            gneiss: 'bottom',
                                            schist: 'bottom'
                                        }
                                    },
                                    {
                                        id: 'slate',
                                        layers: {
                                            slate: 'bottom',
                                            phyllite: 'phyllite'
                                        }
                                    },
                                    {
                                        id: 'marble',
                                        layers: {
                                            marble: 'bottom'
                                        }
                                    },
                                    {
                                        id: 'quartzite',
                                        layers: {
                                            quartzite: 'bottom'
                                        }
                                    },
                                    {
                                        id: 'sedimentary',
                                        layers: {
                                            shale: 'slate',
                                            claystone: 'slate',
                                            conglomerate: 'slate',
                                            limestone: 'marble',
                                            dolomite: 'marble',
                                            chalk: 'marble',
                                            chert: 'quartzite'
                                        }
                                    },
                                    {
                                        id: 'uplift',
                                        layers: {
                                            slate: 'phyllite',
                                            marble: 'bottom',
                                            quartzite: 'bottom',
                                            diorite: 'sedimentary',
                                            granite: 'sedimentary',
                                            gabbro: 'sedimentary'
                                        }
                                    }
                                ],
                                ocean_floor: [ 'igneous_extrusive' ],
                                volcanic: [
                                    'igneous_extrusive',
                                    'igneous_extrusive_x2'
                                ],
                                land: [
                                    'igneous_extrusive',
                                    'sedimentary'
                                ],
                                uplift: [
                                    'sedimentary',
                                    'uplift'
                                ]
                            },
                            temperature_scale: 20000,
                            rainfall_scale: 20000,
                            flat_bedrock: false,
                            continentalness: 0.5,
                            finite_continents: false,
                        }
                    }
                },
                'minecraft:the_nether': {
                    type: 'minecraft:the_nether',
                    generator: {
                        type: 'kubejs_tfc:wrapped',
                        event_key: 'minecraft:the_nether',
                        generator: {
                            type: 'minecraft:noise',
                            biome_source: {
                                type: 'minecraft:multi_noise',
                                preset: 'minecraft:nether'
                            },
                            settings: 'minecraft:nether'
                        },
                        settings: {
                            flat_bedrock: true,
                            spawn_distance: 0,
                            spawn_center_x: 0,
                            spawn_center_z: 0,
                            temperature_scale: 0,
                            rainfall_scale: 0,
                            finite_continents: false,
                            grass_density: 0.5,
                            rock_layer_settings: {
                                rocks: {
                                    nether: {
                                        raw: 'minecraft:netherrack',
                                        hardened: 'minecraft:basalt',
                                        gravel: 'minecraft:gravel',
                                        cobble: 'minecraft:blackstone',
                                        sand: 'minecraft:soul_sand',
                                        sandstone: 'minecraft:soul_soil'
                                    }
                                },
                                bottom: [ 'nether' ],
                                layers: [
                                    {
                                        id: 'nether',
                                        layers: {
                                            nether: 'bottom'
                                        }
                                    }
                                ],
                                ocean_floor: [ 'nether' ],
                                volcanic: [ 'nether' ],
                                land: [ 'nether' ],
                                uplift: [ 'nether' ]
                            },
                            continentalness: 0
                        }
                    }
                },
                'minecraft:the_end': {
                    type: 'minecraft:the_end',
                    generator: {
                        type: 'minecraft:noise',
                        biome_source: {
                            type: 'minecraft:the_end'
                        },
                        settings: 'minecraft:end'
                    }
                }
            }
        }
    );
    */

    e.json(
        'minecraft:worldgen/structure_set/igloos',
        {
            placement: {
                type: 'tfc:climate',
                salt: 0,
                separation: 5,
                spacing: 6,
                climate: {
                    min_temperature: 50
                }
            },
            structures: [
                {
                    structure: 'minecraft:igloo',
                    weight: 1
                }
            ]
        }
    );

    /*
    e.addJson(
        'kubejs:forge/biome_modifier/nether_boulders',
        {
            type: 'forge:add_features',
            biomes: '#minecraft:is_nether',
            features: [
                'kubejs_tfc:nether_boulder',
                'kubejs:nether_boulder'
            ],
            step: 'fluid_springs'
        }
    );
    */
})
