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

    e.create('calendar_example')
        .blockEntity(be => {
            be.attach('tfc:calendar', {
                defaultDuration: 500
            })
            be.serverTick(be => {
                let cal = be.attachments[0];
                if (cal.calendarTick != -1 && cal.hasDurationElapsed()) {
                    be.level.playSound(null, be.x, be.y, be.z, 'minecraft:block.anvil.place', 'blocks', 1, 1);
                    cal.reset();
                }
            })
        })
        .rightClick(event => {
            event.block.entity.attachments[0].startTiming();
        });
})
