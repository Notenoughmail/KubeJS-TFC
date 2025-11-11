package com.notenoughmail.kubejs_tfc;

import com.notenoughmail.kubejs_tfc.block.*;
import com.notenoughmail.kubejs_tfc.block.moss.*;
import com.notenoughmail.kubejs_tfc.recipe.schema.*;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.client.ClientEventHandlers;
import com.notenoughmail.kubejs_tfc.util.implementation.recipe.KubeJSTFCRecipeSerializers;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import net.dries007.tfc.ForgeEventHandler;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.client.ClientEventHandler;
import net.dries007.tfc.client.ClientForgeEventHandler;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.util.InteractionManager;
import net.dries007.tfc.util.SelfTests;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;

// Mild Javadoc abuse

/**
 * TODO: [Future]
 * <ul>
 *     <li>Blocks
 *         <ul>
 *             <li>Kinetics -- custom</li>
 *         </ul>
 *     </li>
 * 	   <li>Custom BiomeExtensions -- 1.21</li>
 * 	   <li><a href="https://discord.com/channels/303440391124942858/1279992681652682874">Attach TFC entity properties to existing entities, ping mo_mo</a>
 * 	   It may be possible to fudge this with attributes</li>
 * </ul>
 */
public class KubeJSTFCPlugin extends KubeJSPlugin {

