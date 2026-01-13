TFCEvents.collapse(event => {
    event.getSecondaryPositions().forEach(pos => {
        event.getLevel().playSound(null, pos, 'minecraft:block.wood.break', 'blocks', 1, 1)
    })
})