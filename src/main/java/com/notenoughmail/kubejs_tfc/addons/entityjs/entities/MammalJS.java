package com.notenoughmail.kubejs_tfc.addons.entityjs.entities;

import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.MammalJSBuilder;
import net.dries007.tfc.common.entities.livestock.Mammal;
import net.dries007.tfc.common.entities.livestock.TFCAnimal;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MammalJS extends Mammal implements IAnimatableJS {

    private final AnimatableInstanceCache animationCache;
    private final MammalJSBuilder builder;

    public MammalJS(EntityType<? extends TFCAnimal> animal, Level level, MammalJSBuilder builder) {
        super(animal, level, builder.sounds, builder.config.build());
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
}
