
ClientEvents.highPriorityAssets(e => {
    e.addModel('block', 'kubejs:test_jar', m => {
        m.parent('tfc:block/jar');
        m.textures({
            "1": "tfc:block/jar/banana",
            "2": "tfc:block/jar_no_lid"
        });
    });

    e.addModel('block', 'kubejs:topped_jar', m => {
        m.parent('tfc:block/jar');
        m.texture("1", "tfc:block/jar/banana");
    });
})