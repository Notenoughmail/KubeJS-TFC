package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.items.GlassworkingItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@ReturnsSelf
@SuppressWarnings("unused")
public class GlassworkingItemBuilder extends ItemBuilder {

    public transient Holder<GlassOperation> operation;

    public GlassworkingItemBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("Sets the glassworking operation type this item is capable of doing")
    public GlassworkingItemBuilder operation(Holder<GlassOperation> operation) {
        this.operation = operation;
        return this;
    }

    @Override
    public Item createObject() {
        return new GlassworkingItem(createItemProperties(), operation);
    }
}
