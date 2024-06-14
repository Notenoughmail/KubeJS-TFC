package com.notenoughmail.kubejs_tfc.addons.entityjs.entities;

import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.WoolAnimalJSBuilder;
import net.dries007.tfc.common.entities.livestock.WoolyAnimal;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WoolyAnimalJS extends WoolyAnimal implements IAnimatableJS {

    private final WoolAnimalJSBuilder builder;
    private final AnimatableInstanceCache animationCache;

    public WoolyAnimalJS(EntityType<? extends WoolyAnimal> animal, Level level, WoolAnimalJSBuilder builder) {
        super(animal, level, builder.sounds, builder.config.producingMammal());
        this.builder = builder;
        animationCache = GeckoLibUtil.createInstanceCache(this);
    }

    @Override
    public TagKey<Item> getFoodTag() {
        return builder.food;
    }

    @Override
    public MutableComponent getProductReadyName() {
        return builder.productMessage == null ? super.getProductReadyName() : builder.productMessage;
    }

    @Override
    public BaseLivingEntityBuilder<?> getBuilder() {
        return builder;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animationCache;
    }
}
