TFCEvents.data(e => {
    /*
    e.climateRange(c => {
        c.maxHydration(120);
    }, 'kubejs:crop');
    e.climateRange(c => {
        c.minHydration(230)
    }, 'kubejs:spreading_bush');
    e.climateRange(c => {
        c.maxTemperature(-3);
    }, 'kubejs:flooded_crop');
    e.climateRange(c => {
        c.minTemperature(7);
    }, 'kubejs:pickable_crop');
    e.climateRange(c => {
        c.minHydration(250);
        c.hydrationWiggle(50);
    }, 'kubejs:spreading_crop');
    e.climateRange(c => {
        c.maxHydration(50);
    }, 'kubejs:double_crop');
    e.climateRange(c => {
        c.maxHydration(200);
        c.maxTemperature(12);
    }, 'kubejs:double_crop_stick');
    e.climateRange(c => {
        c.minTemperature(5);
        c.temperatureWiggle(0.4);
    }, 'kubejs:stationary_bush');
    */

    e.climateRange(c => {}, 'kubejs:crop_2');
    e.climateRange(c => {}, 'kubejs:crop_4');
    e.climateRange(c => {}, 'kubejs:crop_8');
})
