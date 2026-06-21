package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.sub.HorizontalSupportBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.item.StandingAndWallBlockItemBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.DelayedBuilder;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.wood.VerticalSupportBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@ReturnsSelf
@SuppressWarnings("unused")
public class SupportBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private static final ResourceLocation CONNECTION = KubeJSTFC.tfc("block/wood/support/connection");
    private static final ResourceLocation VERTICAL = KubeJSTFC.tfc("block/wood/support/vertical");
    private static final ResourceLocation INVENTORY = KubeJSTFC.tfc("block/wood/support/inventory");

    public transient final DelayedBuilder<HorizontalSupportBlockBuilder> horizontal;
    public transient ResourceLocation connectionModel;
    public transient boolean defaultConnection;

    public SupportBlockBuilder(ResourceLocation i) {
        super(i);
        horizontal = new DelayedBuilder<>(r -> new HorizontalSupportBlockBuilder(r, this), () -> id.withSuffix("_horizontal"));
        itemBuilder = new StandingAndWallBlockItemBuilder(id, this, horizontal);
        Assistant.singleTag(this, TFCTags.Blocks.SUPPORT_BEAMS);
        connectionModel = newID("block/", "_connection");
        defaultConnection = true;
    }

    @Info("Sets the model used by this and the horizontal block for sideways connections")
    public SupportBlockBuilder connectionModel(ResourceLocation model) {
        this.connectionModel = model;
        defaultConnection = false;
        return this;
    }

    @Info("Sets the properties of the horizontal support block")
    public SupportBlockBuilder horizontal(Consumer<HorizontalSupportBlockBuilder> horizontalSupport) {
        return horizontal(null, horizontalSupport);
    }

    @Info("Sets the properties of the horizontal support block")
    public SupportBlockBuilder horizontal(@Nullable KubeResourceLocation id, Consumer<HorizontalSupportBlockBuilder> horizontalSupport) {
        horizontal.accept(id, horizontalSupport);
        return this;
    }

    @Override
    public Block createObject() {
        return new VerticalSupportBlock(createExtendedProperties().noOcclusion());
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        super.createAdditionalObjects(registry);
        Assistant.addBlock(registry, horizontal.get(), false);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(VERTICAL);
            m.textures(textures);
        });
        if (defaultConnection) {
            generator.blockModel(id.withSuffix("_connection"), m -> {
                m.parent(CONNECTION);
                m.textures(textures);
            });
        }
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.itemModelGen(this, m, g -> {
            g.parent(INVENTORY);
            g.textures(textures);
        });
    }

    @Override
    protected boolean useMultipartBlockState() {
        return true;
    }

    @Override
    protected void generateMultipartBlockState(MultipartBlockStateGenerator bs) {
        bs.part("", ModelUtil.plainModel(this));
        bs.part("north=true", p -> p.model(connectionModel).y(270));
        bs.part("east=true", connectionModel);
        bs.part("south=true", p -> p.model(connectionModel).y(90));
        bs.part("west=true", p -> p.model(connectionModel).y(180));
    }
}
