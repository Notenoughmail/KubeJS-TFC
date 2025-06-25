StartupEvents.registry('item', e => {
    e.create('mold', 'tfc:mold');
    e.create('jug', 'tfc:jug')
        .filledDisplayName('Jug of %s')
        .displayName('Really Really Really Really Cool Jug');
    e.create('bottle', 'tfc:glass_bottle')
        .displayName('Totally Normal Bottle');
    e.create('fluid_container', 'tfc:fluid_container');
})
