package net.survivalboom.survivalboomteleport.commands.cmds;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.survivalboom.survivalboomteleport.configuration.PluginMessages;
import net.survivalboom.survivalboomteleport.placeholders.Placeholders;
import net.survivalboom.survivalboomteleport.teleport.TeleportManager;
import net.survivalboom.survivalboomteleport.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaHereCommand {

    public static void command(@NotNull CommandSender sender, @NotNull String[] args) {

        if (!sender.hasPermission("sbteleport.tpahere")) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("no-permission").replace("{PERMISSION}", "sbteleport.tpahere"));
            return;
        }

        if (!(sender instanceof Player player)) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("only-for-player"));
            return;
        }

        String playerName = Utils.getArrayValue(args, 0);
        if (playerName == null) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("tpahere-usage"));
            return;
        }

        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("player-not-found").replace("{PLAYER}", playerName));
            return;
        }

        if (target.equals(player)) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("tpahere-self"));
            return;
        }

        if (TeleportManager.getCountdown(player, target) != null) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("player-already-teleporting"));
            return;
        }

        TeleportManager.requestToPlayer(target, player, true);

        Placeholders placeholders = new Placeholders();
        placeholders.add("{SENDER}", player.getName());
        placeholders.add("{SENDER-PREFIX}", Utils.getPrefix(player));
        placeholders.add("{SENDER-SUFFIX}", Utils.getSuffix(player));

        Component component = PluginMessages.parse(placeholders.parse(PluginMessages.getMessage("teleport-here-request-msg")));

        TextComponent denyButton;
        denyButton = (TextComponent) PluginMessages.parse(PluginMessages.getMessage("deny-button"));
        TextComponent acceptButton = (TextComponent) PluginMessages.parse(PluginMessages.getMessage("accept-button"));

        ClickEvent denyButtonEvent = ClickEvent.runCommand(String.format("/tpdeny %s", playerName));
        ClickEvent acceptButtonEvent = ClickEvent.runCommand(String.format("/tpaccept %s", playerName));

        HoverEvent<Component> denyButtonHover = HoverEvent.showText(PluginMessages.parse(PluginMessages.getMessage("deny-button-hover")));
        HoverEvent<Component> acceptButtonHover = HoverEvent.showText(PluginMessages.parse(PluginMessages.getMessage("accept-button-hover")));

        denyButton = denyButton.clickEvent(denyButtonEvent).hoverEvent(denyButtonHover);
        acceptButton = acceptButton.clickEvent(acceptButtonEvent).hoverEvent(acceptButtonHover);

        //noinspection UnstableApiUsage
        component = component.replaceText("{BUTTON-ACCEPT}", acceptButton).replaceText("{BUTTON-DENY}", denyButton);

        target.sendMessage(component);

    }

}
