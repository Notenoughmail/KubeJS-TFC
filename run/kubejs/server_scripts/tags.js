ServerEvents.tags('block', e => {
    e.add('tfc:can_collapse', 'minecraft:gold_block');
    e.add('tfc:can_start_collapse', 'minecraft:gold_block');
    e.add('tfc:can_trigger_collapse', 'minecraft:gold_block');
})

ServerEvents.tags('item', e => {
    e.add('tfc:glass_batches', 'minecraft:sand')
})

ServerEvents.tags('fluid', e => {
})
