package net.survivalboom.survivalboomteleport;

import net.survivalboom.survivalboomteleport.commands.CommandsHandler;
import net.survivalboom.survivalboomteleport.commands.TabCompleteHandler;
import net.survivalboom.survivalboomteleport.configuration.PluginMessages;
import net.survivalboom.survivalboomteleport.teleport.TeleportManager;
import net.survivalboom.survivalboomteleport.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class SurvivalBoomTeleport extends JavaPlugin {

    private static SurvivalBoomTeleport plugin;
    private static boolean forceStop = false;
    private static final String COMPILED_FOR = "Public Use. Compiled By TIMURishche";

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        try {

            sendSplash();

            PluginMessages.consoleSend("&b>> &fChecking files...");
            checkFiles(false);

            PluginMessages.consoleSend("&b>> &fLoading configuration...");
            getConfig().load(new File(getDataFolder(), "config.yml"));
            PluginMessages.reload(new File(getDataFolder(), "messages.yml"));

            PluginMessages.consoleSend("&b>> &fRegistering plugin components...");
            TeleportManager.init();
            PluginCommand command = getCommand("survivalboomteleport");
            command.setTabCompleter(new TabCompleteHandler());
            command.setExecutor(new CommandsHandler());

            PluginMessages.consoleSend("&a>> &fPlugin &aSurvivalBoomTeleport &aenabled successfully!");

        }

        catch (Exception e) {
            PluginMessages.consoleSend("&c&l! &fOopsie! Looks like &cSurvivalBoomTeleport &fjust crashed! Please report error below to the developer. Sowwy :(");
            Utils.sendStackTrace(e);
            forceStop();
        }


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (forceStop) return;
        PluginMessages.consoleSend("&c>> &fPlugin &cSurvivalBoomTeleport &fdisabled!");
    }

    public static void sendSplash() {

        PluginMessages.consoleSend("&b");
        PluginMessages.consoleSend("&b   _____                  _            _ &3____");
        PluginMessages.consoleSend("&b  / ____|                (_)          | &3|  _ \\                       ");
        PluginMessages.consoleSend("&b | (___  _   _ _ ____   _____   ____ _| &3| |_) | ___   ___  _ __ ___   ");
        PluginMessages.consoleSend("&b  \\___ \\| | | | '__\\ \\ / / \\ \\ / / _` | &3|  _ < / _ \\ / _ \\| '_ ` _ \\ ");
        PluginMessages.consoleSend("&b  ____) | |_| | |   \\ V /| |\\ V / (_| | &3| |_) | (_) | (_) | | | | | | ");
        PluginMessages.consoleSend("&b |_____/ \\__,_|_|    \\_/ |_| \\_/ \\__,_|_&3|____/ \\___/ \\___/|_| |_| |_|   ");
        PluginMessages.consoleSend("    &dSurvivalBoom Network &8| &fBy &bTIMURishche &8|  &dSurvivalBoomTeleport &fv&3{VERSION}".replace("{VERSION}", getVersion()));

    }

    public static void forceStop() {
        forceStop = true;
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    @NotNull
    public static SurvivalBoomTeleport getPlugin() {
        return plugin;
    }

    @NotNull
    public static String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @NotNull
    public static String getCompiledFor() {
        return COMPILED_FOR;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void checkFiles(boolean silent) {

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdir();

        String[] files = {"config.yml", "messages.yml"};
        for (String file : files) if (Utils.copyPluginFile(file) && !silent) PluginMessages.consoleSend(String.format("&3>> &fCreated &3%s", file));

    }

}
