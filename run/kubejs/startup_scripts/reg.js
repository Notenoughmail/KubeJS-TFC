
StartupEvents.registry('tfc:climate_model', e => {
    e.create('model')
        .averageTemperature((m, l, p) => 115)
        .instantaneousTemperature((m, l, p, c, d) => 115)
        .averageRainfall((m, l, p) => 0)
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

const FluidHelpers = Java.loadClass('net.dries007.tfc.common.fluids.FluidHelpers')

StartupEvents.registry('tfc:item_stack_modifiers', e => {
    e.create('modifier')
        .applicator((s, c) => 'minecraft:air');
    e.create('copy_fluid')
        .applicatorWithInput((s, i, c) => {
            console.warn(s);
            console.warn(i);
            if (i.item.hasTag('tfc:fluid_item_ingredient_empty_containers')) {
                let fluid = FluidHelpers.getContainedFluid(i);
                console.warn(fluid)
                s.set('tfc:fluid', { content: Fluid.of('minecraft:water', 60) });
            }
            return s;
        })
})

StartupEvents.registry('tfc:glass_operation', e => {
    e.create('operation')
        .workingTemperature(-1)
        .items('kubejs:glassworking', 'kubejs:glassworking_tool');
})