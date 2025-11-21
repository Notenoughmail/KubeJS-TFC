package com.notenoughmail.kubejs_tfc.addons.entityjs.builders;

import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.entities.livestock.TFCAnimal;
import net.liopyu.entityjs.builders.living.entityjs.AnimalEntityBuilder;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class TFCAnimalBuilder<T extends TFCAnimal & IAnimatableJS> extends AnimalEntityBuilder<T> {


    public transient TagKey<Item> food;
    public transient TFCSounds.EntitySound sounds;

    public TFCAnimalBuilder(ResourceLocation i) {
        super(i);
        food = TFCTags.Items.PIG_FOOD;
        sounds = TFCSounds.PIG;
        textureResource(mammal -> mammal.getBuilder().newID("textures/entity/", "/" + (mammal.getGender().toBool() ? "" : "fe") + "male.png"));
        modelResource(mammal -> mammal.getBuilder().newID("geo/entity/", "/" + (mammal.getGender().toBool() ? "" : "fe") + "male.geo.json"));
        scaleModelForRender(ctx -> {
            final float scale = ctx.entity.getAgeScale();
            ctx.poseStack.scale(scale, scale, scale);
        });
    }

    @HideFromJS
    public String configName() {
        return id.getNamespace() + "." + UtilsJS.snakeCaseToCamelCase(id.getPath());
    }

    @Info(value = "Sets the item tag that the mammal will eat")
    public TFCAnimalBuilder<T> foodTag(ResourceLocation tag) {
        food = TagKey.create(Registries.ITEM, tag);
        return this;
    }

    @Info(value = "Sets the sounds the mammal makes", params = {
            @Param(name = "ambient", value = "The animal's ambient sound"),
            @Param(name = "death", value = "The animal's death sound"),
            @Param(name = "hurt", value = "The animal's hurt sound"),
            @Param(name = "step", value = "The animal's step sound"),
            @Param(name = "attack", value = "The animal's attack sound, may be null"),
            @Param(name = "sleep", value = "The animal's sleep sound, may be null")
    })
    public TFCAnimalBuilder<T> sounds(ResourceLocation ambient, ResourceLocation death, ResourceLocation hurt, ResourceLocation step, @Nullable ResourceLocation attack, @Nullable ResourceLocation sleep) {
        sounds = new TFCSounds.EntitySound(
                () -> RegistryInfo.SOUND_EVENT.getValue(ambient),
                () -> RegistryInfo.SOUND_EVENT.getValue(death),
                () -> RegistryInfo.SOUND_EVENT.getValue(hurt),
                () -> RegistryInfo.SOUND_EVENT.getValue(step),
                attack == null ? Optional.empty() : Optional.of(() -> RegistryInfo.SOUND_EVENT.getValue(attack)),
                sleep == null ? Optional.empty() : Optional.of(() -> RegistryInfo.SOUND_EVENT.getValue(sleep))
        );
        return this;
    }
}
