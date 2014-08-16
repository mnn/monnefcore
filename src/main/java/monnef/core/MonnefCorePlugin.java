/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import monnef.core.asm.ObfuscationHelper;
import monnef.core.utils.CustomLogger;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.Map;

import static cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import static cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@MCVersion
@TransformerExclusions({MonnefCorePlugin.CORE_NAMESPACE + ".asm", MonnefCorePlugin.CORE_NAMESPACE + ".asm.cloakHook"})
public class MonnefCorePlugin implements IFMLLoadingPlugin, IFMLCallHook {
    public static final String CORE_NAMESPACE = "monnef.core";
    public static final String COREMOD_LOCATION_TAG = "coremodLocation";
    public static CustomLogger Log = new CustomLogger("mC");
    public static boolean debugEnv = !ObfuscationHelper.isRunningInObfuscatedMode();
    public static boolean jaffasEnv = System.getProperty("jaffasFlag") != null;
    static boolean initialized = false;

    private static final String CLASS_LOADER_TAG = "classLoader";
    private static final String MC_LOCATION_TAG = "mcLocation";
    private static String mcPath;
    private static String myJarPath;

    public static String getMcPath() {
        if (!isMcPathInitialized()) throw new RuntimeException("MC path not set!");
        return mcPath;
    }

    public static boolean isMcPathInitialized() {
        return mcPath != null;
    }

    public static String getMyJarPath() {
        return myJarPath;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void markInitialized() {
        if (initialized) CoreModContainer.printDebugDataAndCrash("re-initialization of core");
        Log.printInfo("Initialized.");
        initialized = true;
    }

    public static LaunchClassLoader classLoader;

    public MonnefCorePlugin() {
    }

    @Override
    public String getAccessTransformerClass() {
        return CORE_NAMESPACE + ".asm.CoreTransformer";
    }

    @Override
    // TODO: check if we can't use getAccessTransformerClass instead (after deobf access? we could get rid of the whole McpParser and that problematic version-sensitive binary config file)
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return CORE_NAMESPACE + ".CoreModContainer";
    }

    @Override
    public String getSetupClass() {
        // return CORE_NAMESPACE + ".MonnefCorePlugin";
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        if (data.containsKey(CLASS_LOADER_TAG)) {
            // used?
            classLoader = (LaunchClassLoader) data.get(CLASS_LOADER_TAG);
        }
        if (data.containsKey(MC_LOCATION_TAG)) {
            mcPath = ((File) data.get(MC_LOCATION_TAG)).getAbsolutePath();
        } else {
            throw new RuntimeException("Unable to get Minecraft path.");
        }
        if (data.containsKey(COREMOD_LOCATION_TAG)) {
            myJarPath = ((File) data.get(COREMOD_LOCATION_TAG)).getAbsolutePath();
        } else {
            throw new RuntimeException("Unable to get location of my jar.");
        }
        Log.printFinest(String.format("Injected data received - mcPath = \"%s\"", mcPath));
        Config.handleConfig();
    }

    @Override
    public Void call() throws Exception {
        MonnefCorePlugin.Log.printInfo("monnef's Core initialized, version: " + Reference.Version);
        return null;
    }
}
