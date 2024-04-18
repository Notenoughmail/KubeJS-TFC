package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.items.PropickItem;
import net.minecraft.world.level.block.Block;

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
    public void registerRepresentative(Block representative, Block... blocks) {
        PropickItem.registerRepresentative(representative, blocks);
    }
}
