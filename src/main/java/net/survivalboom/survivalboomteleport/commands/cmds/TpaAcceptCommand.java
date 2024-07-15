package net.survivalboom.survivalboomteleport.commands.cmds;

import net.survivalboom.survivalboomteleport.configuration.PluginMessages;
import net.survivalboom.survivalboomteleport.teleport.TeleportManager;
import net.survivalboom.survivalboomteleport.teleport.TeleportRequest;
import net.survivalboom.survivalboomteleport.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaAcceptCommand {

    public static void command(@NotNull CommandSender sender, @NotNull String[] args) {

        if (!sender.hasPermission("sbteleport.tpaccept")) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("no-permission").replace("{PERMISSION}", "sbteleport.tpaccept"));
            return;
        }

        if (!(sender instanceof Player player)) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("only-for-player"));
            return;
        }

        String playerName = Utils.getArrayValue(args, 0);

        TeleportRequest request;
        if (playerName == null) request = TeleportManager.getRequest(player, null);
        else {
            Player from = Bukkit.getPlayer(playerName);
            if (from == null) {
                PluginMessages.sendMessage(sender, PluginMessages.getMessage("player-not-found").replace("{PLAYER}", playerName));
                return;
            }
            request = TeleportManager.getRequest(player, from);
        }

        if (playerName != null && request == null) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("no-request-from-player").replace("{PLAYER}", playerName));
            return;
        }

        if (playerName == null && request == null) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("no-requests"));
            return;
        }

        if (TeleportManager.getCountdown(player, null) != null) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("already-teleporting"));
            return;
        }

        request.accept();

    }

}
