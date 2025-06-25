StartupEvents.registry('block', e => {
    e.create('crop', 'tfc:crop');
    e.create('flooded_crop', 'tfc:flooded_crop');
    e.create('pickable_crop', 'tfc:pickable_crop');
    e.create('spreading_crop', 'tfc:spreading_crop')
        .deadBlock(d => {
            d.models((stage, m) => {
                if (stage.mature()) {
                    m.texture('crop', 'minecraft:block/dirt');
                } else {
                    m.texture('crop', 'minecraft:block/deepslate');
                }
            });
        });
    e.create('double_crop', 'tfc:double_crop')
        .deadBlock(d => {
            d.models((stage, m) => {
                if (!stage.mature()) {
                    m.texture('crop', 'minecraft:block/cobblestone');
                } else if (stage.bottom()) {
                    m.texture('crop', 'minecraft:block/sand');
                }
            });
        });
    e.create('double_crop_stick', 'tfc:double_crop')
        .requiresStick(true)
        .deadBlock(d => {
            d.models((stage, m) => {
                if (stage.bottom()) {
                    m.texture('crop', 'minecraft:block/gravel');
                }
            });
        });
    e.create('wild_crop', 'tfc:wild_crop')
        .seeds('kubejs:crop_seeds')
        .food('minecraft:chorus_fruit')
        .deadModel('minecraft:block/cobblestone');
})
