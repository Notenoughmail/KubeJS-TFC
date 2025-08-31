StartupEvents.registry('block', e => {
    e.create('loose_rock', 'tfc:loose_rock')
        .textureAll('tfc:block/rock/smooth/marble');
    e.create('ground_cover', 'tfc:ground_cover')
        .textureAll('tfc:block/alabaster/raw/green');
    e.create('rock_spike', 'tfc:rock_spike')
        .textureAll('tfc:block/alabaster/raw/pink');
    e.create('thin_spike', 'tfc:thin_spike')
        .textureAll('tfc:block/alabaster/polished/red');
    e.create('raw', 'tfc:raw_rock')
        .stoneSoundType()
        .felsicIgneousExtrusive()
        .naturallySupported(false)
        .textureAll('tfc:block/rock/cracked_bricks/dacite');
    e.create('hardened', 'tfc:raw_rock')
        .naturallySupported(true)
        .textureAll('tfc:block/rock/cracked_bricks/gabbro');
    e.create('ore', 'tfc:ground_cover')
        .textureAll('minecraft:block/gold_block')
        .collision()
        .notAxisAligned()
        .ore()
        .withPreexistingItem('minecraft:gold_nugget')
        .hardness(0.1);
})
