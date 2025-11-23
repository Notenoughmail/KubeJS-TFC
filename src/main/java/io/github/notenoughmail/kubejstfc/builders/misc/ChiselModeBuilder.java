package io.github.notenoughmail.kubejstfc.builders.misc;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import net.dries007.tfc.common.player.ChiselMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

@ReturnsSelf
public class ChiselModeBuilder extends BuilderBase<ChiselMode> {

    public transient int priority = 300;
    private transient HotbarIcon hotbarIcon = HotbarIcon.DEFAULT;
    private transient RecipeIcon recipeIcon = RecipeIcon.DEFAULT;
    public transient ChiselBehavior chiselBehavior = (original, chiseled, player, hit) -> chiseled;

    public ChiselModeBuilder(ResourceLocation id) {
        super(id);
    }

    private static ResourceLocation asTexture(ResourceLocation loc) {
        return loc.withPath(s -> "textures/" + s + ".png");
    }

    @Info("Sets the sorting priority of the mode")
    public ChiselModeBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }

    @Info("Sets the texture & texture offsets to use when displaying the mode on the hotbar")
    public ChiselModeBuilder hotbarIcon(ResourceLocation textureLocation, int x, int y) {
        hotbarIcon = new HotbarIcon(
                asTexture(textureLocation),
                x,
                y
        );
        return this;
    }

    @Info("Sets the texture, texture offsets, & texture size to use when displaying the mode in recipe viewers")
    public ChiselModeBuilder recipeIcon(ResourceLocation textureLocation, int x, int y, int width, int height) {
        recipeIcon = new RecipeIcon(
                asTexture(textureLocation),
                x,
                y,
                width,
                height
        );
        return this;
    }

    @Info("Sets the chiseling behavior")
    public ChiselModeBuilder chiselBehavior(ChiselBehavior behavior) {
        chiselBehavior = behavior;
        return this;
    }

    @Override
    public ChiselMode createObject() {
        return new ChiselMode(priority) {
            @Override
            @Nullable
            public BlockState modifyStateForPlacement(BlockState original, BlockState chiseled, Player player, BlockHitResult hit) {
                return chiselBehavior.place(original, chiseled, player, hit);
            }

            @Override
            public <T> T createIcon(IconCallback<T> callback) {
                return callback.accept(
                        recipeIcon.tex(),
                        recipeIcon.u(),
                        recipeIcon.v(),
                        recipeIcon.width(),
                        recipeIcon.height()
                );
            }

            @Override
            public void createHotbarIcon(HotbarIconCallback callback) {
                callback.accept(
                        hotbarIcon.tex(),
                        hotbarIcon.u(),
                        hotbarIcon.v()
                );
            }
        };
    }

    @FunctionalInterface
    public interface ChiselBehavior {

        @Nullable
        BlockState place(BlockState original, BlockState chiseled, Player player, BlockHitResult hit);
    }

    private record RecipeIcon(ResourceLocation tex, int u, int v, int width, int height) {

        static final RecipeIcon DEFAULT = new RecipeIcon(KubeJSTFC.mc("missingno"), 0, 0, 16, 16);
    }

    private record HotbarIcon(ResourceLocation tex, int u, int v) {

        static final HotbarIcon DEFAULT = new HotbarIcon(KubeJSTFC.mc("missingno"), 0, 0);
    }
}
