StartupEvents.registry('block', e => {
    e.create('log', 'tfc:log')
        .stripped(s => {
            s.useFullBlockForItemModel();
        });
    e.create('log_no_stripped', 'tfc:log')
        .stripped(null);
})
