package io.github.notenoughmail.kubejstfc;

import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.HashSet;
import java.util.Set;

public class GroundcoverModelProvider extends ModelProvider<BlockModelBuilder> {

    public GroundcoverModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, KubeJSTFC.ID, BLOCK_FOLDER, BlockModelBuilder::new, existingFileHelper);
    }

    private final Set<ResourceLocation> EXCLUDE = new HashSet<>();

    @Override
    protected void registerModels() {
        for (RockCategory category : RockCategory.values()) {
            for (int i = 1 ; i < 4 ; i++) {
                withExistingParent(
                        "groundcover/loose/%s_%s".formatted(category.getSerializedName(), i),
                        KubeJSTFC.tfc("block/rock/loose_%s_%s".formatted(category.getSerializedName(), i))
                ).texture("texture", "#all");
            }
        }

        exclude("guano", "humus", "salt_lick");
        for (int i = 2 ; i <= 14 ; i += 2){
            exclude("fallen_leaves_height%s".formatted(i));
        }

        final ResourceManager manager = Assistant.invokePrivateMethod(existingFileHelper, "getManager", PackType.CLIENT_RESOURCES);

        manager.listResources("models/block/groundcover", this::isValid)
                .keySet()
                .stream()
                .map(ResourceLocation::getPath)
                .map(this::clip)
                .forEach(str ->
                                withExistingParent(
                                        "groundcover/%s".formatted(str),
                                        KubeJSTFC.tfc("block/groundcover/%s".formatted(str))
                                ).texture("0", "#all")
                                        .texture("1", "#all")
                                        .texture("particle", "#all")
                );

        getBuilder("groundcover/flat")
                .texture("particle", "#all")
                .element()
                .from(0, 0, 0)
                .to(16, 1, 16)
                .face(Direction.NORTH)
                    .cullface(Direction.NORTH)
                    .texture("#all")
                    .uvs(0, 0, 16, 1)
                    .end()
                .face(Direction.SOUTH)
                    .cullface(Direction.SOUTH)
                    .texture("#all")
                    .uvs(0, 0, 16, 1)
                    .end()
                .face(Direction.EAST)
                    .cullface(Direction.EAST)
                    .texture("#all")
                    .uvs(0, 0, 16, 1)
                    .end()
                .face(Direction.WEST)
                    .cullface(Direction.WEST)
                    .texture("#all")
                    .uvs(0, 0, 16, 1)
                    .end()
                .face(Direction.UP)
                    .texture("#all")
                    .uvs(0, 0, 16, 16)
                    .end()
                .face(Direction.DOWN)
                    .cullface(Direction.DOWN)
                    .texture("#all")
                    .uvs(0, 0, 16, 16);
    }

    @Override
    public String getName() {
        return "KubeJS TFC Groundcover Block Models";
    }

    @Override
    public BlockModelBuilder getBuilder(String path) {
        ResourceLocation id = ResourceLocation.tryParse(path).withPrefix(folder + "/");
        id = KubeJSTFC.id(id.getPath());
        existingFileHelper.trackGenerated(id, MODEL);
        return generatedModels.computeIfAbsent(id, factory);
    }

    void exclude(String... paths) {
        for (String str : paths) {
            exclude(str);
        }
    }

    void exclude(String path) {
        EXCLUDE.add(KubeJSTFC.tfc("models/block/groundcover/%s.json".formatted(path)));
    }

    boolean isValid(ResourceLocation id) {
        return id.getNamespace().equals(TerraFirmaCraft.MOD_ID) && id.getPath().endsWith(".json") && !EXCLUDE.contains(id);
    }

    String clip(String str) {
        return str.substring(str.lastIndexOf('/') + 1, str.indexOf('.'));
    }
}
