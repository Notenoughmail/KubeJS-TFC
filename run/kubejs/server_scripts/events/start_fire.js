TFCEvents.startFire(event => {
    let { block } = event;
    let { level, pos, id } = block;
    let { x, y, z } = pos;
    let { random } = level;

    if ((id == 'tfc:dead_torch' || id == 'tfc:dead_wall_torch')) {
        level.playSound(null, x, y, z, 'minecraft:item.flintandsteel.use', 'blocks', 1, 1);
        level.sendParticles(
            Utils.getRegistry('minecraft:particle_type').getValue('minecraft:flame'),
            x + 0.5,
            y + 0.5,
            z + 0.5,
            4,
            0.1,
            0.1,
            0.1,
            0.02
        );
        level.sendParticles(
            Utils.getRegistry('minecraft:particle_type').getValue('minecraft:smoke'),
            x + 0.5,
            y + 0.5,
            z + 0.5,
            2,
            0.05,
            0.05,
            0.05,
            0.01
        );
    }
})
