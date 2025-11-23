package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.color.KubeColor;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.WindmillBladeItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

@ReturnsSelf
public class WindmillBladeItemBuilder extends ItemBuilder {

    static final ResourceLocation DEFAULT_TEXTURE = Helpers.identifier("textures/entity/misc/windmill_blade.png");

    public transient ResourceLocation texture = DEFAULT_TEXTURE;
    @Nullable
    public transient KubeColor color;

    public WindmillBladeItemBuilder(ResourceLocation id) {
        super(id);
        Assistant.singleTag(this, TFCTags.Items.WINDMILL_BLADES);
        BuilderRefs.windmillBlades.add(this);
    }

    @Info("Sets the color to use")
    public WindmillBladeItemBuilder bladeColor(@Nullable KubeColor color) {
        this.color = color;
        return this;
    }

    @Info("Sets the texture used for the blade")
    public WindmillBladeItemBuilder bladeTexture(ResourceLocation texture) {
        this.texture = texture.withPath(p -> "textures/" + p + ".png");
        return this;
    }

    @Override
    public Item createObject() {
        return new WindmillBladeItem(createItemProperties());
    }
}
