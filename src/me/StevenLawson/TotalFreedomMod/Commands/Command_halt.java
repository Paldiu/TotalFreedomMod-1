package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
import me.StevenLawson.TotalFreedomMod.TFM_SuperadminList;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SUPER, source = SourceType.BOTH)
@CommandParameters(description = "Halts a player", usage = "/<command> <<partialname> | all | purge | list>")
public class Command_halt extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (args[0].equalsIgnoreCase("all"))
        {
            TFM_Util.adminAction(sender.getName(), "Halting all non-superadmins.", true);
            int counter = 0;
            for (Player p : server.getOnlinePlayers())
            {
                if (!TFM_SuperadminList.isUserSuperadmin(p))
                {
                    TFM_PlayerData.getPlayerData(p).setHalted(true);
                    counter++;
                }
            }
            playerMsg("Halted " + counter + " players.");
            return true;
        }

        if (args[0].equalsIgnoreCase("purge"))
        {
            TFM_Util.adminAction(sender.getName(), "Unhalting all players.", true);
            int counter = 0;
            for (Player p : server.getOnlinePlayers())
            {
                TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(p);
                if (TFM_PlayerData.getPlayerData(p).isHalted())
                {
                    playerdata.setHalted(false);
                    counter++;
                }
            }
            playerMsg("Unhalted " + counter + " players.");
            return true;
        }

        if (args[0].equalsIgnoreCase("list"))
        {
            TFM_PlayerData info;
            int count = 0;
            for (Player hp : server.getOnlinePlayers())
            {
                info = TFM_PlayerData.getPlayerData(hp);
                if (info.isHalted())
                {
                    if (count == 0)
                    {
                        playerMsg(sender, "Halted players:");
                    }
                    playerMsg("- " + hp.getName());
                    count++;
                }
            }
            if (count == 0)
            {
                playerMsg("There are currently no halted players.");
            }
            return true;
        }

        Player p;
        try
        {
            p = getPlayer(args[0]);
        }
        catch (CantFindPlayerException ex)
        {
            sender.sendMessage(ex.getMessage());
            return true;
        }


        TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(p);
        if (!playerdata.isHalted())
        {
            TFM_Util.adminAction(sender.getName(), "Halting " + p.getName(), true);
            playerdata.setHalted(true);
            return true;
        }
        else
        {
            TFM_Util.adminAction(sender.getName(), "Unhalting " + p.getName(), true);
            playerdata.setHalted(false);
            return true;
        }
    }
}
