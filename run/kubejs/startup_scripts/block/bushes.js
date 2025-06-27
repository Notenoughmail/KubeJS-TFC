StartupEvents.registry('block', e => {
    e.create('stationary_bush', 'tfc:stationary_berry_bush')
        .productItem('minecraft:glow_berries')
        .lifecycle('march', 'healthy')
        .lifecycle('april', 'healthy')
        .lifecycle('may', 'flowering')
        .lifecycle('june', 'fruiting')
        .lifecycle('july', 'healthy')
        .models((lifecycle, stage, m) => {
            if (!lifecycle.active()) {
                m.parent(`tfc:block/plant/stationary_bush_${stage}`);
                m.texture('bush', 'tfc:block/berry_bush/dead_bush');
                m.texture('particle', 'tfc:block/mud/silt');
            }
        })
        .texture('flowering', 2, 'minecraft:block/flowering_azalea_leaves');
    e.create('spreading_bush', 'tfc:spreading_berry_bush')
        .productItem('minecraft:glow_berries')
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
                m.texture('particle', 'tfc:block/mud/silt')
            }
        })
        // Deprecated!
        .allModels((lifecycle, stage) => {
            if (lifecycle.active()) return null;
            return m => {
                m.parent(`tfc:block/plant/stationary_bush_${stage}`)
                m.texture('bush', 'tfc:block/berry_bush/dead_bush')
                m.texture('particle', 'tfc:block/mud/silt')
            };
        })
        .model('healthy', 0, m => {
            m.parent('minecraft:block/cobblestone');
        })
        .texture('flowering', 2, 'minecraft:block/flowering_azalea_leaves');
})
