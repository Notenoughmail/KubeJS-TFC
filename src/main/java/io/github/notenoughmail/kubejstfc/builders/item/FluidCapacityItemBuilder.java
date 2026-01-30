package io.github.notenoughmail.kubejstfc.builders.item;

import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.StringUtilsWrapper;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

@ReturnsSelf
public abstract class FluidCapacityItemBuilder extends ItemBuilder {

    public transient Supplier<Integer> capacity;
    public transient TagKey<Fluid> allowedFluids;

    public FluidCapacityItemBuilder(ResourceLocation id) {
        super(id);
        capacity = () -> 100;
        allowedFluids = TFCTags.Fluids.USABLE_IN_JUG;
        BuilderRefs.fluidContainers.add(this);
    }

    @HideFromJS
    public boolean mold() {
        return false;
    }

    @Override
    public FluidCapacityItemBuilder texture(String tex) {
        return textures(tex, tex + "_fluid");
    }

    @Info("Sets the base and overlay textures")
    public FluidCapacityItemBuilder textures(String base, String fluid) {
        textures.put("base", base);
        textures.put("fluid", fluid);
        return this;
    }

    @Info("Sets the capacity fo the fluid container")
    public FluidCapacityItemBuilder capacity(int amount) {
        capacity = () -> amount;
        return this;
    }

    @Info("Sets the capacity of the fluid container via a supplier")
    public FluidCapacityItemBuilder capacitySupplier(Supplier<Integer> amount) {
        capacity = amount;
        return this;
    }

    @Info("Sets which fluids the fluid container can hold")
    public FluidCapacityItemBuilder allowedFluids(TagKey<Fluid> allowed) {
        allowedFluids = allowed;
        return this;
    }

    @Override
    public abstract Item createObject();

    @Override
    protected void generateItemModels(KubeAssetGenerator generator) {
        generator.itemModel(id, m -> {
            if (modelGenerator != null) {
                modelGenerator.accept(m);
            } else {
                m.parent(parentModel == null ? ModelUtil.DEFAULT_ITEM_PARENT : parentModel);
                if (textures.isEmpty()) {
                    texture(ModelUtil.itemTexture(this));
                }
                m.textures(textures);
                m.custom(j -> j.addProperty("loader", "tfc:fluid_container"));
            }
        });
    }

    @ReturnsSelf
    public static abstract class WithLang extends FluidCapacityItemBuilder {

        public transient String filledKey;
        public transient String filledText;

        public WithLang(ResourceLocation id) {
            super(id);
        }

        @Info("Sets the display name of the fluid container when filled")
        public WithLang filledDisplayName(String text) {
            filledText = text;
            return this;
        }

        @HideFromJS
        public String getFilledKey() {
            if (filledKey == null) {
                filledKey = getBuilderTranslationKey() + ".filled";
            }
            return filledKey;
        }

        @Override
        public void generateLang(LangKubeEvent lang) {
            super.generateLang(lang);
            if (filledText != null) {
                lang.add(id.getNamespace(), getFilledKey(), filledText);
            } else {
                lang.add(id.getNamespace(), getFilledKey(), "%s " + StringUtilsWrapper.snakeCaseToTitleCase(id.getPath()));
            }
        }
    }
}
