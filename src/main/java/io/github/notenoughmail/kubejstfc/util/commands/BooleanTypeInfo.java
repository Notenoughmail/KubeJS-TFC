package io.github.notenoughmail.kubejstfc.util.commands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class BooleanTypeInfo<T, A extends ArgumentType<T>, I extends BooleanTypeInfo<T, A, I>> implements ArgumentTypeInfo<A, BooleanTypeInfo.Template<T, A, I>> {

    private final BiFunction<CommandBuildContext, Boolean, A> templateBuilder;
    private final Predicate<A> valueExtractor;

    protected BooleanTypeInfo(
            BiFunction<CommandBuildContext, Boolean, A> templateBuilder,
            Predicate<A> valueExtractor
    ) {
        this.templateBuilder = templateBuilder;
        this.valueExtractor = valueExtractor;
    }

    @Override
    public void serializeToNetwork(Template<T, A, I> template, FriendlyByteBuf buffer) {
        buffer.writeBoolean(template.value());
    }

    @Override
    public Template<T, A, I> deserializeFromNetwork(FriendlyByteBuf buffer) {
        return new Template<>(
                buffer.readBoolean(),
                templateBuilder,
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
                valueExtractor.test(argument),
                templateBuilder,
                this
        );
    }

    public record Template<T, A extends ArgumentType<T>, I extends BooleanTypeInfo<T, A, I>>(boolean value, BiFunction<CommandBuildContext, Boolean, A> builder, BooleanTypeInfo<T, A, I> type) implements ArgumentTypeInfo.Template<A> {

        @Override
        public A instantiate(CommandBuildContext context) {
            return builder().apply(context, value);
        }
    }
}
