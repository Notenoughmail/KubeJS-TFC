ServerEvents.tags('block', e => {
    e.add('tfc:any_spreading_bush', [
        'kubejs:plant/goldenleaf_bush'
    ]);
    e.add('minecraft:replaceable', [
        'kubejs:spring'
    ]);
    e.add('tfc:can_collapse', [
        'minecraft:coarse_dirt'
    ]);
    e.add('tfc:can_landslide', [
        'minecraft:stone'
    ]);
})

ServerEvents.tags('item', e => {
    e.add('tfc:powders', 'minecraft:dirt');
})

ServerEvents.tags('fluid', e => {
    e.add('tfc:usable_in_pot', [
        'minecraft:lava'
    ]);
    e.add('tfc:usable_in_barrel', [
        'minecraft:lava',
        'minecraft:flowing_lava'
    ]);
    e.add('tfc:usable_in_ingot_mold', [
        'minecraft:lava'
    ]);
})

ServerEvents.tags('worldgen/biome', e => {
    e.add('tfc:land', /tfc:.+/);
    e.remove('tfc:land', '#tfc:is_ocean');
    e.remove('tfc:land', '#tfc:is_lake');
    e.remove('tfc:land', '#tfc:is_river');

    e.add('minecraft:has_structure/igloo', [
        '#minecraft:is_nether',
        /tfc:.+/
    ]);
})

ServerEvents.tags('worldgen/configured_feature', e => {
    e.add('kubejs:example_forest_entries', [
        'kubejs_tfc:willow_replica',
        'kubejs_tfc:stacked_tree'
    ]);
})

ServerEvents.tags('worldgen/placed_feature', e => {
    e.add('tfc:in_biome/veins', [
        'kubejs_tfc:geode',
        'kubejs_tfc:cluster_vein',
        'kubejs_tfc:pipe_vein',
        'kubejs_tfc:disc_vein',
        'kubejs_tfc:big_cluster'
    ]);
    e.add('tfc:feature/boulders', [
        'kubejs_tfc:boulder',
        'kubejs_tfc:baby_boulder'
    ]);
    e.add('beneath:everywhere_but_basalt_deltas', [
        'kubejs_tfc:nether_boulder',
        'kubejs:nether_boulder'
    ])
    e.add('tfc:in_biome/underground_decoration', [
        'kubejs_tfc:example_thin_spike'
    ]);
    e.add('tfc:feature/crops', [
        'kubejs:dirt_patch'
    ]);
    e.remove('tfc:feature/crops', 'tfc:plant/wild_crop/barley_patch');
})
