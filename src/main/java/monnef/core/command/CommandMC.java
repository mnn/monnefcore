/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.command;

import monnef.core.Config;
import monnef.core.common.GuiHandler;
import monnef.core.mod.MonnefCoreNormalMod;
import monnef.core.utils.DyeHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import static monnef.core.utils.PlayerHelper.addMessage;

public class CommandMC extends CommandBase {
    @Override
    public String getCommandName() {
        return "mc";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "commands.mc.usage";
    }

    @Override
    public void processCommand(ICommandSender commandsender, String[] parameters) {
        if (parameters.length <= 0) {
            addMessage(commandsender, "monnef core");
        } else if (parameters.length == 1 && parameters[0].equals("exporter")) {
            if (Config.isExporterEnabled()) {
                if (commandsender instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) commandsender;
                    player.openGui(MonnefCoreNormalMod.instance, GuiHandler.GuiId.EXPORTER.ordinal(), player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
                } else {
                    addMessage(commandsender, "You're not an entity, aborting.");
                }
            } else {
                addMessage(commandsender, "Exporter is disabled in a config file.");
            }
        } else if (parameters.length == 2 && parameters[0].equals("debug")) {
            if (parameters[1].equals("dumpColors")) {
                addMessage(commandsender, DyeHelper.compileColorList());
            } else {
                addMessage(commandsender, "Unknown debug sub-command.");
            }
        } else {
            addMessage(commandsender, "Unknown sub-command.");
        }
    }
}
