package com.notenoughmail.kubejs_tfc.menu;

import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.blockentities.AnvilBlockEntity;
import net.dries007.tfc.common.container.AnvilContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class AnvilMenuBuilder extends BuilderBase<MenuType<AnvilContainer>> {

    public static AnvilContainer create(AnvilBlockEntity anvil, Inventory playerInv, int windowId) {
        final AnvilContainer container = new AnvilContainer(windowId, anvil) {
            @Override
            public MenuType<?> getType() { return RegistryUtils.getAnvilMenu().get(); }
        };
        return container.init(playerInv, 41);
    }

    public AnvilMenuBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public RegistryInfo<?> getRegistryType() {
        return RegistryInfo.MENU;
    }

    @Override
    public MenuType<AnvilContainer> createObject() {
        return IForgeMenuType.create((windowId, inv, data) -> {
            final Level level = inv.player.level();
            final BlockPos pos = data.readBlockPos();
            final AnvilBlockEntity anvil = level.getBlockEntity(pos, RegistryUtils.getAnvil().get()).orElseThrow();
            return create(anvil, inv, windowId);
        });
    }
}
