TFCEvents.defaultWorldSettings(e => {

    e.finiteContinents();
    e.flatBedrock();
    e.setTemperatureScale(10000);
    e.setRainfallScale(10000);

    e.addRock('test', 'kubejs:vanilla', true);
})
