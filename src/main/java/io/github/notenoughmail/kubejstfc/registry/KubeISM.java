package io.github.notenoughmail.kubejstfc.registry;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.handler.codec.DecoderException;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifierType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record KubeISM(String id, ModifierApplicator applicator) implements ItemStackModifier {

    private static final Supplier<Map<String, ModifierApplicator>> TYPES = Suppliers.memoize(HashMap::new);

    public static void registerApplicator(String id, ModifierApplicator applicator) {
        if (TYPES.get().put(id, applicator) != null) {
            throw new IllegalArgumentException("Duplicate custom ItemStackModifier type ids");
        }
    }

    private static KubeISM of(String id) {
        final ModifierApplicator applicator = TYPES.get().get(id);
        if (applicator == null) {
            throw new DecoderException("Unknown custom ItemStackModifier type");
        }
        return new KubeISM(id, applicator);
    }

    public static final MapCodec<KubeISM> CODEC = Codec.STRING.fieldOf("id").xmap(
            KubeISM::of,
            KubeISM::id
    );
    // Would love to #map the default string codec, but it uses a normal ByteBuf
    public static final StreamCodec<RegistryFriendlyByteBuf, KubeISM> STREAM_CODEC = StreamCodec.of(
            (buf, ism) -> buf.writeUtf(ism.id),
            (buf) -> of(buf.readUtf())
    );

    @Override
    public ItemStack apply(ItemStack stack, ItemStack input, Context ctx) {
        return applicator.apply(stack, input, ctx);
    }

    @Override
    public ItemStackModifierType<KubeISM> type() {
        return KubeJSTFCRegistries.CUSTOM_ISM.get();
    }

    @Override
    public boolean dependsOnInput() {
        return applicator.inputDependent();
    }

    @FunctionalInterface
    public interface ModifierApplicator {

        ItemStack apply(ItemStack stack, ItemStack input, Context ctx);

        default boolean inputDependent() {
            return false;
        }
    }
}
