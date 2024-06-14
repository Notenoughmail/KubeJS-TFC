package com.notenoughmail.kubejs_tfc.addons.entityjs.builders;

import com.notenoughmail.kubejs_tfc.addons.entityjs.entities.DairyAnimalJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class DairyAnimalJSBuilder extends ProducingMammalBuilder<DairyAnimalJS> {

    public DairyAnimalJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public EntityType.EntityFactory<DairyAnimalJS> factory() {
        config.producingMammal();
        return (type, level) -> new DairyAnimalJS(type, level, this);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return DairyAnimalJS.createMobAttributes();
    }
}
