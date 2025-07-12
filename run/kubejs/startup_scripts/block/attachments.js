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
        .textureAll('minecraft:block/gold_block')
        .rightClick(event => {
            event.block.entity.attachments[0].startTiming();
        });

    e.create('sealable_example')
        .blockEntity(be => {
            be.attach('tfc:sealable_inventory', {
                width: 9,
                height: 1,
                trait: 'kubejs:sealed'
            });
        })
        .rightClick(event => {
            let { player } = event;
            let be = event.block.entity;
            if (!player.shiftKeyDown) {
                player.openInventoryGUI(be.inventory, event.block.blockState.block.name);
            } else {
                be.inventory.toggleSeal();
            }
        })
        .textureAll('minecraft:block/iron_block');
    e.create('preserve_example')
        .blockEntity(be => {
            be.attach('tfc:sealable_inventory', {
                width: 9,
                height: 1,
                requiresSeal: false,
                canSeal: false,
                trait: 'kubejs:sealed'
            });
            be.rightClickOpensInventory();
        })
        .textureAll('tfc:block/metal/block/wrought_iron');
})
