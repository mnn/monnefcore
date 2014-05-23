/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core;

import monnef.core.asm.PathHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import static monnef.core.MonnefCorePlugin.Log;

public class Config {
    public static final String SKIP_LIBRARY_DOWNLOAD_TAG = "skipLibraryDownload";
    public static final String CONFIG_DIR = "config";
    public static final String USE_ASM_LIGHTNING = "useASMLightning";
    public static final String EXPORTER_ENABLED = "exporterEnabled";
    public static final String CORE_COMMAND_ENABLED = "coreCommandEnabled";
    public static final String DISABLE_CLOAKS_WITH_SHADERS = "disableCloaksWithShadersMod";

    public static final String FALSE_VALUE = Boolean.FALSE.toString().toLowerCase();
    public static final String TRUE_VALUE = Boolean.TRUE.toString().toLowerCase();

    private static boolean initialized = false;
    private static boolean useASMLightning;
    private static boolean exporterEnabled;
    private static boolean commandEnabled;
    private static boolean disableCloaksWithShaders;

    public static boolean areDisabledCloaksWithShaders() {
        return disableCloaksWithShaders;
    }

    public static boolean useOldASMLightning() {
        assertLoadedConfig();
        return useASMLightning;
    }

    public static boolean isExporterEnabled() {
        assertLoadedConfig();
        return exporterEnabled;
    }

    public static boolean isCommandEnabled() {
        assertLoadedConfig();
        return commandEnabled;
    }

    public static void assertLoadedConfig() {
        if (!initialized) {
            throw new RuntimeException("config not loaded");
        }
    }

    static void handleConfig() {
        if (initialized) {
            throw new RuntimeException("already initialized");
        }
        initialized = true;

        //String fullPathToConfig = PathHelper.getMinecraftPath() + "/" + CONFIG_DIR;
        String fullPathToConfig = MonnefCorePlugin.getMcPath() + "/" + CONFIG_DIR;
        String configFileName = fullPathToConfig + "/" + Reference.ModId + ".cfg";
        Log.printFine("Opening config file: \"" + configFileName + "\"");
        File config = new File(configFileName);
        InputStream inputStream = null;

        boolean fileIsReady = false;
        if (config.exists()) {
            if (!config.isFile()) {
                throw new RuntimeException("\"" + config + "\" is not a file.");
            }

            try {
                inputStream = new FileInputStream(config);
                fileIsReady = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Log.printInfo("Config file not found. Creating one.");
            OutputStream outputStream;
            PathHelper.createDirIfNecessary(fullPathToConfig);
            try {
                outputStream = new FileOutputStream(configFileName);
                Properties prop = new Properties();
                initConfig(prop);
                prop.store(outputStream, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.printWarning("Problem occurred in default config writing.");
            } catch (IOException e) {
                e.printStackTrace();
                Log.printWarning("Problem occurred in default config writing.");
            }
        }

        if (fileIsReady) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            try {
                Properties prop = new Properties();
                prop.load(inputStream);
                processConfig(prop);
            } catch (IOException e) {
                e.printStackTrace();
                Log.printWarning("Problem occurred parsing config.");
            }

        }
    }

    private static void processConfig(Properties prop) {
        String skipString = prop.getProperty(SKIP_LIBRARY_DOWNLOAD_TAG, "");

        if (!skipString.isEmpty()) {
            String[] skipList = skipString.split(", ?");
            for (String toSkip : skipList) {
                Library.tryDisableLibrary(toSkip.toLowerCase());
            }
        }

        useASMLightning = processBoolValue(prop, USE_ASM_LIGHTNING);
        exporterEnabled = processBoolValue(prop, EXPORTER_ENABLED);
        commandEnabled = processBoolValue(prop, CORE_COMMAND_ENABLED, TRUE_VALUE);
        disableCloaksWithShaders = processBoolValue(prop, DISABLE_CLOAKS_WITH_SHADERS);
    }

    private static boolean processBoolValue(Properties prop, String key) {
        return processBoolValue(prop, key, FALSE_VALUE);
    }

    private static boolean processBoolValue(Properties prop, String key, String defaultValue) {
        return TRUE_VALUE.equals(prop.getProperty(key, defaultValue).toLowerCase());
    }

    private static void initConfig(Properties prop) {
        prop.setProperty(SKIP_LIBRARY_DOWNLOAD_TAG, "");
        prop.setProperty(USE_ASM_LIGHTNING, FALSE_VALUE);
        prop.setProperty(EXPORTER_ENABLED, FALSE_VALUE);
        prop.setProperty(CORE_COMMAND_ENABLED, TRUE_VALUE);
        prop.setProperty(DISABLE_CLOAKS_WITH_SHADERS, FALSE_VALUE);
    }
}
