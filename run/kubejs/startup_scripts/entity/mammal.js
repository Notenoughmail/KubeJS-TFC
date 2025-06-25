StartupEvents.registry('entity_type', e => {
    e.create('mammal_test', 'tfc:mammal')
        .withFaunaDefinition('on_ground', 'world_surface_wg');
})
