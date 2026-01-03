package io.github.notenoughmail.kubejstfc.compat.precpros;

import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import io.github.notenoughmail.precisionprospecting.PrecisionProspecting;
import net.minecraft.core.registries.Registries;

public class PrecProsPlugin implements KubeJSPlugin {

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.ITEM, c -> c.add(PrecisionProspecting.id("prospector"), ProspectorItemBuilder.class, ProspectorItemBuilder::new));
    }

    @Override
    public void registerClasses(ClassFilter filter) {
        filter.deny(PrecisionProspecting.class);
    }
}
