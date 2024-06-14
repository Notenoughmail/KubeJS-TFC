package com.notenoughmail.kubejs_tfc.addons.entityjs.entities;

import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.OviparousAnimalJSBuilder;
import net.dries007.tfc.common.entities.livestock.OviparousAnimal;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class OviparousAnimalJS extends OviparousAnimal implements IAnimatableJS {

    private final OviparousAnimalJSBuilder builder;
    private final AnimatableInstanceCache animationCache;

    public OviparousAnimalJS(EntityType<? extends OviparousAnimal> type, Level level, OviparousAnimalJSBuilder builder) {
        super(type, level, builder.sounds, builder.config.oviparous(), builder.crows);
        this.builder = builder;
        animationCache = GeckoLibUtil.createInstanceCache(this);
    }

    @Override
    public TagKey<Item> getFoodTag() {
        return builder.food;
    }

    @Override
    public BaseLivingEntityBuilder<?> getBuilder() {
        return builder;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animationCache;
    }

    @Override
    public MutableComponent getProductReadyName() {
        return builder.productReadyMessage == null ? super.getProductReadyName() : builder.productReadyMessage;
    }
}
