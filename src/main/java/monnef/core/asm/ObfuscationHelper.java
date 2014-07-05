/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.asm;

import monnef.core.MonnefCorePlugin;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

import static monnef.core.MonnefCorePlugin.Log;

public class ObfuscationHelper {
    @Deprecated
    public static final String JAFFAS_MAPPINGS_CFG = "monnef_mappings.ser";

    private static boolean runningInObfuscatedMode = System.getProperty("debugFlag") == null;

    @Deprecated
    private static HashMap<MappedObjectType, MappingDictionary> database;
    @Deprecated
    private static HashMap<MappedObjectType, HashSet<MappedObject>> usedFlags;

    private static boolean initialized = false;

    static {
        database = new HashMap<MappedObjectType, MappingDictionary>();
        usedFlags = new HashMap<MappedObjectType, HashSet<MappedObject>>();
        for (MappedObjectType type : MappedObjectType.values()) {
            database.put(type, new MappingDictionary());
            usedFlags.put(type, new HashSet<MappedObject>());
        }
    }

    public static boolean isRunningInObfuscatedMode() {
        return runningInObfuscatedMode;
    }

    @Deprecated
    public static String getRealNameSlashed(MappedObject toTranslate) {
        return getRealName(toTranslate).replace('.', '/');
    }

    @Deprecated
    public static String getRealName(MappedObject toTranslate) {
        if (!initialized) initialize();

        String translated = database.get(toTranslate.getType()).getFirst(toTranslate.getFullName());
        if (translated == null) {
            printAllDataToLog(database);
            throw new RuntimeException(String.format("Mapping \"%s\" not found!", toTranslate.getFullName()));
        }

        usedFlags.get(toTranslate.getType()).add(toTranslate);

        if (!runningInObfuscatedMode) {
            return toTranslate.getFullName();
        } else {
            return translated;
        }
    }

    @Deprecated
    private static void initialize() {
        if (runningInObfuscatedMode) {
            loadConfigFromJar();
        } else {
            //McpParser.parse(database, PathHelper.getMinecraftPath() + "/../conf/");
            McpParser.parse(database, MonnefCorePlugin.getMcPath() + "/../conf/");
            Log.printInfo("After MCP parser we have " + formatDatabaseStats(database) + ".");
        }

        initialized = true;
    }

    @Deprecated
    private static void loadConfigFromJar() {
        //String myJar = PathHelper.getMyPath();
        String myJar = MonnefCorePlugin.getMyJarPath();
        URL url;
        InputStream inputStream = null;
        try {
            String urlString = "jar:file:" + myJar + "!/" + JAFFAS_MAPPINGS_CFG;
            Log.printFine(String.format("url string: [%s], jar path: [%s]", urlString, myJar));
            url = new URL(urlString);
            inputStream = url.openStream();
            if (inputStream == null) {
                throw new RuntimeException("Unable to get data from my JAR.");
            }
            database = MappingsConfigManager.loadConfig(inputStream);
            Log.printFine("Loaded from mappings config: " + formatDatabaseStats(database) + ".");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Deprecated
    public static void printAllDataToLog() {
        printAllDataToLog(database);
    }

    @Deprecated
    public static void printAllDataToLog(HashMap<MappedObjectType, MappingDictionary> database) {
        for (MappedObjectType type : MappedObjectType.values()) {
            Log.printFine(type.toString() + ":");
            MappingDictionary collection = database.get(type);
            for (String key : collection.keySet()) {
                Log.printFine(String.format("[%s] -> [%s]", key, collection.getAll(key)));
            }
        }
    }

    @Deprecated
    private static String formatDatabaseStats(HashMap<MappedObjectType, MappingDictionary> database) {
        return database.get(MappedObjectType.METHOD).countKeys() + " methods, " +
                database.get(MappedObjectType.CLASS).countKeys() + " classes, " +
                database.get(MappedObjectType.FIELD).countKeys() + " fields";
    }

    @Deprecated
    public static void dumpUsedItemsToConfig() {
        //String path = PathHelper.getMinecraftPath() + "/../bin_data/" + JAFFAS_MAPPINGS_CFG;
        String path = MonnefCorePlugin.getMcPath() + "/../bin_data/" + JAFFAS_MAPPINGS_CFG;
        try {
            OutputStream output = new FileOutputStream(path);
            HashMap<MappedObjectType, MappingDictionary> usedOnlyDatabase = constructOnlyUsed(database, usedFlags);
            MappingsConfigManager.saveConfig(usedOnlyDatabase, output);

            Log.printFine(String.format("Saved items to \"%s\":", path));
            printAllDataToLog(usedOnlyDatabase);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    private static HashMap<MappedObjectType, MappingDictionary> constructOnlyUsed(HashMap<MappedObjectType, MappingDictionary> database, HashMap<MappedObjectType, HashSet<MappedObject>> usedFlags) {
        HashMap<MappedObjectType, MappingDictionary> ret = new HashMap<MappedObjectType, MappingDictionary>();

        // construct from all enum items
        for (MappedObjectType type : MappedObjectType.values()) {
            MappingDictionary entry = new MappingDictionary();
            ret.put(type, entry);
        }

        for (MappedObject obj : MappedObject.values()) {
            MappedObjectType type = obj.getType();
            String fullName = obj.getFullName();

            MappingDictionary entry = ret.get(type);
            MappingDictionary data = database.get(type);
            entry.put(fullName, data.get(fullName));
        }

        return ret;
    }

    @Deprecated
    public static boolean namesAreEqual(String name, MappedObject toTranslate) {
        if (!initialized) initialize();

        usedFlags.get(toTranslate.getType()).add(toTranslate);

        return database.get(toTranslate.getType()).canBeTranslatedTo(toTranslate.getFullName(), name);
    }
}
