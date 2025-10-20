package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.HammerItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class HammerItemBuilder extends HandheldItemBuilder {

    private static final ResourceLocation[] DEFAULT_TAGS = Assistant.single(TFCTags.Items.TOOLS_HAMMER.location());
    private static final ResourceLocation[] TRIP_HAMMER = Assistant.single(TFCTags.Items.TRIP_HAMMERS.location());

    public transient ResourceLocation tripHammerTexture;

    public HammerItemBuilder(ResourceLocation i) {
        super(i, 3, -2.4f);
        tag(DEFAULT_TAGS);
    }

    @Info("Sets the texture to use when this hammer is in a trip hammer, also marks it as being allowed in a trip hammer")
    public HammerItemBuilder tripHammerTexture(ResourceLocation location) {
        tag(TRIP_HAMMER);
        tripHammerTexture = location;
        BuilderRefs.hammers.add(this);
        return this;
    }

    @Override
    public Item createObject() {
        Assistant.toolItemAttributes(this);
        return new HammerItem(toolTier, createItemProperties());
    }
}
