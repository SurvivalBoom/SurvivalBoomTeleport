package net.survivalboom.survivalboomteleport.commands;

import net.survivalboom.survivalboomteleport.SurvivalBoomTeleport;
import net.survivalboom.survivalboomteleport.commands.cmds.*;
import net.survivalboom.survivalboomteleport.configuration.PluginMessages;
import net.survivalboom.survivalboomteleport.utils.Utils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandsHandler implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        try {

            if (label.equals("spawn")) {
                SpawnCommand.command(sender, args);
                return true;
            }

            if (label.equals("tpa")) {
                TpaCommand.command(sender, args);
                return true;
            }

            if (label.equals("tpahere")) {
                TpaHereCommand.command(sender, args);
                return true;
            }

            if (label.equals("tpaccept")) {
                TpaAcceptCommand.command(sender, args);
                return true;
            }

            if (label.equals("tpdeny")) {
                TpaDenyCommand.command(sender, args);
                return true;
            }

            String argument = Utils.getArrayValue(args, 0);
            if (argument == null) {
                sendAbout(sender);
                return true;
            }

            switch (argument) {

                case "reload" -> ReloadCommand.command(sender);

                default -> PluginMessages.sendMessage(sender, PluginMessages.getMessage("help"));

            }

        }

        catch (Exception e) {
            Utils.sendPluginError("Command perform failed with error.", e);
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("command-error"));
        }

        return true;
    }

    private static void sendAbout(@NotNull CommandSender sender) {

        PluginMessages.sendMessage(sender, "&b");
//        PluginMessages.sendMessage(sender, String.format("&#8360C3&lS&#7E65C0&lu&#7A6BBD&lr&#7570BB&lv&#7075B8&li&#6B7AB5&lv&#6780B2&la&#6285B0&ll&#5D8AAD&lB&#5990AA&lo&#5495A7&lo&#4F9AA4&lm&#4A9FA2&lB&#46A59F&le&#41AA9C&ld&#3CAF99&lW&#37B497&la&#33BA94&lr&#2EBF91&ls &7v&2%s &7by &#009FFFTIMURishche", SurvivalBoomBedWars.getVersion()));
        PluginMessages.sendMessage(sender, String.format("&dSurvivalBoomTeleport &7v&2%s &7by &#009FFFTIMURishche", SurvivalBoomTeleport.getVersion()));
        PluginMessages.sendMessage(sender, "&3&l> &7" + SurvivalBoomTeleport.getCompiledFor());
        PluginMessages.sendMessage(sender, "&3&l> &#2ebf91Use &#7570BB/sbteleport help &#2ebf91to see all commands.");
        PluginMessages.sendMessage(sender, "&b");

        if (!(sender instanceof Player player)) return;

        player.playSound(player.getLocation(), Sound.valueOf("ENTITY_ENDER_DRAGON_GROWL"), 10L, 2L);

    }

}
