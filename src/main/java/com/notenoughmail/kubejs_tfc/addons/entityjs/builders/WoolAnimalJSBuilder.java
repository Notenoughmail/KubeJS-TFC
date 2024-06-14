package com.notenoughmail.kubejs_tfc.addons.entityjs.builders;

import com.notenoughmail.kubejs_tfc.addons.entityjs.entities.WoolyAnimalJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class WoolAnimalJSBuilder extends ProducingMammalBuilder<WoolyAnimalJS> {


    public WoolAnimalJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public EntityType.EntityFactory<WoolyAnimalJS> factory() {
        config.producingMammal();
        return (type, level) -> new WoolyAnimalJS(type, level, this);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return WoolyAnimalJS.createMobAttributes();
    }
}
