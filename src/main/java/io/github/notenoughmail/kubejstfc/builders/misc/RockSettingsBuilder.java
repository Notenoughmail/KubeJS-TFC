package io.github.notenoughmail.kubejstfc.builders.misc;

import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
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

@ReturnsSelf
public class RockSettingsBuilder extends BuilderBase<RockSettings> {

    @Nullable
    public transient Block raw, hardened, gravel, cobble, sand, sandstone, spike, loose, mossyLoose;
    public transient boolean karst, mafic;

    public RockSettingsBuilder(ResourceLocation id) {
        super(id);
    }

    @Info("The raw block of the rock")
    public RockSettingsBuilder raw(Block b) {
        raw = b;
        return this;
    }

    @Info("The hardened block of the rock")
    public RockSettingsBuilder hardened(Block b) {
        hardened = b;
        return this;
    }

    @Info("The gravel block of the rock")
    public RockSettingsBuilder gravel(Block b) {
        gravel = b;
        return this;
    }

    @Info("The cobble block of the rock")
    public RockSettingsBuilder cobble(Block b) {
        cobble = b;
        return this;
    }

    @Info("The sand block of the rock")
    public RockSettingsBuilder sand(Block b) {
        sand = b;
        return this;
    }

    @Info("The sandstone block of the rock")
    public RockSettingsBuilder sandstone(Block b) {
        sandstone = b;
        return this;
    }

    @Info("The spike block of the rock")
    public RockSettingsBuilder spike(Block b) {
        requiredProperty(b, "spike", RockSpikeBlock.PART);
        spike = b;
        return this;
    }

    @Info("The loose block of the rock")
    public RockSettingsBuilder loose(Block b) {
        requiredProperty(b, "loose", LooseRockBlock.COUNT);
        loose = b;
        return this;
    }

    @Info("The mossy loose block of the rock")
    public RockSettingsBuilder mossyLoose(Block b) {
        requiredProperty(b, "mossyLoose", LooseRockBlock.COUNT);
        mossyLoose = b;
        return this;
    }

    @Info("Mark the rock as being karst")
    public RockSettingsBuilder karst() {
        karst = true;
        return this;
    }

    @Info("Mark the rock as being mafic")
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
