package net.survivalboom.survivalboomteleport.actions.types;

import net.survivalboom.survivalboomteleport.actions.Action;
import net.survivalboom.survivalboomteleport.actions.ActionType;
import net.survivalboom.survivalboomteleport.configuration.PluginMessages;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionSendMessage extends Action {
    public ActionSendMessage(String original_action_text, String action_text, int delay) {
        super(original_action_text, action_text, ActionType.SEND_MESSAGE, delay);
    }

    @Override
    public void run(@NotNull Player player, @NotNull String parsedActionText) {
        PluginMessages.sendMessage(player, parsedActionText);
    }
}
