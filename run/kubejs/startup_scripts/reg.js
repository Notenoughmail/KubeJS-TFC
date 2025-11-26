
StartupEvents.registry('tfc:climate_model', e => {
    e.create('model')
        .fog((m, l, p) => 1);
})

StartupEvents.registry('tfc:food_trait', e => {
    e.create('trait')
        .decayModifier(0.5)
        .tooltipText('Tasteful');
})

StartupEvents.registry('tfc:chisel_mode', e => {
    e.create('mode')
        .priority(0);
})

StartupEvents.registry('tfc:item_stack_modifiers', e => {
    e.create('modifier')
        .applicator((s, i, c) => 'minecraft:air');
})

StartupEvents.registry('tfc:glass_operation', e => {
    e.create('operation')
        .items('kubejs:glassworking', 'kubejs:glassworking_tool');
})