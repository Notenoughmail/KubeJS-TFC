package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.common.items.PropickItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

@Info(value = """
        When prospecting, TFC uses representative blocks to group similar
        blocks together to get a better count of what's nearby
        
        This allows new representatives to be registered
        """)
public class RegisterRepresentativeBlocksEventJS extends EventJS {

    @Info(value = "Registers a new representative block", params = {
            @Param(name = "representative", value = "The registry name of a block, the representative"),
            @Param(name = "blocks", value = "A list of block registry names, the blocks to be represented")
    })
    @Generics(value = ResourceLocation.class)
    public void registerRepresentative(ResourceLocation representative, List<ResourceLocation> blocks) {
        final Block rep = RegistryInfo.BLOCK.getValue(representative);
        final Block[] array = new Block[blocks.size()];
        for (int i = 0 ; i < blocks.size() ; i++) {
            array[i] = RegistryInfo.BLOCK.getValue(UtilsJS.getMCID(null, blocks.get(i)));
        }
        PropickItem.registerRepresentative(rep, array);
    }
}
