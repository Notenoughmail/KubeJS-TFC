TFCEvents.data(e => {
    e.climateRange({
        maxHydration: 120
    }, 'kubejs:crop');
    e.climateRange({
        minHydration: 230
    }, 'kubejs:spreading_bush');
    e.climateRange({
        maxTemperature: -3
    }, 'kubejs:flooded_crop');
    e.climateRange({
        minTemperature: 7
    }, 'kubejs:pickable_crop');
    e.climateRange({
        minHydration: 250,
        hydrationWiggleRange: 50
    }, 'kubejs:spreading_crop');
    e.climateRange({
        maxHydration: 50
    }, 'kubejs:double_crop');
    e.climateRange({
        maxHydration: 200,
        maxTemperature: 12
    }, 'kubejs:double_crop_stick');
    e.climateRange({
        minTemperature: 5,
        temperatureWiggleRange: 0.4
    }, 'kubejs:stationary_bush');

    e.climateRange(c => {}, 'kubejs:crop_2');
    e.climateRange(c => {}, 'kubejs:crop_4');
    e.climateRange(c => {}, 'kubejs:crop_8');
})
