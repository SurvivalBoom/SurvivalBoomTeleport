package net.survivalboom.survivalboomteleport.actions.types;

import net.survivalboom.survivalboomteleport.actions.Action;
import net.survivalboom.survivalboomteleport.actions.ActionType;
import net.survivalboom.survivalboomteleport.configuration.PluginMessages;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionShowActionTitle extends Action {

    public ActionShowActionTitle(String original_action_text, String action_text, int delay) {
        super(original_action_text, action_text, ActionType.SHOW_ACTION_TITLE, delay);
    }

    @Override
    public void run(@NotNull Player player, @NotNull String parsedActionText) {

        player.sendActionBar(PluginMessages.parse(parsedActionText, player));

    }
}
