StartupEvents.registry('block', e => {
    e.create('stationary_bush', 'tfc:stationary_berry_bush')
        .withProduct('minecraft:glow_berries')
        .lifecycle('march', 'healthy')
        .lifecycle('april', 'healthy')
        .lifecycle('may', 'flowering')
        .lifecycle('june', 'fruiting')
        .lifecycle('july', 'healthy')
        .models((lifecycle, stage, m) => {
            if (!lifecycle.active()) {
                m.parent(`tfc:block/plant/stationary_bush_${stage}`);
                m.texture('bush', 'tfc:block/berry_bush/dead_bush');
                m.texture('particle', 'minecraft:block/dirt');
            }
        });
    e.create('spreading_bush', 'tfc:spreading_berry_bush')
        .withProduct('minecraft:glow_berries')
        .maxHeight(7)
        .lifecycle('march', 'healthy')
        .lifecycle('april', 'healthy')
        .lifecycle('may', 'flowering')
        .lifecycle('june', 'fruiting')
        .lifecycle('july', 'healthy')
        .models((lifecycle, stage,m ) => {
            if (!lifecycle.active()) {
                m.parent(`tfc:block/plant/stationary_bush_${stage}`)
                m.texture('bush', 'tfc:block/berry_bush/dead_bush')
                m.texture('particle', 'minecraft:block/dirt')
            }
        });
})
