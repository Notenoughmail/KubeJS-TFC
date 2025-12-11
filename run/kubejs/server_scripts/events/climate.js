TFCEvents.selectClimateModel(event => {
    if (event.level.dimensionKey.location() == 'minecraft:the_nether') {
        event.model = event.kubeModel('kubejs:model', 20000, false);
    }
})
