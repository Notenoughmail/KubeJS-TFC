TFCEvents.rockSettings(e => {
    e.defineRock('kubejs:test', 'minecraft:light_gray_stained_glass', 'minecraft:dirt', 'minecraft:gravel', 'minecraft:cobblestone', 'minecraft:red_sand', 'minecraft:sandstone', 'kubejs:rock_spike', null, null);
    global.test = e.defineRock('kubejs:test2', 'minecraft:stone', 'minecraft:oak_planks', 'minecraft:spruce_planks', 'minecraft:acacia_planks', 'minecraft:red_sandstone', 'minecraft:wet_sponge', null, null, null);
    global.netherRockSettings = e.defineRock('kubejs:netherrack', 'minecraft:netherrack', 'minecraft:bricks', 'minecraft:gravel', 'minecraft:basalt', 'minecraft:soul_sand', 'minecraft:soul_soil', 'kubejs:rock_spike', null, null);
})

TFCEvents.defaultWorldSettings(e => {
    e.setRainfallScale(4000);
    e.setTemperatureScale(20);
    e.setContinentalness(-3.5);

    e.cleanSlate();
    e.addRockFromId('kubejs:test', 'test_rock', true);
    e.addRock(global.test, 'test_rock_2', false);
    e.defineLayer('test', {
        test_rock: 'bottom'
    });
    e.defineLayer('test2', {
        test_rock_2: 'test',
        test_rock: 'test'
    });
    e.addOceanFloorLayer('test2');
    e.addLandLayer('test');
    e.addVolcanicLayer('test');
    e.addUpliftLayer('test');
})
