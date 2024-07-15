package net.survivalboom.survivalboomteleport.commands.cmds;

import net.survivalboom.survivalboomteleport.SurvivalBoomTeleport;
import net.survivalboom.survivalboomteleport.configuration.PluginMessages;
import net.survivalboom.survivalboomteleport.teleport.TeleportManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ReloadCommand {

    public static void command(@NotNull CommandSender sender) throws IOException, InvalidConfigurationException {

        if (!sender.hasPermission("sbteleport.reload")) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("no-permission").replace("{PERMISSION}", "sbteleport.reload"));
            return;
        }

        File dataFolder = SurvivalBoomTeleport.getPlugin().getDataFolder();
        PluginMessages.reload(new File(dataFolder, "messages.yml"));
        SurvivalBoomTeleport.getPlugin().getConfig().load(new File(dataFolder, "config.yml"));

        TeleportManager.reload();

        PluginMessages.sendMessage(sender, PluginMessages.getMessage("reloaded"));

    }

}
