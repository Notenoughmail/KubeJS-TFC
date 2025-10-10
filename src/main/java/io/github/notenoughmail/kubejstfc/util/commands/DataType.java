package io.github.notenoughmail.kubejstfc.util.commands;

import com.google.common.base.Predicates;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.latvian.mods.kubejs.util.Cast;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface DataType<T> {

    static Argument all() {
        return new Argument(true);
    }

    static Argument searchable() {
        return new Argument(false);
    }

    static <K> DataType<K> get(String name, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return Cast.to(Argument.getDataType(ctx, name));
    }

    /**
     * The name of this {@code DataType}
     */
    String name();

    /**
     * Potentially converts the string into an object handled by this {@code DataType}
     */
    @Nullable
    T find(String str);

    /**
     * Adds a description of the given object to the text
     */
    void display(T value, MutableComponent text);

    /**
     * If {@link #search(String)} should be called
     */
    boolean canBeSearched();

    /**
     * Returns the names of objects which can be {@link #find(String) found} and subsequently {@link #display(Object, MutableComponent) displayed}
     */
    Set<String> search(String str);

    /**
     * Returns strings which would be accepted in {@link #search(String)}
     */
    Stream<String> searchSuggestions();

    /**
     * Strings which can be {@link #find(String) found} and {@link #display(Object, MutableComponent) displayed}
     */
    default Stream<String> suggest() {
        return names().stream();
    }

    /**
     * The names of all objects handled by this {@code DataType}
     */
    Set<String> names();

    record Argument(boolean all) implements ArgumentType<ResourceLocation> {

        private static Predicate<DataType<?>> filter(boolean all) {
            return all ?
                    Predicates.alwaysTrue() :
                    DataType::canBeSearched;
        }

        private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(loc -> Component.literal("Unknown DataType '%s'".formatted(loc)));

        static DataType<?> getDataType(CommandContext<CommandSourceStack> ctx, String name) throws CommandSyntaxException {
            final ResourceLocation loc = ctx.getArgument(name, ResourceLocation.class);
            final DataType<?> type = KubeJSTFCRegistries.DATA_TYPES.get(loc);

            if (type == null) {
                throw ERROR_INVALID.create(loc);
            } else {
                return type;
            }
        }

        @Override
        public ResourceLocation parse(StringReader reader) throws CommandSyntaxException {
            return ResourceLocation.read(reader);
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return SharedSuggestionProvider.suggestResource(
                    KubeJSTFCRegistries.DATA_TYPES.stream()
                            .filter(filter(all))
                            .map(KubeJSTFCRegistries.DATA_TYPES::getKey),
                    builder
            );
        }
    }

    TypeInfo TYPE_INFO = new TypeInfo();

    final class TypeInfo extends BooleanTypeInfo<ResourceLocation, Argument, TypeInfo> {

        private TypeInfo() {
            super((ctx, b) -> new Argument(b), Argument::all);
        }
    }
}
