/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.mod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import monnef.core.Config;
import monnef.core.MonnefCorePlugin;
import monnef.core.Reference;
import monnef.core.block.BlockMonnefCore;
import monnef.core.client.ExporterTickHandler;
import monnef.core.client.SashRegistry;
import monnef.core.command.CommandMC;
import monnef.core.common.CommonProxy;
import monnef.core.common.GuiHandler;
import monnef.core.network.CorePacketHandlerMC17;
import monnef.core.network.CorePacketHandlerTrait;
import monnef.core.power.PowerValues$;
import monnef.core.utils.BreakableIronMaterial;
import monnef.core.utils.ClassHelper;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = Reference.ModIdHelper, name = Reference.ModNameHelper, version = Reference.Version, dependencies = "required-after:monnef-core")
public class MonnefCoreNormalMod {
    @SidedProxy(clientSide = "monnef.core.client.ClientProxy", serverSide = "monnef.core.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(Reference.ModIdHelper)
    public static MonnefCoreNormalMod instance;
    public static int renderID;

    public static CorePacketHandlerTrait packetHandler = CorePacketHandlerMC17.instance();

    public static Configuration config;
    public static SashRegistry sashRegistry;

    @Mod.EventHandler
    public void preLoad(FMLPreInitializationEvent evt) {
        handleMetadata();
        proxy.registerContainers();
        packetHandler.onPreLoad();

        config = new Configuration(evt.getSuggestedConfigurationFile());
        PowerValues$.MODULE$.coreConfigPowerGenerationCoef_$eq((float) config.get(Configuration.CATEGORY_GENERAL, "powerGenerationCoef", 1d).getDouble(1));
        PowerValues$.MODULE$.coreConfigPowerConsumptionCoef_$eq((float) config.get(Configuration.CATEGORY_GENERAL, "powerConsumptionCoef", 1d).getDouble(1));
        config.save();
        sashRegistry = new SashRegistry();
        sashRegistry.init();
        if (!ClassHelper.isClassPresent("cofh.api.energy.IEnergyProvider")) {
            MonnefCorePlugin.Log.printSevere("Missing or potentially incompatible Redstone Flux API.");
        }
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent evt) {
        if (Config.isExporterEnabled() && Config.isCommandEnabled()) {
            NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
            FMLCommonHandler.instance().bus().register(new ExporterTickHandler());
            MonnefCorePlugin.Log.printInfo("Exporter initialized.");
        }

        proxy.registerClientStuff();
    }

    @Mod.EventHandler
    public void postLoad(FMLPostInitializationEvent evt) {
        BreakableIronMaterial.onPostLoad();
        BlockMonnefCore.onPostLoad();
        packetHandler.onPostLoad();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (Config.isCommandEnabled()) {
            event.registerServerCommand(new CommandMC());
        }
    }

    private void handleMetadata() {
        ModContainer container = FMLCommonHandler.instance().findContainerFor(this);
        ModMetadata metaData = container.getMetadata();
        metaData.parent = Reference.ModId;
    }
}