    @Override
    public void init() {
        RegistryInfo.BLOCK.addType("tfc:stationary_berry_bush", StationaryBerryBushBlockBuilder.class, StationaryBerryBushBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:spreading_berry_bush", SpreadingBushBlockBuilder.class, SpreadingBushBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:crop", DefaultCropBlockBuilder.class, DefaultCropBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:flooded_crop", FloodedCropBlockBuilder.class, FloodedCropBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:pickable_crop", PickableCropBlockBuilder.class, PickableCropBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:spreading_crop", SpreadingCropBlockBuilder.class, SpreadingCropBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:double_crop", DoubleCropBlockBuilder.class, DoubleCropBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:wild_crop", WildCropBlockBuilder.class, WildCropBlockBuilder::new);
    }

    @Override
    public void afterInit() {
        final ForgeConfigSpec spec = KubeJSTFC.serverConfigBuilder.build();

        if (!spec.isEmpty()) {
            ModList.get().getModContainerById(KubeJSTFC.MODID).ifPresent(container ->
                    container.addConfig(new ModConfig(ModConfig.Type.SERVER, spec, container, "kubejs-tfc-server.toml")));
        }
    }
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.namespace(TerraFirmaCraft.MOD_ID)
                .register(TFCRecipeSerializers.ALLOY.getId().getPath(), AlloySchema.SCHEMA)
                .register(TFCRecipeSerializers.WELDING.getId().getPath(), WeldingSchema.SCHEMA)
                .register(TFCRecipeSerializers.ANVIL.getId().getPath(), AnvilSchema.SCHEMA)
                .register(TFCRecipeSerializers.INSTANT_FLUID_BARREL.getId().getPath(), BarrelInstantFluidSchema.SCHEMA)
                .register(TFCRecipeSerializers.INSTANT_BARREL.getId().getPath(), BarrelInstantSchema.SCHEMA)
                .register(TFCRecipeSerializers.SEALED_BARREL.getId().getPath(), BarrelSealedSchema.SCHEMA)
                .register(TFCRecipeSerializers.BLAST_FURNACE.getId().getPath(), BlastFurnaceSchema.SCHEMA)
                .register(TFCRecipeSerializers.BLOOMERY.getId().getPath(), BloomerySchema.SCHEMA)
                .register(TFCRecipeSerializers.CASTING.getId().getPath(), CastingSchema.SCHEMA)
                .register(TFCRecipeSerializers.CHISEL.getId().getPath(), ChiselSchema.SCHEMA)
                .register(TFCRecipeSerializers.COLLAPSE.getId().getPath(), MovingBlockSchema.SCHEMA)
                .register(TFCRecipeSerializers.GLASSWORKING.getId().getPath(), GlassworkingSchema.SCHEMA)
                .register(TFCRecipeSerializers.HEATING.getId().getPath(), HeatingSchema.SCHEMA)
                .register(TFCRecipeSerializers.KNAPPING.getId().getPath(), KnappingSchema.SCHEMA)
                .register(TFCRecipeSerializers.LANDSLIDE.getId().getPath(), MovingBlockSchema.SCHEMA)
                .register(TFCRecipeSerializers.LOOM.getId().getPath(), LoomSchema.SCHEMA)
                .register(TFCRecipeSerializers.POT_JAM.getId().getPath(), JamPotSchema.SCHEMA)
                .register(TFCRecipeSerializers.POT_SIMPLE.getId().getPath(), SimplePotSchema.SCHEMA)
                .register(TFCRecipeSerializers.POT_SOUP.getId().getPath(), SoupPotSchema.SCHEMA)
                .register(TFCRecipeSerializers.QUERN.getId().getPath(), BasicSchema.SCHEMA)
                .register(TFCRecipeSerializers.SCRAPING.getId().getPath(), ScrapingSchema.SCHEMA)
                .register(TFCRecipeSerializers.ADVANCED_SHAPED_CRAFTING.getId().getPath(), AdvancedCraftingSchema.SHAPED)
                .register(TFCRecipeSerializers.ADVANCED_SHAPELESS_CRAFTING.getId().getPath(), AdvancedCraftingSchema.SHAPELESS)
                .register(TFCRecipeSerializers.DAMAGE_INPUT_SHAPED_CRAFTING.getId().getPath(), DelegateCraftingSchema.schema("damage"))
                .register(TFCRecipeSerializers.DAMAGE_INPUTS_SHAPELESS_CRAFTING.getId().getPath(), DelegateCraftingSchema.schema("damage"))
                .register(TFCRecipeSerializers.EXTRA_PRODUCTS_SHAPED_CRAFTING.getId().getPath(), ExtraProductCraftingSchema.SCHEMA)
                .register(TFCRecipeSerializers.EXTRA_PRODUCTS_SHAPELESS_CRAFTING.getId().getPath(), ExtraProductCraftingSchema.SCHEMA)
                .register(TFCRecipeSerializers.NO_REMAINDER_SHAPED_CRAFTING.getId().getPath(), DelegateCraftingSchema.schema("no_remainder"))
                .register(TFCRecipeSerializers.NO_REMAINDER_SHAPELESS_CRAFTING.getId().getPath(), DelegateCraftingSchema.schema("no_remainder"))
                .register(TFCRecipeSerializers.SEWING.getId().getPath(), SewingSchema.SCHEMA)
                ;

        event.namespace(KubeJSTFC.MODID)
                .register(KubeJSTFCRecipeSerializers.SHAPED.getId().getPath(), AdvancedCraftingSchema.SHAPED)
                .register(KubeJSTFCRecipeSerializers.SHAPELESS.getId().getPath(), AdvancedCraftingSchema.SHAPELESS)
                ;
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        // KubeJS TFC
        filter.allow("com.notenoughmail.kubejs_tfc");
        filter.deny("com.notenoughmail.kubejs_tfc.util.implementation.mixin");
        filter.deny(KubeJSTFCPlugin.class);
        filter.deny(RegistryUtils.class);
        filter.deny(EventHandlers.class);
        filter.deny(ClientEventHandlers.class);
        filter.deny("com.notenoughmail.kubejs_tfc.addons.precpros.PrecProsPlugin");
        filter.deny("com.notenoughmail.kubejs_tfc.addons.firmalife.FirmaLifePlugin");
        filter.deny("com.notenoughmail.kubejs_tfc.addons.afc.AFCPlugin");
        filter.deny("com.notenoughmail.kubejs_tfc.addons.tfccc.TFCCCPlugin");
        // TFC
        filter.allow("net.dries007.tfc");
        filter.deny("net.dries007.tfc.mixin");
        filter.deny("net.dries.tfc.network");
        filter.deny(SelfTests.class);
        filter.deny(ForgeEventHandler.class);
        filter.deny(InteractionManager.class);
        filter.deny(ClientEventHandler.class);
        filter.deny(ClientForgeEventHandler.class);
    }
}