TFCEvents.data(e => {
	e.entityDamageResistance(
		'tfc:fish',
		{
			piercing: -10000
		},
		'kubejs:fish_pierce'
	);
	e.itemDamageResistance(
		'minecraft:iron_leggings',
		{
			slashing: 100
		}
	);
})
