TFCEvents.selectClimateModel(event => {
    var dim = event.level.dimensionKey.location();
    if (dim == 'minecraft:the_nether') {
        event.model = 'kubejs:hell'
    } else if (dim == 'minecraft:the_end') {
        event.model = 'kubejs:potluck'
    }
})
