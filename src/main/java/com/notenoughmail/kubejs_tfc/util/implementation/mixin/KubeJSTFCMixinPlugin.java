package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class KubeJSTFCMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!isModLoaded("firmalife")) {
            return !"com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS".equals(targetClassName) || !"com.notenoughmail.kubejs_tfc.util.implementation.mixin.extensions.FirmaLifeISPMixin".equals(mixinClassName);
        }
        if (!isModLoaded("tfcchannelcasting")) {
            return !"com.notenoughmail.kubejs_tfc.item.MoldItemBuilder".equals(targetClassName) || !"com.notenoughmail.kubejs_tfc.util.implementation.mixin.extensions.TFCCCMoldItemBuilderExtensions".equals(mixinClassName);
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    private boolean isModLoaded(String id) {
        if (ModList.get() == null) {
            return LoadingModList.get().getModFileById(id) != null;
        } else {
            return ModList.get().isLoaded(id);
        }
    }
}
