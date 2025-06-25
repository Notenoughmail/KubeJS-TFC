StartupEvents.registry('block', e => {
    e.create('inventory')
        .blockEntity(be => {
            be.attach('tfc:inventory', {
                width: 9,
                height: 1,
                size: size => size.isSmallerThan('normal')
            });
            be.rightClickOpensInventory();
        })
        .textureAll('tfc:block/mud/silt');
    e.create('heat')
        .blockEntity(be => {
            be.attach('tfc:heat', {
                temperatureCallback: (be, t, c, j) => {
                    return Math.min(t + 1, 1500);
                },
                providesHeat: true
            })
        })
        .textureAll('tfc:block/metal/block/copper');
})
