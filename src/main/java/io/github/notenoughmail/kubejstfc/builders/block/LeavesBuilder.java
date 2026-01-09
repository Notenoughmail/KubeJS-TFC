package io.github.notenoughmail.kubejstfc.builders.block;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import net.minecraft.resources.ResourceLocation;

@ReturnsSelf
public abstract class LeavesBuilder extends ExtendedPropertiesBlockBuilder {

    protected static final ResourceLocation LEAVES = KubeJSTFC.mc("block/leaves");

    public transient int autumnIndex;
    public transient boolean seasonalColors;

    public LeavesBuilder(ResourceLocation i) {
        super(i);
        BuilderRefs.leafColor.add(this);
        autumnIndex = 0;
        seasonalColors = false;
    }

    @Info("Sets the vertical coordinate, in the range [0, 255], on TFC's `foliage_fall` colormap for dynamic tinting of the leaves the leaves")
    public LeavesBuilder autumnIndex(int index) {
        autumnIndex = index;
        return this;
    }

    @Info("Make the dynamic tinting of the leaves change with the seasons")
    public LeavesBuilder seasonalColors() {
        this.seasonalColors = true;
        return this;
    }

    @Info("Removes the dynamic item and block tinting that is applied to this block by default")
    public LeavesBuilder noDynamicTinting() {
        BuilderRefs.leafColor.remove(this);
        return this;
    }

    @HideFromJS
    public boolean hasSeasonalColors() { return seasonalColors; }

    @HideFromJS
    public int autumnIndex() { return autumnIndex; }

    @HideFromJS
    public boolean isFallen() { return false; }
}
