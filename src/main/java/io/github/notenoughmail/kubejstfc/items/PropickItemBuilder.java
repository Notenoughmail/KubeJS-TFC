package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.PropickItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@ReturnsSelf
public class PropickItemBuilder extends HandheldItemBuilder {

    public transient int level;

    public PropickItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
        Assistant.singleTag(this, TFCTags.Items.TOOLS_PROPICK);
    }

    @Info("Sets the tool level of this propick, determines the chance of having a false negative")
    public PropickItemBuilder level(int level) {
        this.level = level;
        return this;
    }

    @Override
    public Item createObject() {
        Assistant.toolItemAttributes(this);
        return new PropickItem(Assistant.levelTier(toolTier, level), createItemProperties());
    }
}
