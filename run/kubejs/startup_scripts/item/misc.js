StartupEvents.registry('item', e => {
    e.create('windmill_blade', 'tfc:windmill_blade')
        .bladeColor(0x545F977);
    e.create('glassworking', 'tfc:glassworking')
        .operation('kubejs:operation');
})
