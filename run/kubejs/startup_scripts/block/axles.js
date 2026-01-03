StartupEvents.registry('block', e => {
    e.create('axle', 'tfc:axle')
        .axleTexture('tfc:block/metal/smooth/gold')
        .waterWheel(w => {
            w.texture('tfc:block/wood/planks/kapok');
            w.wheelTexture('tfc:kapok');
        })
        .gearBox(b => {
            b.texture('tfc:block/metal/smooth/wrought_iron');
        })
        .clutch(c => {
            c.texture('tfc:block/metal/smooth/blue_steel');
        })
        .bladedAxle(a => {
            a.texture('tfc:block/metal/smooth/rose_gold');
        });
    e.create('encased_axle', 'tfc:encased_axle')
        .texture('tfc:block/metal/smooth/bismuth');
})
