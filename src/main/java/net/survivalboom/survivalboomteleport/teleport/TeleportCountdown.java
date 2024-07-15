package net.survivalboom.survivalboomteleport.teleport;

import net.survivalboom.survivalboomteleport.SurvivalBoomTeleport;
import net.survivalboom.survivalboomteleport.events.Event;
import net.survivalboom.survivalboomteleport.placeholders.Placeholders;
import net.survivalboom.survivalboomteleport.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeleportCountdown {

    private BukkitTask task = null;
    private final TeleportCountdownTemplate template;
    private int countdown = 0;
    private Player player;
    private Player target;

    public TeleportCountdown(@NotNull TeleportCountdownTemplate template) {
        this.template = template;
    }

    public TeleportCountdown launch(@NotNull Player player, @Nullable Player target) {

        countdown = template.getCountdown();

        this.player = player;
        this.target = target;

        TeleportManager.registerCountdown(this);

        task = Bukkit.getScheduler().runTaskTimer(SurvivalBoomTeleport.getPlugin(), () -> {

            Placeholders placeholders = new Placeholders();
            placeholders.add("{TIME}", countdown);
            placeholders.add("{SENDER}", player.getName());
            placeholders.add("{SENDER-PREFIX}", Utils.getPrefix(player));
            placeholders.add("{SENDER-SUFFIX}", Utils.getSuffix(player));

            if (target != null) {
                placeholders.add("{TARGET}", target.getName());
                placeholders.add("{TARGET-PREFIX}", Utils.getPrefix(target));
                placeholders.add("{TARGET-SUFFIX}", Utils.getSuffix(target));
            }

            if (!player.isOnline()) {
                task.cancel();
                task = null;
                if (target != null) template.performIfExists("QUIT", target, player, null);
                return;
            }

            if (countdown <= 0) {
                task.cancel();
                task = null;
                if (target != null) {
                    template.performIfExists("TELEPORTED", target, player, placeholders);
                    player.teleport(target);
                }
                else {
                    template.performIfExists("TELEPORTED", player, null, placeholders);
                    player.teleport(TeleportManager.getSpawnLocation());
                }
                return;
            }

            Event event = template.getEvent(countdown);
            if (event != null) {
                if (target != null) event.perform(player, new ArrayList<>(List.of(target)), placeholders);
                else event.perform(player, placeholders);
            }

            countdown--;

        }, 20L, 20L);

        return this;

    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public Player getTarget() {
        return target;
    }

    public void cancel() {
        if (task == null) return;
        task.cancel();
        task = null;
        TeleportManager.unregisterCountdown(this);

        if (target == null) {
            template.performIfExists("MOVED", player, null, null);
            return;
        }

        Placeholders placeholders = new Placeholders();
        placeholders.add("{TARGET}", player.getName());

        template.performIfExists("MOVED", target, player, placeholders);

    }

    public boolean isCanceled() {
        return task == null;
    }

    public TeleportCountdownTemplate getTemplate() {
        return template;
    }

}
