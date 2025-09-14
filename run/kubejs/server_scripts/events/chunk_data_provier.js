TFCEvents.createChunkDataProvider('nether', event => {

    // Use a LayeredArea for the rocks as noises can be slow when used with the rock rule source
    const randomSource = event.getRandomSource('nether');
    const rockLayer = TFC.misc.uniformLayeredArea(randomSource.nextLong());
    for (let i = 0 ; i < 3 ; i++) {
        rockLayer.zoom(true, randomSource.nextLong()).smooth(randomSource.nextLong());
    }
    for (let i = 0 ; i < 6 ; i++) {
        rockLayer.zoom(true, randomSource.nextLong());
    }
    rockLayer
        .smooth(randomSource.nextLong())
        .zoom(true, randomSource.nextLong())
        .smooth(randomSource.nextLong());

    const rain = TFC.misc.lerpFloatLayer(0, 0, 0, 0);
    const tempLayer = TFC.misc.newOpenSimplex2D(event.worldSeed + 4621678939469)
        .spread(0.2)
        .octaves(3)
        .scaled(70, 90);
    const forestLayer = TFC.misc.newOpenSimplex2D(event.worldSeed + 98713856895664)
        .spread(0.8)
        .terraces(9)
        .affine(6, 12)
        .scaled(6, 18, 0, 1);
    const rockLayerHeightNoise = TFC.misc.newOpenSimplex2D(event.worldSeed + 30121796313692)
        .octaves(6)
        .scaled(12, 34)
        .spread(0.009);

    // Precompute the aquifer heights as constants as this is nether and will not realistically change
    var aquifer = [];
    var i = 0;
    while (i < 16) {
        aquifer.push(0);
        i++;
    }

    event.partial((data, chunk) => {
        var x = chunk.pos.minBlockX;
        var z = chunk.pos.minBlockZ;

        var temp = TFC.misc.lerpFloatLayer(
            tempLayer.noise(x, z),
            tempLayer.noise(x, z + 15),
            tempLayer.noise(x + 15, z),
            tempLayer.noise(x + 15, z + 15)
        );

        data.generatePartial(
            rain,
            temp,
            forestLayer.noise(x, z) * 4, // Kube accepts ordinal numbers for enum constants
            forestLayer.noise(x * 78423 + 869, z),
            forestLayer.noise(x, z * 651349 - 698763)
        );
    });

    event.full((data, chunk) => {
        var heights = [];
        // In the nether this will always return 127, but this is included as a demonstration of
        // using height maps and properly indexing the height values within the array
        for (let x = 0 ; x < 16 ; x++) {
            for (let z = 0 ; z < 16 ; z++) {
                let height = chunk.getHeight('ocean_floor_wg', x, z);
                heights[x + 16 * z] = height;
            }
        }
        data.generateFull(heights, aquifer);
    });

    event.rocks((x, y, z, surfaceY, cache, rockLayers) => {
        let layer = 0;
        let layerHeight = 0;
        let deltaY = surfaceY - y;

        do {
            // A simplified version of what TFC does for its layer depth
            // Of note is the lack of skewing for either the rock layer or the heights
            // And the non-use of the cache
            layerHeight = rockLayerHeightNoise.noise(x >> 5, z >> 5);
            if (deltaY <= layerHeight) {
                break;
            }
            deltaY -= layerHeight;
            layer++;
        } while (deltaY > 0);

        return rockLayers.sampleAtLayer(rockLayer.getAt(x, z), layer);
    });
})