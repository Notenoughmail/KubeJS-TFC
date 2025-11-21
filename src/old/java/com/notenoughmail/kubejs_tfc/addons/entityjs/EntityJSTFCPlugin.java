package com.notenoughmail.kubejs_tfc.addons.entityjs;

import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.DairyAnimalJSBuilder;
import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.MammalJSBuilder;
import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.OviparousAnimalJSBuilder;
import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.WoolAnimalJSBuilder;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;

/**
 * TODO: [Future]
 * WildAnimal
 * Prey
 * Predator
 * WingedPrey
 * Pest
 * TamableMammal
 * RammingPrey
 * PackPredator
 * AmphibiousPredator
 * FelinePredator
 * Jellyfish
 * TFCHorse
 * TFCMule
 * TFCDonkey
 */
public class EntityJSTFCPlugin extends KubeJSPlugin {

    @Override
    public void init() {
        EventHandlers.init();

        RegistryInfo.ENTITY_TYPE.addType("tfc:mammal", MammalJSBuilder.class, MammalJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("tfc:oviparous", OviparousAnimalJSBuilder.class, OviparousAnimalJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("tfc:wooly_animal", WoolAnimalJSBuilder.class, WoolAnimalJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("tfc:dairy_animal", DairyAnimalJSBuilder.class, DairyAnimalJSBuilder::new);
    }
}
