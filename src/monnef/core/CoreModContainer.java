/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import monnef.core.asm.CoreTransformer;
import monnef.core.asm.ObfuscationHelper;
import monnef.core.calendar.CoreTickHandler;
import monnef.core.utils.WolfFoodRegistry;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

import static monnef.core.MonnefCorePlugin.Log;
import static monnef.core.MonnefCorePlugin.debugEnv;

public class CoreModContainer extends DummyModContainer {
    private static CoreModContainer instance;
    private static ModMetadata myMeta;

    public CoreModContainer() {
        super(new ModMetadata());
        myMeta = super.getMetadata();
        myMeta.authorList = Reference.Authors;
        myMeta.description = "The core library used by " + Reference.MONNEF + "'s mods.";
        myMeta.modId = Reference.ModId;
        myMeta.version = Reference.Version;
        myMeta.name = Reference.ModName;

        if (instance != null) {
            Log.printWarning("multiple container creation?");
        }

        instance = this;
    }

    public static void registerModHelper(Object mod) {
        myMeta.childMods = new ArrayList<ModContainer>();
        myMeta.childMods.add(FMLCommonHandler.instance().findContainerFor(mod));
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    public static CoreModContainer instance() {
        return instance;
    }

    // use google subscribe and FML events
    @Subscribe
    public void load(FMLInitializationEvent event) {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        // TODO: solve cloak stuff
        /*
        if (side == Side.CLIENT) {
            CloakHookHandler.registerCloakHandler(new CustomCloaksHandler());

            if (!CoreTransformer.cloakHookApplied) {
                printDebugDataAndCrash("Unable to install a cloak hook!");
            }
        }
        */

        if (!CoreTransformer.lightningHookApplied) {
            printDebugDataAndCrash("Unable to install a lightning hook!");
        }

        if (MonnefCorePlugin.debugEnv && MonnefCorePlugin.jaffasEnv) {
            ObfuscationHelper.dumpUsedItemsToConfig();
        }

        CoreTickHandler tickHandler = new CoreTickHandler();
        TickRegistry.registerTickHandler(tickHandler, Side.CLIENT);
        TickRegistry.registerTickHandler(tickHandler, Side.SERVER);

        MinecraftForge.EVENT_BUS.register(new WolfFoodRegistry());

        MonnefCorePlugin.initialized = true;
        if (debugEnv) doDebuggingThings();
        MonnefCorePlugin.Log.printInfo("Final checks passed.");
    }

    private void printDebugDataAndCrash(String msg) {
        Log.printSevere(msg);
        Log.printFine("Mapping database:");
        ObfuscationHelper.printAllDataToLog();
        throw new RuntimeException(msg);
    }

    private void doDebuggingThings() {
    }
}
