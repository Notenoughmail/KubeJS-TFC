package com.notenoughmail.kubejs_tfc.util.implementation.mixin.extensions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ljuangbminecraft.tfcchannelcasting.TFCChannelCasting;
import com.ljuangbminecraft.tfcchannelcasting.common.TFCCCTags;
import com.notenoughmail.kubejs_tfc.item.MoldItemBuilder;
import com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions.ITFCCCMoldItemBuilderExtensions;
import com.notenoughmail.kubejs_tfc.util.implementation.IfPresent;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@IfPresent(TFCChannelCasting.MOD_ID)
@SuppressWarnings("unused")
@Mixin(value = MoldItemBuilder.class, remap = false)
public abstract class TFCCCMoldItemBuilderExtensions extends ItemBuilder implements ITFCCCMoldItemBuilderExtensions {

    @Unique
    private transient List<String> kubejs_tfc$model;

    public TFCCCMoldItemBuilderExtensions(ResourceLocation i) {
        super(i);
    }

    @Override
    public MoldItemBuilder kubeJS_TFC$tfcccAllowedInMoldTable() {
        tag(TFCCCTags.Items.ACCEPTED_IN_MOLD_TABLES.location());
        return (MoldItemBuilder) (Object) this;
    }

    @Override
    public MoldItemBuilder kubeJS_TFC$tfcccAllowedInMoldTable(List<String> model) {
        if (model.size() != 14) {
            throw new IllegalArgumentException("The mold table model must have 14 rows each of length 14");
        }
        kubejs_tfc$model = model;
        return kubeJS_TFC$tfcccAllowedInMoldTable();
    }

    @Inject(method = "generateAssetJsons", at = @At("TAIL"), remap = false)
    private void kubejs_tfc$generateMoldTableModel(AssetJsonGenerator generator, CallbackInfo ci) {
        if (kubejs_tfc$model != null) {
            final JsonObject model = new JsonObject();
            model.addProperty("loader", "tfcchannelcasting:mold");
            final JsonObject textures = new JsonObject();
            textures.addProperty("0", "tfcchannelcasting:block/mold_texture");
            textures.addProperty("particle", "tfcchannelcasting:block/mold_texture");
            model.add("textures", textures);
            final JsonArray pattern = new JsonArray(14);
            kubejs_tfc$model.forEach(pattern::add);
            model.add("pattern", pattern);

            generator.json(kubejs_tfc$moldTableModelId(), model);
        }
    }

    @Unique
    private ResourceLocation kubejs_tfc$moldTableModelId() {
        return new ResourceLocation("tfcchannelcasting", "models/mold/" + id.getNamespace() + "/" + id.getPath());
    }
}
