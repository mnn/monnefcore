/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.command;

import monnef.core.common.GuiHandler;
import monnef.core.mod.MonnefCoreNormalMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandMC extends CommandBase {
    @Override
    public String getCommandName() {
        return "mc";
    }

    @Override
    public void processCommand(ICommandSender commandsender, String[] parameters) {
        if (parameters.length <= 0) {
            commandsender.sendChatToPlayer("monnef core");
        } else if (parameters.length == 1 && parameters[0].equals("exporter")) {
            if (commandsender instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) commandsender;
                player.openGui(MonnefCoreNormalMod.instance, GuiHandler.GuiId.EXPORTER.ordinal(), player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            } else {
                commandsender.sendChatToPlayer("You're not an entity, aborting.");
            }
        } else {
            commandsender.sendChatToPlayer("Unknown sub-command.");
        }
    }
}
