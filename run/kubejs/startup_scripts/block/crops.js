StartupEvents.registry('block', e => {
    e.create('crop', 'tfc:crop')
        .models((i, m) => m.parent('minecraft:block/deepslate'));
    e.create('flooded_crop', 'tfc:flooded_crop');
    e.create('pickable_crop', 'tfc:pickable_crop')
        .models((i, m) => m.parent('minecraft:block/light_blue_wool'));
    e.create('spreading_crop', 'tfc:spreading_crop')
        .models((i, m) => m.texture('crop', 'minecraft:block/dirt'))
        .stages(7)
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
    e.create('double_crop_stick', 'tfc:climbing_crop')
        .deadBlock(d => {
            d.models((stage, m) => {
                if (stage.bottom()) {
                    m.texture('crop', 'minecraft:block/gravel');
                }
            });
        });
    e.create('wild_crop', 'tfc:wild_crop')
        .seeds('kubejs:crop_seeds')
        .food('minecraft:chorus_fruit');

    e.create('crop_2', 'tfc:crop')
        .expiryModifier(250)
        .stages(2);
    e.create('crop_4', 'tfc:crop')
        .growthModifier(2)
        .stages(4);
    e.create('crop_8', 'tfc:crop')
        .growthModifier(4)
        .stages(8);
})
