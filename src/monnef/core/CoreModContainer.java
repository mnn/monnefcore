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
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import monnef.core.asm.CoreTransformer;
import monnef.core.asm.ObfuscationHelper;
import monnef.core.calendar.CoreTickHandler;

import static monnef.core.MonnefCorePlugin.Log;
import static monnef.core.MonnefCorePlugin.debugEnv;

public class CoreModContainer extends DummyModContainer {
    private static CoreModContainer instance;

    public CoreModContainer() {
        super(new ModMetadata());
        ModMetadata myMeta = super.getMetadata();
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
    public void onStart(FMLInitializationEvent event) {
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side == Side.CLIENT) {
            CloakHookHandler.registerCloakHandler(new CustomCloaksHandler());

            if (!CoreTransformer.cloakHookApplied) {
                printDebugDataAndCrash("Unable to install a cloak hook!");
            }
        }

        if (!CoreTransformer.lightningHookApplied) {
            printDebugDataAndCrash("Unable to install a lightning hook!");
        }

        if (MonnefCorePlugin.debugEnv && MonnefCorePlugin.jaffasEnv) {
            ObfuscationHelper.dumpUsedItemsToConfig();
        }

        CoreTickHandler tickHandler = new CoreTickHandler();
        TickRegistry.registerTickHandler(tickHandler, Side.CLIENT);
        TickRegistry.registerTickHandler(tickHandler, Side.SERVER);

        MonnefCorePlugin.initialized = true;
        if (debugEnv) doDebuggingThings();
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
