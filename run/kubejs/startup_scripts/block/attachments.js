StartupEvents.registry('block', e => {
    e.create('inventory')
        .blockEntity(be => {
            be.attach('inv', 'tfc:inventory', [], {
                width: 9,
                height: 1,
                size: s => s.isSmallerThan('normal')
            });
            be.rightClickOpensInventory('inv');
        })
        .texture('tfc:block/mud/silt');
    e.create('heat_consumer')
        .blockEntity(be => {
            be.attach('heat', 'tfc:heat_consumer', [], {
                decayAmount: 6
            });
            be.inventory('inv', [], 9, 1);
            be.rightClickOpensInventory('inv');
        })
        .texture('tfc:block/metal/block/copper');

    e.create('calendar_example')
        .blockEntity(be => {
            be.attach('cal', 'tfc:calendar_tracking', [], {});
        })
        .texture('minecraft:block/gold_block')
        .rightClick(event => {
            event.block.entity.attachments['inv'].set();
        });

    e.create('sealable_example')
        .blockEntity(be => {
            be.attach('inv', 'tfc:sealable_inventory', [], {
                width: 9,
                height: 1,
                trait: 'kubejs:trait'
            });
        })
        .rightClick(event => {
            let { player } = event;
            let be = event.block.entity;
            if (!player.shiftKeyDown) {
                player.openInventoryGUI(be.attachments['inv'], event.block.blockState.block.name);
            } else {
                be.attachments['inv'].toggleSeal();
            }
        })
        .texture('minecraft:block/iron_block');
    e.create('preserve_example')
        .blockEntity(be => {
            be.attach('inv', 'tfc:sealable_inventory', [], {
                width: 9,
                height: 1,
                requiresSeal: false,
                canSeal: false,
                trait: 'kubejs:trait'
            });
            be.rightClickOpensInventory('inv');
        })
        .texture('tfc:block/metal/block/wrought_iron');
})


