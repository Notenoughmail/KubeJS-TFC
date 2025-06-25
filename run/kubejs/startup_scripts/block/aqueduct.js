StartupEvents.registry('block', e => {
    e.create('aqueduct', 'tfc:aqueduct')
        .allowedFluids(['minecraft:water', 'minecraft:lava', 'tfc:salt_water', 'tfc:spring_water', 'kubejs:spring'])
        .textureAll('tfc:block/rock/magma/gabbro');
})
