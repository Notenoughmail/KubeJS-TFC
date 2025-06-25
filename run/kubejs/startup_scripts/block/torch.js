StartupEvents.registry('block', e => {
    e.create('torch', 'tfc:torch')
        .decayLength(100);
})
