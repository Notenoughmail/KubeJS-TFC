StartupEvents.registry('block', e => {
    e.create('lamp', 'tfc:lamp')
        .texture('particle', 'minecraft:block/chain')
        .texture('metal', 'tfc:block/alabaster/bricks/blue')
        .texture('chain', 'minecraft:block/chain');
})
