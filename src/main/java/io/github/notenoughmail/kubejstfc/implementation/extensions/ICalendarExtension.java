package io.github.notenoughmail.kubejstfc.implementation.extensions;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import net.dries007.tfc.util.calendar.CalendarTransaction;
import net.dries007.tfc.util.calendar.Calendars;

import java.util.function.Consumer;

@RemapPrefixForJS(KubeJSTFC.MIXIN_PREFIX)
public interface ICalendarExtension {

    long getTicks();

    @HideFromJS
    CalendarTransaction transaction();

    @Info("Gets the current timestamp-safe tick, a simple alias of #getTicks()")
    default long kubejs_tfc$getTimestamp() {
        return getTicks();
    }

    @Info("Allows for transient modification of the calendar state so that actions may be performed at non-present times")
    default void kubejs_tfc$transaction(Consumer<Transaction> transaction) {
        try (final Transaction tr = new Transaction(transaction())) {
            transaction.accept(tr);
        }
    }

    @Info("If this is the server calendar")
    default boolean kubejs_tfc$isServerCalendar() {
        return equals(Calendars.SERVER);
    }

    final class Transaction implements AutoCloseable {
        private CalendarTransaction transaction;

        Transaction(CalendarTransaction transaction) {
            this.transaction = transaction;
        }

        @Info("Adds the given number of ticks to the calendar within the transaction")
        public void add(long ticks) {
            transaction.add(ticks);
        }

        @Info("Get the number of ticks that have been added in the transaction")
        public long ticks() {
            return transaction.ticks();
        }

        @HideFromJS
        @Override
        public void close() {
            transaction.close();
            transaction = null;
        }
    }
}
