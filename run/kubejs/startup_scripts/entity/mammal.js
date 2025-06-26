
StartupEvents.registry('entity_type', e => {
    e.create('mammal_test', 'tfc:mammal')
        .withFaunaDefinition('on_ground', 'world_surface_wg')
        .configs(c => {
            c.animalName('animal.coward.coward');
            c.daysToAdult(86);
            c.uses(13);
            c.maxFamiliarity(0.6);
            c.eatsRottenFood(true);
            c.childCount(1);
            c.gestationDays(19);
        });
    e.create('animal_test', 'entityjs:animal')
        .modelResource(a => 'kubejs:geo/entity/mammal_test/male.geo.json');
        .textureResource(a => 'kubejs:item/example_item.png')

})
