// requires beneath

TFCEvents.data(e => {
    e.beneathLostPage(
        'minecraft:cobblestone',
        'minecraft:stone',
        [
            2,
            5,
            7
        ],
        [
            8,
            1
        ],
        [ 'levitation' ],
        null
    );
    e.beneathLostPage(
        'minecraft:dirt',
        'minecraft:coarse_dirt',
        [ 2 ],
        [ 2 ],
        [ 'none' ],
        'test.test'
    );

    e.beneathNetherFertilizer(
        'minecraft:dirt',
        0.5,
        0.5,
        0.5,
        null,
        0.5
    )
})
