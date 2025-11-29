package io.github.notenoughmail.kubejstfc.util.mixin.entityjs;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import io.github.notenoughmail.kubejstfc.events.startup.KubeFaunaSpawnsEvent;
import io.github.notenoughmail.kubejstfc.implementation.extensions.EntityBuilderExtension;
import io.github.notenoughmail.kubejstfc.util.IfPresent;
import net.liopyu.entityjs.EntityJSMod;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@IfPresent(EntityJSMod.MOD_ID)
@Mixin(BaseLivingEntityBuilder.class)
public abstract class BaseLivingEntityBuilderMixin extends BuilderBase<EntityType<?>> implements EntityBuilderExtension {

    private BaseLivingEntityBuilderMixin(ResourceLocation id) {
        super(id);
    }

    @Shadow
    public abstract BaseLivingEntityBuilder<?> spawnPlacement(SpawnPlacementType placementType, Heightmap.Types heightMap, SpawnPlacements.SpawnPredicate<?> spawnPredicate);

    @Override
    public BaseLivingEntityBuilder<?> kubejs_tfc$tfcSpawnPlacement(SpawnPlacementType placementType, Heightmap.Types heightMap) {
        return spawnPlacement(placementType, heightMap, KubeFaunaSpawnsEvent.make(id));
    }
}
