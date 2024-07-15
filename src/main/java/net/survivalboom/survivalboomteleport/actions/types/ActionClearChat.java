package net.survivalboom.survivalboomteleport.actions.types;

import net.survivalboom.survivalboomteleport.actions.Action;
import net.survivalboom.survivalboomteleport.actions.ActionPerformFailed;
import net.survivalboom.survivalboomteleport.actions.ActionType;
import net.survivalboom.survivalboomteleport.configuration.PluginMessages;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionClearChat extends Action {


    public ActionClearChat(@NotNull String original_action_text, @NotNull String action_text, int delay) {
        super(original_action_text, action_text, ActionType.CLEAR_CHAT, delay);
    }

    @Override
    public void run(@NotNull Player player, @NotNull String parsedActionText) {

        try {

            for (int i = 0; i < Integer.parseInt(parsedActionText); i++) {
                PluginMessages.sendMessage(player, "&b&l &r");
            }

        }


        catch (NumberFormatException e) { throw new ActionPerformFailed("Action CLEAR_CHAT argument must be an integer");}
    }
}
