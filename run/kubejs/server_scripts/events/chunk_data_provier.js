TFCEvents.createChunkDataProvider('minecraft:the_nether', e => {
    var rain = TFC.misc.lerpFloatLayer(0, 0, 0, 0);
    let tLayer = TFC.misc.newOpenSimplex2D(8941321561)
        .spread(0.2)
        .octaves(3)
        .scaled(20, 70);
    let fLayer = TFC.misc.newOpenSimplex2D(51481356451222)
        .spread(0.8)
        .scaled(0, 1);

    var heights = [];
    var i = 0;
    while (i < 16 * 16) {
        heights.push(127);
        i++;
    }
    var aquifer = [];
    i = 0;
    while (i < 4 * 4) {
        aquifer.push(0);
        i++;
    }

    e.rocks((x, y, z, surfaceY, cache, rockLayers) => rockLayers.sampleAtLayer(0, 0));
    e.partial((data, access) => {
        var x = access.pos.minBlockX;
        var z = access.pos.maxBlockZ;
        var temp = TFC.misc.lerpFloatLayer(
            tLayer.noise(x, z),
            tLayer.noise(x, z + 15),
            tLayer.noise(x + 15, z),
            tLayer.noise(x + 15, z + 15)
        );
        data.generatePartial(
            rain,
            temp,
            fLayer.noise(x, z) * 4,
            fLayer.noise(x + 54843, z * 983),
            fLayer.noise(z * 156, x * 9783)
        );
    });
    e.full((data, access) => data.generateFull(heights, aquifer));
})
