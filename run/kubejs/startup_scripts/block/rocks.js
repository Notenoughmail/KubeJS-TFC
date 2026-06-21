StartupEvents.registry('block', e => {
    e.create('loose_rock', 'tfc:loose_rock')
        .texture('tfc:block/rock/smooth/marble');
    e.create('groundcover', 'tfc:groundcover')
        .texture('tfc:block/alabaster/raw/green');
    e.create('rock_spike', 'tfc:rock_spike')
        .texture('tfc:block/alabaster/raw/pink')
        .anchor(a => {
            a.texture('tfc:block/alabaster/raw/pink');
        });
    e.create('thin_spike', 'tfc:thin_spike')
        .texture('tfc:block/alabaster/polished/red');
    e.create('raw', 'tfc:raw_rock')
        .stoneSoundType()
        .texture('tfc:block/rock/cracked_bricks/dacite');
    e.create('hardened', 'tfc:raw_rock')
        .texture('tfc:block/rock/cracked_bricks/gabbro');
    e.create('ore', 'tfc:groundcover')
        .texture('minecraft:block/gold_block')
        .collision()
        .ore()
        .withPreexistingItem('minecraft:gold_nugget')
        .hardness(0.1);
    e.create('gc_model_test', 'tfc:groundcover')
        .parentModel('minecraft:block/cobblestone')
})
