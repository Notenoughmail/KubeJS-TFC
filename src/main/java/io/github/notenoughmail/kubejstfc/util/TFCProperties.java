package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.util.BaseProperties;
import net.neoforged.fml.loading.FMLLoader;

import java.nio.file.Path;
import java.util.function.Consumer;

public class TFCProperties extends BaseProperties {

    private static final Path PATH = KubeJSPaths.CONFIG.resolve("tfc.json");

    private static TFCProperties INSTANCE;

    public static TFCProperties get() {
        if (INSTANCE == null) {
            INSTANCE = new TFCProperties();
        }
        return INSTANCE;
    }

    public static TFCProperties reload() {
        INSTANCE = new TFCProperties();
        return INSTANCE;
    }

    public static boolean debug() {
        return !FMLLoader.isProduction() || get().debug;
    }

    public void print(Consumer<String> info) {
        info.accept(toString());
        info.accept("- Debug mode enabled: %s".formatted(debug));
        info.accept("- Self tests console insertion enabled: %s".formatted(insertIntoConsole));
        info.accept("- Self tests warnings deduplicated: %s".formatted(deduplicateSelfTestWarnings));
        info.accept("- Data manager warnings deduplicated: %s".formatted(deduplicateDataManagerWarnings));
    }

    public boolean debug, insertIntoConsole, deduplicateSelfTestWarnings, deduplicateDataManagerWarnings;

    public TFCProperties() {
        super(PATH, "KubeJS TFC Configuration");
    }

    @Override
    protected void load() {
        debug = get("debug", false);
        insertIntoConsole = get("insert_into_console", true);
        deduplicateSelfTestWarnings = get("deduplicate_self_test_warnings", true);
        deduplicateDataManagerWarnings = get("deduplicate_data_manager_warnings", true);

    }
}
