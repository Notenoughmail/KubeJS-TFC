StartupEvents.registry('block', e => {
    e.create('inventory')
        .blockEntity(be => {
            be.attach('inv', 'tfc:inventory', [], {
                width: 9,
                height: 1,
                sizeFilter: s => s.isSmallerThan('normal')
            });
            be.rightClickOpensInventory('inv');
        })
        .texture('minecraft:block/white_wool');
    e.create('heat_consumer')
        .blockEntity(be => {
            be.attach('heat', 'tfc:heat_consumer', [], {
                decayAmount: 0.02
            });
            be.inventory('inv', [], 9, 1);
            be.rightClickOpensInventory('inv');
        })
        .tag('tfc:charcoal_forge_invisible')
        .texture('tfc:block/metal/block/copper');

    e.create('calendar_example')
        .blockEntity(be => {
            be.attach('cal', 'tfc:calendar_tracking', [], {});
        })
        .texture('minecraft:block/gold_block')
        .rightClick(event => {
            event.block.entity.attachments['cal'].set();
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
})


