package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.component.glass.IGlassworkingTool;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class GlassworkingToolItemBuilder extends ToolItemBuilder {

    public transient Holder<GlassOperation> op;

    public GlassworkingToolItemBuilder(ResourceLocation i) {
        super(i);
        op = GlassOperation.SAW;
    }

    @Info("Sets the glassworking operation type this item is capable of doing")
    public GlassworkingToolItemBuilder operation(Holder<GlassOperation> operation) {
        op = operation;
        return this;
    }

    @Override
    public Item createObject() {
        Assistant.toolItemAttributes(this);
        return new KubeGlassWorkingTool(toolTier, mineableBlocks, createItemProperties(), op);
    }

    public static class KubeGlassWorkingTool extends ToolItem implements IGlassworkingTool {

        private final Holder<GlassOperation> operation;

        public KubeGlassWorkingTool(Tier tier, TagKey<Block> mineableBlocks, Properties properties, Holder<GlassOperation> op) {
            super(tier, mineableBlocks, properties);
            operation = op;
        }

        @Override
        public GlassOperation getOperation() {
            return operation.value();
        }
    }
}
