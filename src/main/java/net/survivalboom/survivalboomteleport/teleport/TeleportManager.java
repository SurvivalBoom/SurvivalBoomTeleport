package net.survivalboom.survivalboomteleport.teleport;

import com.earth2me.essentials.spawn.EssentialsSpawn;
import net.survivalboom.survivalboomteleport.SurvivalBoomTeleport;
import net.survivalboom.survivalboomteleport.configuration.PluginMessages;
import net.survivalboom.survivalboomteleport.placeholders.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeleportManager implements Listener {

    private static TeleportCountdownTemplate teleportCountdown = null;
    private static TeleportCountdownTemplate spawnCountdown = null;

    private static boolean cancelTeleportMove = false;
    private static boolean cancelSpawnMove = false;

    private static final List<TeleportRequest> requests = new ArrayList<>();
    private static final List<TeleportCountdown> countdowns = new ArrayList<>();

    public static void init() {
        reload();
        Bukkit.getPluginManager().registerEvents(new TeleportManager(), SurvivalBoomTeleport.getPlugin());
    }

    public static void reload() {

        Configuration section = SurvivalBoomTeleport.getPlugin().getConfig();

        ConfigurationSection teleportSection = section.getConfigurationSection("tpa-teleport");
        if (teleportSection != null) {
            teleportCountdown = new TeleportCountdownTemplate(teleportSection);
            cancelTeleportMove = teleportSection.getBoolean("cancel-on-move");
        }
        else PluginMessages.consoleSend("&e&l! &fSection 'tpa-teleport' not found.");

        ConfigurationSection spawnSection = section.getConfigurationSection("spawn-teleport");
        if (spawnSection != null) spawnCountdown = new TeleportCountdownTemplate(spawnSection);
        else PluginMessages.consoleSend("&e&l! &fSection 'spawn-teleport' not found.");

    }

    public static boolean isTeleport() {
        return teleportCountdown != null;
    }

    public static boolean isSpawn() {
        return spawnCountdown != null;
    }

    public static void requestToPlayer(@NotNull Player from, @NotNull Player to, boolean inverted) {
        if (!isTeleport()) throw new IllegalStateException("/tpa is disabled");
        if (getCountdown(from, null) != null) throw new IllegalStateException(String.format("Player %s has countdown", from.getName()));

        TeleportRequest request;
        if (!inverted) request = new TeleportRequest(from, to, false);
        else request = new TeleportRequest(to, from, true);

        Placeholders placeholders = new Placeholders();
        placeholders.add("{SENDER}", request.getFrom().getName());
        placeholders.add("{TARGET}", request.getTo().getName());
        teleportCountdown.performIfExists("REQUESTED", to, from, placeholders);

        requests.add(request);
    }

    public static TeleportCountdown requestToSpawn(@NotNull Player player) {
        if (!isSpawn()) throw new IllegalStateException("/spawn is disabled");
        if (getCountdown(player, null) != null) throw new IllegalStateException(String.format("Player %s has countdown", player.getName()));
        return spawnCountdown.start(player, null);
    }

    public static TeleportCountdown acceptTeleport(@NotNull TeleportRequest request) {
        requests.remove(request);

        Player from = request.getFrom();
        Player to = request.getTo();

        Placeholders placeholders = new Placeholders();
        placeholders.add("{SENDER}", from.getName());
        placeholders.add("{TARGET}", to.getName());
        teleportCountdown.performIfExists("ACCEPTED", from, to, placeholders);

        return teleportCountdown.start(from, to);
    }

    public static void declineTeleport(@NotNull TeleportRequest request) {
        requests.remove(request);

        Player from = request.getFrom();
        Player to = request.getTo();

        Placeholders placeholders = new Placeholders();
        placeholders.add("{SENDER}", from.getName());
        placeholders.add("{TARGET}", to.getName());
        teleportCountdown.performIfExists("DENIED", from, to, placeholders);
    }

    public static List<TeleportRequest> getRequests() {
        return new ArrayList<>(requests);
    }

    public static List<TeleportCountdown> getCountdowns() {
        return new ArrayList<>(countdowns);
    }

    @Nullable
    public static TeleportRequest getRequest(@NotNull Player from, @Nullable Player to) {

        TeleportRequest out = null;
        for (TeleportRequest request : getRequests()) {
            if ((to != null && !request.getTo().equals(to)) && (from != null && !request.getFrom().equals(from))) continue;
            out = request;
        }

        return out;

    }

    @Nullable
    public static TeleportCountdown getCountdown(@NotNull Player player, @Nullable Player target) {

        TeleportCountdown out = null;
        for (TeleportCountdown countdown : getCountdowns()) {
            if ((target != null && !countdown.getTarget().equals(target)) && !player.equals(countdown.getPlayer())) continue;
            out = countdown;
            break;
        }

        return out;

    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {

        if (!event.hasChangedBlock() && (!cancelTeleportMove || !cancelSpawnMove)) return;

        Player player = event.getPlayer();

        TeleportCountdown out = getCountdown(player, null);
        if (out == null) return;

        if (out.isCanceled()) {
            countdowns.remove(out);
            return;
        }

        out.cancel();
    }

    public static void registerCountdown(@NotNull TeleportCountdown countdown) {
        countdowns.add(countdown);
    }

    public static void unregisterCountdown(@NotNull TeleportCountdown countdown) {
        countdowns.remove(countdown);
    }

    @Nullable
    public static Location getSpawnLocation() {
        if (Bukkit.getPluginManager().isPluginEnabled("EssentialsSpawn")) return EssentialsSpawn.getPlugin(EssentialsSpawn.class).getSpawn("default");
        else return null;
    }

}
