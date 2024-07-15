package net.survivalboom.survivalboomteleport.actions;

import net.survivalboom.survivalboomteleport.SurvivalBoomTeleport;
import net.survivalboom.survivalboomteleport.configuration.PluginMessages;
import net.survivalboom.survivalboomteleport.placeholders.Placeholders;
import net.survivalboom.survivalboomteleport.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Action {

    protected final String ORIGINAL_ACTION_TEXT;
    protected final String ACTION_TEXT;
    protected final ActionType ACTION_TYPE;
    protected final int DELAY;

    public Action(@NotNull String original_action_text, @NotNull String action_text, @NotNull ActionType action_type, int delay) {

        ORIGINAL_ACTION_TEXT = original_action_text;
        ACTION_TEXT = action_text;
        ACTION_TYPE = action_type;
        DELAY = delay;

    }

    public void perform(@NotNull Player player, @Nullable Player placeholdersTarget, @Nullable Placeholders placeholders) {

        Bukkit.getScheduler().runTaskLater(SurvivalBoomTeleport.getPlugin(), () -> {

            try {

                run(player, Placeholders.parseFull(ACTION_TEXT, placeholdersTarget, placeholders));

            } catch (Exception e) {

                PluginMessages.consoleSend(String.format("&bSurvivalBoomTeleport &8&l► &fAn error occurred while performing action: &e%s", ORIGINAL_ACTION_TEXT));
                PluginMessages.consoleSend(String.format("&c>> &6%s", e));

                PluginMessages.sendMessage(player, String.format("&bSurvivalBoomTeleport &8&l► &fAn error occurred while performing action: &e%s", ORIGINAL_ACTION_TEXT));

                if (e instanceof ActionPerformFailed) return;

                Utils.sendStackTrace(e);

            }

        }, DELAY);

    }

    protected void run(@NotNull Player player, @NotNull String parsedActionText) {



    }

}
