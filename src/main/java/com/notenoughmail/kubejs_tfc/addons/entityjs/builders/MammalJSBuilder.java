package com.notenoughmail.kubejs_tfc.addons.entityjs.builders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.addons.entityjs.entities.MammalJS;
import com.notenoughmail.kubejs_tfc.config.MammalConfigBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.TFCTags;
import net.liopyu.entityjs.builders.living.entityjs.AnimalEntityBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class MammalJSBuilder extends AnimalEntityBuilder<MammalJS> implements IHaveFaunaDefinition<MammalJS> {

    public transient TagKey<Item> food;
    public transient TFCSounds.EntitySound sounds;
    public transient MammalConfigBuilder config;

    public MammalJSBuilder(ResourceLocation i) {
        super(i);
        food = TFCTags.Items.PIG_FOOD;
        sounds = TFCSounds.PIG;
        config = new MammalConfigBuilder(KubeJSTFC.wrappedServerConfigBuilder, id.getNamespace() + "." + UtilsJS.snakeCaseToCamelCase(id.getPath()));
        textureResource(mammal -> mammal.getBuilder().newID("textures/entity/", "/" + (mammal.getGender().toBool() ? "" : "fe") + "male.png"));
        modelResource(mammal -> mammal.getBuilder().newID("geo/entity/", "/" + (mammal.getGender().toBool() ? "" : "fe") + "male.geo.json"));
        scaleModelForRender(ctx -> {
            final float scale = ctx.entity.getAgeScale();
            ctx.poseStack.scale(scale, scale, scale);
        });
    }

    @Info(value = "Sets the item tag that the mammal will eats")
    public MammalJSBuilder foodTag(ResourceLocation tag) {
        food = TagKey.create(Registries.ITEM, tag);
        return this;
    }

    @Info(value = "Sets the sounds the mammal makes")
    public MammalJSBuilder sounds(ResourceLocation ambient, ResourceLocation death, ResourceLocation hurt, ResourceLocation step, @Nullable ResourceLocation attack, @Nullable ResourceLocation sleep) {
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

    @Info(value = "Allows for setting the default values of the mammal's config")
    public MammalJSBuilder configs(Consumer<MammalConfigBuilder> configBuilder) {
        configBuilder.accept(config);
        return this;
    }

    @Override
    public EntityType.EntityFactory<MammalJS> factory() {
        config.build();
        return (type, level) -> new MammalJS(type, level, this);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return MammalJS.createMobAttributes();
    }
}
