StartupEvents.registry('block', e => {
    e.create('axle', 'tfc:axle')
        .textureAll('tfc:block/metal/smooth/gold')
        .axleTexture('tfc:block/metal/smooth/gold')
        .waterWheel(w => {})
        .gearBox(b => {
            b.textureAll('tfc:block/metal/smooth/wrought_iron');
        })
        .clutch(c => {
            c.textureAll('tfc:metal/smooth/blue_steel');
        })
        .bladedAxle(a => {
            a.textureAll('tfc:block/metal/smooth/rose_gold');
        });
    e.create('encased_axle', 'tfc:encased_axle')
        .textureAll('tfc:block/metal/smooth/bismuth');
})
