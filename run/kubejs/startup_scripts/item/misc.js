StartupEvents.registry('item', e => {
    e.create('windmill_blade', 'tfc:windmill_blade')
        .bladeColor(0x545F977);
    e.create('glassworking', 'tfc:glassworking')
        .operation('kubejs_quench');
    e.create('jar', 'tfc:jar')
        .placedModel('kubejs:block/test_jar');
    e.create('topped_jar', 'tfc:jar')
        .placedModel('kubejs:block/topped_jar');
})
