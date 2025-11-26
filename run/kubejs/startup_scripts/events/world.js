TFCEvents.defaultWorldSettings(e => {

    e.finiteContinents();
    e.flatBedrock();
    e.setTemperatureScale(10000);
    e.setRainfallScale(10000);

    e.addRock('test', {
        raw: 'minecraft:stone',
        hardened: 'minecraft:deepslate',
        gravel: 'minecraft:gravel',
        cobble: 'minecraft:cobblestone',
        sand: 'minecraft:sand',
        sandstone: 'minecraft:sandstone',
        mafic: true
    }, false);
})
