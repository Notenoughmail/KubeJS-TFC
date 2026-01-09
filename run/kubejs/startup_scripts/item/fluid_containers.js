StartupEvents.registry('item', e => {
    e.create('mold', 'tfc:mold');
    e.create('jug', 'tfc:jug')
        .filledDisplayName('Jug of %s')
        .displayName('Really Really Really Really Cool Jug');
    e.create('bottle', 'tfc:glass_bottle')
        .textures('tfc:item/ceramic/jug_empty', 'tfc:item/ceramic/jug_overlay')
        .displayName('Totally Normal Bottle');
    e.create('fluid_container', 'tfc:fluid_container');
})
