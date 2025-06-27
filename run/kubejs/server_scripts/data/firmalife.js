// requires firmalife

TFCEvents.data(e => {
    e.firmalifeGreenhouseType(
        '#minecraft:cobblestone',
        7942
    );
    e.firmalifeGreenhouseType(
        '#minecraft:flowers',
        1,
        'kubejs:flowers'
    );

    e.firmalifePlantable(
        'minecraft:beetroot_seeds',
        'trellis',
        2,
        null,
        0.6,
        'minecraft:beetroot_seeds',
        'minecraft:beetroot',
        'nitrogen',
        [
            'minecraft:block/dirt',
            'minecraft:block/oak_leaves',
            'minecraft:item/diamond_sword',
            'minecraft:block/cobblestone'
        ],
        null
    );
    e.firmalifePlantable(
        'minecraft:carrot',
        'quad',
        900,
        1,
        0.2,
        'minecraft:carrot',
        'minecraft:carrot',
        'potassium',
        [
            'minecraft:block/deepslate',
            'minecraft:item/iron_sword'
        ],
        null,
        'kubejs:plantable'
    );
})
