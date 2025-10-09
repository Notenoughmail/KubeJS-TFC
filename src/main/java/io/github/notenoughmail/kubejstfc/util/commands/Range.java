package io.github.notenoughmail.kubejstfc.util.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

// Vanilla's range does not require both ends, nor gives access to their values
public record Range(double min, double max) {

    public double range() {
        return max - min;
    }

    public double step(int range) {
        return range() / range;
    }

    public static Range get(String name, CommandContext<CommandSourceStack> ctx) {
        return ctx.getArgument(name, Range.class);
    }

    public static RangeArgumentType arg() {
        return RangeArgumentType.INSTANCE;
    }

    public enum RangeArgumentType implements ArgumentType<Range> {
        INSTANCE;

        private static final Collection<String> EXAMPLES = Arrays.asList("0 0.1", "1.2 79", "100000 5");
        private static final Collection<SharedSuggestionProvider.TextCoordinates> DEFAULT_SUGGESTION = List.of(new SharedSuggestionProvider.TextCoordinates("-1", "0", "1"));
        private static final Predicate<String> SUGGESTION_VALIDATOR = Commands.createValidator(INSTANCE::parse);

        public static final SimpleCommandExceptionType INVALID = new SimpleCommandExceptionType(Component.literal("Range must have two different values"));

        @Override
        public Range parse(StringReader reader) throws CommandSyntaxException {
            if (!reader.canRead()) {
                throw INVALID.createWithContext(reader);
            } else {
                int start = reader.getCursor();
                final double a = reader.readDouble();
                reader.skipWhitespace();
                if (reader.canRead() && StringReader.isAllowedNumber(reader.peek())) {
                    final double b = reader.readDouble();
                    if (a == b) {
                        reader.setCursor(start);
                        throw INVALID.createWithContext(reader);
                    }
                    return new Range(
                            Math.min(a, b),
                            Math.max(a, b)
                    );
                } else {
                    reader.setCursor(start);
                    throw INVALID.createWithContext(reader);
                }
            }
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            if (!(context.getSource() instanceof SharedSuggestionProvider)) {
                return Suggestions.empty();
            } else {
                return SharedSuggestionProvider.suggest2DCoordinates(builder.getRemaining(), DEFAULT_SUGGESTION, builder, SUGGESTION_VALIDATOR);
            }
        }


        @Override
        public Collection<String> getExamples() {
            return EXAMPLES;
        }
    }
}
