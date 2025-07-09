StartupEvents.registry('block', e => {
    e.create('crop', 'tfc:crop')
        .model('minecraft:block/deepslate');
    e.create('flooded_crop', 'tfc:flooded_crop');
    e.create('pickable_crop', 'tfc:pickable_crop')
        .setModel(m => m.parent('minecraft:block/light_blue_wool'));
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

    e.create('crop_2', 'tfc:crop')
        .expiryModifier(250)
        .stages(2)
        .model(0, 'minecraft:block/white_wool')
        .setModel(1, m => m.parent('minecraft:block/red_wool'));
    e.create('crop_4', 'tfc:crop')
        .growthModifier(2)
        .stages(4)
        .model(0, 'minecraft:block/white_wool')
        .model(1, 'minecraft:block/red_wool')
        .model(2, 'minecraft:block/pink_wool')
        .model(3, 'minecraft:block/black_wool');
    e.create('crop_8', 'tfc:crop')
        .growthModifier(4)
        .stages(8)
        .model(0, 'minecraft:block/white_wool')
        .model(1, 'minecraft:block/red_wool')
        .model(2, 'minecraft:block/pink_wool')
        .model(3, 'minecraft:block/black_wool')
        .model(4, 'minecraft:block/green_wool')
        .model(5, 'minecraft:block/yellow_wool')
        .model(6, 'minecraft:block/lime_wool')
        .model(7, 'minecraft:block/blue_wool')
})
