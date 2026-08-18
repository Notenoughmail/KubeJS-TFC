package io.github.notenoughmail.kubejstfc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Actionable<T> implements Consumer<T> {

    private List<Consumer<T>> actions = new ArrayList<>();
    private T actor;

    public void queue(Consumer<T> action) {
        if (actions == null) {
            action.accept(actor);
        } else {
            actions.add(action);
        }
    }

    @Override
    public void accept(T t) {
        if (actor == null) {
            actor = t;
            actions.forEach(a -> a.accept(actor));
            actions = null;
        }
    }
}
