package com.notenoughmail.kubejs_tfc.addons.entityjs.builders;

import com.notenoughmail.kubejs_tfc.addons.entityjs.AnimalConfigBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.entities.livestock.ProducingMammal;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class ProducingMammalBuilder<T extends ProducingMammal & IAnimatableJS> extends TFCAnimalBuilder<T>{

    public final transient AnimalConfigBuilder.ProducingMammal config;
    @Nullable
    public transient MutableComponent productMessage;

    public ProducingMammalBuilder(ResourceLocation i) {
        super(i);
        config = new AnimalConfigBuilder.ProducingMammal(configName());
    }

    @Info(value = "Sets the message that is shown when the mammal has a product")
    public ProducingMammalBuilder<T> productMessage(MutableComponent message) {
        productMessage = message;
        return this;
    }

    public ProducingMammalBuilder<T> config(Consumer<AnimalConfigBuilder.ProducingMammal> config) {
        config.accept(this.config);
        return this;
    }
}
