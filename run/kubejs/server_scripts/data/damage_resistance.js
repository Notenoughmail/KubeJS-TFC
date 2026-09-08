TFCEvents.data(e => {
	e.entityDamageResistance(
		{
			entity: 'tfc:fish',
			piercing: -10000
		},
		'kubejs:fish_pierce'
	);
	e.itemDamageResistance(
		{
			ingredient: 'minecraft:iron_leggings',
			slashing: 100
		}
	);
})
