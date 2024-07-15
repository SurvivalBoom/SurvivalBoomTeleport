package net.survivalboom.survivalboomteleport.teleport;

import net.survivalboom.survivalboomteleport.events.Event;
import net.survivalboom.survivalboomteleport.events.Events;
import net.survivalboom.survivalboomteleport.placeholders.Placeholders;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeleportCountdownTemplate {

    private Events countdownEvents = null;
    private final int countdown;

    public TeleportCountdownTemplate(@NotNull ConfigurationSection section) {

        countdown = section.getInt("countdown");

        ConfigurationSection countdownSection = section.getConfigurationSection("events");
        if (countdownSection == null) return;

//        for (String s : countdownSection.getKeys(false)) {
//
//            try {
//                int value = Integer.parseInt(s);
//                countdownEvents.put(value, new Event(s, section.getConfigurationSection(s)));
//            }
//
//            catch (Exception e) { PluginMessages.consoleSend(String.format("&c>> &fFailed to load teleport countdown &e%s&f: &c%s", s, e.getMessage())); }
//
//        }

        countdownEvents = new Events(countdownSection);

    }

    public TeleportCountdown start(@NotNull Player player, @Nullable Player other) {
        return new TeleportCountdown(this).launch(player, other);
    }

    @NotNull
    public Events getCountdownEvents() {
        return countdownEvents;
    }

    @Nullable
    public Event getEvent(int index) {
        return countdownEvents.getEvent(String.valueOf(index));
    }

    @Nullable
    public Event getEvent(@NotNull String key) {
        return countdownEvents.getEvent(key);
    }

    public void performIfExists(@NotNull String name, @NotNull Player player, @Nullable Player other, @Nullable Placeholders placeholders) {
        Event event = getEvent(name);
        if (event == null) return;
        if (other != null) event.perform(player, new ArrayList<>(List.of(other)), placeholders);
        else event.perform(player, placeholders);
    }

    public int getCountdown() {
        return countdown;
    }

}
