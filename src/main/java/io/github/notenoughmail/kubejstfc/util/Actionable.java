package io.github.notenoughmail.kubejstfc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Actionable<T> {

    private List<Consumer<T>> actions = new ArrayList<>();
    private T actor;

    public void init(T t) {
        actor = t;
        actions.forEach(a -> a.accept(actor));
        actions = null;
    }

    public void queue(Consumer<T> action) {
        if (actions == null) {
            action.accept(actor);
        } else {
            actions.add(action);
        }
    }
}
