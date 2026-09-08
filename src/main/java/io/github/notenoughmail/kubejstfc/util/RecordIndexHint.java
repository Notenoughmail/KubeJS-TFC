package io.github.notenoughmail.kubejstfc.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RecordIndexHint {

    /**
     * The script-side parameter index, ignores first parameter {@link dev.latvian.mods.rhino.Context Context} arguments
     */
    int value();
}
