TFCEvents.startFire(event => {
    let { level, pos } = event;
    let { block } = level.getBlockState(pos);
    let { id } = block
    let { x, y, z } = pos;

    if (id == 'kubejs:torch_dead' || id == 'kubejs:torch_dead_wall') {
        level.sendParticles(
            'minecraft:flame',
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
            'minecraft:smoke',
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
