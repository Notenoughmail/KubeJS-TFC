package io.github.notenoughmail.kubejstfc.items;

import com.google.gson.JsonArray;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.builders.item.FluidCapacityItemBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.MoldItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@ReturnsSelf
public class MoldItemBuilder extends FluidCapacityItemBuilder {

    public transient String @Nullable [] moldTablePattern;
    @Nullable
    public transient Map<String, String> moldTableTextures;

    public MoldItemBuilder(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean mold() {
        return true;
    }

    @Info("Alles the mold to be placed in mold tables and sets the model it uses")
    public MoldItemBuilder allowedInMoldTable(String[] pattern) {
        return allowedInMoldTable(pattern, null);
    }

    @Info("Allows the mold to be placed in mold tables and sets the model it uses")
    public MoldItemBuilder allowedInMoldTable(String[] pattern, @Nullable Map<String, String> textures) {
        Assistant.singleTag(this, TFCTags.Items.USABLE_IN_MOLD_TABLE);
        if (pattern.length != 14) {
            throw new IllegalArgumentException("Pattern must be 14 high by 14 wide! Was not in %s mold".formatted(id));
        }
        moldTablePattern = pattern;
        moldTableTextures = textures;
        return this;
    }

    @Override
    public Item createObject() {
        return new MoldItem(capacity, allowedFluids, createItemProperties());
    }

    @Override
    protected void generateItemModels(KubeAssetGenerator generator) {
        super.generateItemModels(generator);
        if (moldTablePattern != null) {
            generator.blockModel(id.withPrefix("mold/"), m -> {
                if (moldTableTextures != null && !moldTableTextures.isEmpty()) {
                    m.textures(moldTableTextures);
                } else {
                    m.texture("0", "tfc:block/mold");
                    m.texture("particle", "tfc:block/mold");
                }
                m.custom(j -> {
                    j.addProperty("loader", "tfc:mold");
                    final JsonArray arr = new JsonArray(14);
                    for (String str : moldTablePattern) {
                        arr.add(str);
                    }
                    j.add("pattern", arr);
                });
            });
        }
    }
}
