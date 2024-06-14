package com.notenoughmail.kubejs_tfc.addons.entityjs.builders;

import com.notenoughmail.kubejs_tfc.addons.entityjs.entities.MammalJS;
import com.notenoughmail.kubejs_tfc.addons.entityjs.AnimalConfigBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Consumer;

public class MammalJSBuilder extends TFCAnimalBuilder<MammalJS> {

    public transient AnimalConfigBuilder.Mammal config;

    public MammalJSBuilder(ResourceLocation i) {
        super(i);
        config = new AnimalConfigBuilder.Mammal(configName());
    }

    @Info(value = "Allows for setting the default values of the mammal's config")
    public MammalJSBuilder configs(Consumer<AnimalConfigBuilder.Mammal> configBuilder) {
        configBuilder.accept(config);
        return this;
    }

    @Override
    public EntityType.EntityFactory<MammalJS> factory() {
        config.mammal();
        return (type, level) -> new MammalJS(type, level, this);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return MammalJS.createMobAttributes();
    }
}
