TFCEvents.registerInteractions(e => {
    e.registerBlockPlacement('tfc:powder/diamond', 'minecraft:diamond_block');
    e.register('tfc:metal/sheet/steel', 'air', (stack, ctx) => {
        let { player, clickedPos } = ctx;
        let { offHandItem } = player;
        console.error('Trying to carve steel');
        if (player != null && clickedPos.equals(BlockPos.ZERO) && offHandItem.id('minecraft:dirt')) {
            console.error('carved steel')
            return 'success';
        }
        return 'pass';
    });
})
