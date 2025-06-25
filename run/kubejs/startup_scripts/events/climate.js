TFCEvents.registerClimateModel(event => {
    event.register('kubejs:hell', builder => {
        var fogNoiseIndex = builder.newNoise(s => s.octaves(3).spread(0.1).abs());
        var temperatureVarianceIndex = builder.newNoise(s => s.scaled(-360, 500).spread(0.6));
        builder.setAverageTemperatureCalculation((level, pos) => 1000);
        builder.currentTemperatureCalculation = (level, pos, calendarTicks, daysInMonth) => {
            var variance = builder.noise(temperatureVarianceIndex).noise(pos.x, pos.z);
            return 1000 + variance;
        };
        builder.setAirFog((level, pos, calendarTicks) => builder.noise(fogNoiseIndex).noise(pos.x, pos.z));
        builder.windVector = builder.tfcWind;
    });
    event.register('kubejs:potluck', builder => {
        builder.windVector = (level, pos, ticks) => builder.vector(pos.x, pos.z);
        builder.onWorldLoad = level => console.warn('The world has loaded, and boy is it dire');
        builder.averageRainfallCalculation = (level, pos) => 5000;
        builder.averageTemperatureCalculation = (level, pos) => -60;
        builder.currentTemperatureCalculation = (level, pos, ticks, dim) => -80
    })
})
