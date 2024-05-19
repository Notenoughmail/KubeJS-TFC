package com.notenoughmail.kubejs_tfc.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.capabilities.glass.GlassOperation;
import net.dries007.tfc.common.capabilities.glass.IGlassworkingTool;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GlassworkingToolItemBuilder extends ToolItemBuilder {

    public transient GlassOperation op;

    public GlassworkingToolItemBuilder(ResourceLocation i) {
        super(i);
        op = GlassOperation.SAW;
    }

    @Info(value = "Sets the glassworking operation type this item is capable of doing")
    public GlassworkingToolItemBuilder operation(GlassOperation operation) {
        op = operation;
        return this;
    }

    @Override
    public Item createObject() {
        return new GlassWorkingToolJS(toolTier, attackDamageBaseline, speedBaseline, mineableBlocks, createItemProperties(), op) {
            private boolean modified = false;

            {
                defaultModifiers = ArrayListMultimap.create(defaultModifiers);
            }

            @Override
            public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
                if (!modified) {
                    modified = true;
                    attributes.forEach((r, m) -> defaultModifiers.put(RegistryInfo.ATTRIBUTE.getValue(r), m));
                }
                return super.getDefaultAttributeModifiers(equipmentSlot);
            }
        };
    }

    public static class GlassWorkingToolJS extends ToolItem implements IGlassworkingTool {

        private final GlassOperation operation;

        public GlassWorkingToolJS(Tier tier, float attackDamage, float attackSpeed, TagKey<Block> mineableBlocks, Properties properties, GlassOperation op) {
            super(tier, attackDamage, attackSpeed, mineableBlocks, properties);
            operation = op;
        }

        @Override
        public GlassOperation getOperation() {
            return operation;
        }

        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            addToolTooltip(pTooltipComponents);
        }
    }
}
