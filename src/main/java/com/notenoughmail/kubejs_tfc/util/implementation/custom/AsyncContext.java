package com.notenoughmail.kubejs_tfc.util.implementation.custom;

import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.ContextAccessor;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.*;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

// Probably a bad idea, may make obscure things explode
public class AsyncContext extends Context {

    public static Supplier<Context> DEFAULT = Lazy.of(() -> enter(ScriptType.STARTUP.manager.get().context));

    public static <T> void wrapFunctionalInterfaceAsync(TypeWrappers typeWrappers, Class<T> type) {
        typeWrappers.registerSimple(type, o -> o instanceof BaseFunction, o ->{
            final BaseFunction function = (BaseFunction) o;
            return (T) NativeJavaObject.createInterfaceAdapter(DEFAULT.get(), type, function);
        });
    }

    public static AsyncContext enter(Context ctx) {
        if (ctx instanceof AsyncContext async) {
            return async;
        }
        final AsyncContext context = new AsyncContext();
        final ScriptType type = ctx.getProperty("Type", ScriptType.STARTUP);

        context.setProperty("Type", type);
        context.setProperty("Console", ctx.getProperty("Console"));
        context.setClassShutter(ctx.getClassShutter());
        context.setRemapper(ctx.getRemapper());
        context.setApplicationClassLoader(ctx.getApplicationClassLoader());
        context.setTopCall(context.getTopCallScope());
        context.setWrapFactory(ctx.getWrapFactory());
        final ContextAccessor access = ((ContextAccessor) context);
        access.kubejs_tfc$SetTypeWrappers(ctx.getTypeWrappers());
        access.kubejs_tfc$GetReverseWrappers().addAll(((ContextAccessor) ctx).kubejs_tfc$GetReverseWrappers());

        return context;
    }

    private final ThreadLocal<Scriptable> storedScriptable = new ThreadLocal<>();

    @Override
    public Object callSync(Callable callable, Scriptable scope, Scriptable thisObj, Object[] args) {
        return callable.call(this, scope, thisObj, args);
    }

    @Override
    public Scriptable lastStoredScriptable() {
        final Scriptable value = storedScriptable.get();
        storedScriptable.remove();
        return value;
    }

    @Override
    public void storeScriptable(Scriptable value) {
        if (storedScriptable.get() != null) {
            throw new IllegalStateException("Tried to overwrite stored scriptable object");
        }
        storedScriptable.set(value);
    }
}
