package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.HammerItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@ReturnsSelf
@SuppressWarnings("unused")
public class HammerItemBuilder extends HandheldItemBuilder {

    public transient ResourceLocation tripHammerTexture;

    public HammerItemBuilder(ResourceLocation i) {
        super(i, 3, -2.4f);
        Assistant.singleTag(this, TFCTags.Items.TOOLS_HAMMER);
    }

    @Info("Sets the texture to use when this hammer is in a trip hammer, also marks it as being allowed in a trip hammer")
    public HammerItemBuilder tripHammerTexture(ResourceLocation texture) {
        Assistant.singleTag(this, TFCTags.Items.TRIP_HAMMERS);
        tripHammerTexture = texture;
        BuilderRefs.hammers.add(this);
        return this;
    }

    @Override
    public Item createObject() {
        Assistant.toolItemAttributes(this);
        return new HammerItem(toolTier, createItemProperties());
    }
}
