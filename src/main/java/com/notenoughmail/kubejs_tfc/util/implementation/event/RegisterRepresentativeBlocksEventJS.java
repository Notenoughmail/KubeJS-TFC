package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.items.PropickItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class RegisterRepresentativeBlocksEventJS extends EventJS {

    public void registerRepresentative(ResourceLocation representative, ResourceLocation... blocks) {
        final Block rep = RegistryInfo.BLOCK.getValue(representative);
        final List<Block> list = new ArrayList<>();
        for (ResourceLocation block : blocks) {
            list.add(RegistryInfo.BLOCK.getValue(block));
        }
        PropickItem.registerRepresentative(rep, list.toArray(new Block[0]));
    }
}
