package io.github.notenoughmail.kubejstfc.builders.misc;

import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

// TODO: 2.1.0 | Docs
@ReturnsSelf
public class RockSettingsBuilder extends BuilderBase<RockSettings> {

    @Nullable
    public transient Block raw, hardened, gravel, cobble, sand, sandstone, spike, loose, mossyLoose;
    public transient boolean karst, mafic;

    public RockSettingsBuilder(ResourceLocation id) {
        super(id);
    }

    public RockSettingsBuilder raw(Block b) {
        raw = b;
        return this;
    }

    public RockSettingsBuilder hardened(Block b) {
        hardened = b;
        return this;
    }

    public RockSettingsBuilder gravel(Block b) {
        gravel = b;
        return this;
    }

    public RockSettingsBuilder cobble(Block b) {
        cobble = b;
        return this;
    }

    public RockSettingsBuilder sand(Block b) {
        sand = b;
        return this;
    }

    public RockSettingsBuilder sandstone(Block b) {
        sandstone = b;
        return this;
    }

    public RockSettingsBuilder spike(Block b) {
        requiredProperty(b, "spike", RockSpikeBlock.PART);
        spike = b;
        return this;
    }

    public RockSettingsBuilder loose(Block b) {
        requiredProperty(b, "loose", LooseRockBlock.COUNT);
        loose = b;
        return this;
    }

    public RockSettingsBuilder mossyLoose(Block b) {
        requiredProperty(b, "mossyLoose", LooseRockBlock.COUNT);
        mossyLoose = b;
        return this;
    }

    public RockSettingsBuilder karst() {
        karst = true;
        return this;
    }

    public RockSettingsBuilder mafic() {
        mafic = true;
        return this;
    }

    @Override
    public RockSettings createObject() {
        return new RockSettings(
                Assistant.notNull(raw, "raw", sourceLine),
                Assistant.notNull(hardened, "hardened", sourceLine),
                Assistant.notNull(gravel, "gravel", sourceLine),
                Assistant.notNull(cobble, "cobble", sourceLine),
                Assistant.notNull(sand, "sand", sourceLine),
                Assistant.notNull(sandstone, "sandstone", sourceLine),
                Optional.ofNullable(spike),
                Optional.ofNullable(loose),
                Optional.ofNullable(mossyLoose),
                Optional.of(karst),
                Optional.of(mafic)
        );
    }

    private void requiredProperty(Block b, String name, Property<?> property) {
        if (!b.getStateDefinition().getProperties().contains(property))
            throw new KubeRuntimeException("'%s' must have %s state property".formatted(name, property.getName()))
                    .source(sourceLine)
                    .customData("required property", property);
    }
}
