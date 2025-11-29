package io.github.notenoughmail.kubejstfc.implementation.extensions;

import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.level.levelgen.Heightmap;

@RemapPrefixForJS(KubeJSTFC.MIXIN_PREFIX)
public interface EntityBuilderExtension {

    BaseLivingEntityBuilder<?> kubejs_tfc$tfcSpawnPlacement(SpawnPlacementType placementType, Heightmap.Types heightMap);
}
