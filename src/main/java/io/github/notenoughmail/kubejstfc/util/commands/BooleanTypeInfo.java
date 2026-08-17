package io.github.notenoughmail.kubejstfc.util.commands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public final class BooleanTypeInfo<T, A extends ArgumentType<T>, I extends BooleanTypeInfo<T, A, I>> implements ArgumentTypeInfo<A, BooleanTypeInfo.Template<T, A, I>> {
    
    public static <T, A extends ArgumentType<T>, I extends BooleanTypeInfo<T, A, I>> BooleanTypeInfo<T, A, I> of(
            Predicate<A> unwrap,
            BiFunction<CommandBuildContext, Boolean, A> wrap
    ) {
        return new BooleanTypeInfo<>(unwrap, wrap);
    }

    public static <T, A extends ArgumentType<T>, I extends BooleanTypeInfo<T, A, I>> BooleanTypeInfo<T, A, I> of(
            Predicate<A> unwrap,
            Function<Boolean, A> wrap
    ) {
        return new BooleanTypeInfo<>(unwrap, ($, b) -> wrap.apply(b));
    }


    private final Predicate<A> unwrap;
    private final BiFunction<CommandBuildContext, Boolean, A> wrap;
    
    private BooleanTypeInfo(
            Predicate<A> unwrap,
            BiFunction<CommandBuildContext, Boolean, A> wrap
    ) {
        this.unwrap = unwrap;
        this.wrap = wrap;
    }

    @Override
    public void serializeToNetwork(Template<T, A, I> template, FriendlyByteBuf buffer) {
        buffer.writeBoolean(template.value());
    }

    @Override
    public Template<T, A, I> deserializeFromNetwork(FriendlyByteBuf buffer) {
        return new Template<>(
                buffer.readBoolean(),
                this
        );
    }

    @Override
    public void serializeToJson(Template<T, A, I> template, JsonObject json) {
        json.addProperty("value", template.value());
    }

    @Override
    public Template<T, A, I> unpack(A argument) {
        return new Template<>(
                unwrap.test(argument),
                this
        );
    }

    public record Template<T, A extends ArgumentType<T>, I extends BooleanTypeInfo<T, A, I>>(
            boolean value,
            BooleanTypeInfo<T, A, I> type
    ) implements ArgumentTypeInfo.Template<A> {

        @Override
        public A instantiate(CommandBuildContext context) {
            return type.wrap.apply(context, value);
        }
    }
}
