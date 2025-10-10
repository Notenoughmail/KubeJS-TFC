package io.github.notenoughmail.kubejstfc;

import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachmentRegistry;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.recipe.schema.function.RecipeSchemaFunctionRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.registry.ServerRegistryRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.DataComponentTypeInfoRegistry;
import dev.latvian.mods.kubejs.script.TypeDescriptionRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import io.github.notenoughmail.kubejstfc.builders.misc.GlassOperationBuilder;
import io.github.notenoughmail.kubejstfc.events.KubeJSTFCEventHandlers;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.minecraft.core.registries.Registries;

import static io.github.notenoughmail.kubejstfc.KubeJSTFC.id;

public class KubeJSTFCPlugin implements KubeJSPlugin {


    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {

        registry.addDefault(GlassOperation.KEY, GlassOperationBuilder.class, GlassOperationBuilder::new);

        registry.of(Registries.BLOCK, callback -> {

        });

        registry.of(Registries.ITEM, callback -> {

        });

        registry.of(Registries.FLUID, callback -> {

        });
    }

    @Override
    public void registerServerRegistries(ServerRegistryRegistry registry) {
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(KubeJSTFCEventHandlers.TFCEvents);
    }

    @Override
    public void registerClasses(ClassFilter filter) {
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry) {
    }

    @Override
    public void registerTypeDescriptions(TypeDescriptionRegistry registry) {
    }

    @Override
    public void registerRecipeSchemaFunctionTypes(RecipeSchemaFunctionRegistry registry) {
    }

    @Override
    public void registerRecipeComponents(RecipeComponentTypeRegistry registry) {
    }

    @Override
    public void registerBlockEntityAttachments(BlockEntityAttachmentRegistry registry) {
    }

    @Override
    public void registerDataComponentTypeDescriptions(DataComponentTypeInfoRegistry registry) {
    }

    // TODO: 2.0.0 | Will this finally work for the data events?
    @Override
    public void generateData(KubeDataGenerator generator) {
    }
}
