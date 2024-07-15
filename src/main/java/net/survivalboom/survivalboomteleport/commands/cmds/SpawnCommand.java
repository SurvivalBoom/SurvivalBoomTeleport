package net.survivalboom.survivalboomteleport.commands.cmds;

import net.survivalboom.survivalboomteleport.configuration.PluginMessages;
import net.survivalboom.survivalboomteleport.teleport.TeleportManager;
import net.survivalboom.survivalboomteleport.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand {

    public static void command(@NotNull CommandSender sender, @NotNull String[] args) {

        if (TeleportManager.getSpawnLocation() == null) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("spawn-not-set"));
            return;
        }

        String argument = Utils.getArrayValue(args, 0);
        if (argument != null && !sender.hasPermission("sbteleport.spawn.other")) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("no-permission").replace("{PERMISSION}", "sbteleport.spawn.other"));
            return;
        }

        if (!(sender instanceof Player) && argument == null) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("only-for-player"));
            return;
        }

        if (argument == null) {

            if (TeleportManager.getCountdown((Player) sender, null) != null) {
                PluginMessages.sendMessage(sender, PluginMessages.getMessage("already-teleporting"));
                return;
            }

            TeleportManager.requestToSpawn((Player) sender);
            return;
        }

        Player player = Bukkit.getPlayer(argument);
        if (player == null) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("player-not-found").replace("{PLAYER}", player.getName()));
            return;
        }

        if (TeleportManager.getCountdown(player, null) != null) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("player-already-teleporting"));
            return;
        }

        if (player.equals(sender)) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("spawn-self"));
            return;
        }

        TeleportManager.requestToSpawn(player);
        PluginMessages.sendMessage(sender, PluginMessages.getMessage("you-moved-to-spawn").replace("{PLAYER}", player.getName()));

    }

}
