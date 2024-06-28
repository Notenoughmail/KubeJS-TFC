package com.notenoughmail.kubejs_tfc.addons.entityjs.builders;

import com.notenoughmail.kubejs_tfc.addons.entityjs.AnimalConfigBuilder;
import com.notenoughmail.kubejs_tfc.addons.entityjs.entities.OviparousAnimalJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.entities.livestock.OviparousAnimal;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OviparousAnimalJSBuilder extends TFCAnimalBuilder<OviparousAnimalJS> {

    public final transient AnimalConfigBuilder.Oviparous config;
    public transient boolean crows;
    @Nullable
    public transient MutableComponent productReadyMessage;

    public OviparousAnimalJSBuilder(ResourceLocation i) {
        super(i);
        config = new AnimalConfigBuilder.Oviparous(configName());
        crows = false;
        productReadyMessage = null;
    }

    @Info(value = "Allows for setting the default values of the animal's config")
    public OviparousAnimalJSBuilder configs(Consumer<AnimalConfigBuilder.Oviparous> configBuilder) {
        configBuilder.accept(config);
        return this;
    }

    @Info(value = "If this animal should crow every so often")
    public OviparousAnimalJSBuilder crows(boolean crows) {
        this.crows = crows;
        return this;
    }

    @Info(value = "Sets the message displayed when the animal has a product ready (by default an egg)")
    public OviparousAnimalJSBuilder productReadyMessage(MutableComponent message) {
        productReadyMessage = message;
        return this;
    }

    @Override
    public EntityType.EntityFactory<OviparousAnimalJS> factory() {
        config.oviparous();
        return (type, level) -> new OviparousAnimalJS(type, level, this);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return OviparousAnimal.createAttributes();
    }
}
