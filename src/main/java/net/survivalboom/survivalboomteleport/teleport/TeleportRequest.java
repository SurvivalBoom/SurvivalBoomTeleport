package net.survivalboom.survivalboomteleport.teleport;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportRequest {

    private final Player from;
    private final Player to;
    private final boolean inverted;

    public TeleportRequest(@NotNull Player from, @NotNull Player to, boolean inverted) {
        this.from = from;
        this.to = to;
        this.inverted = inverted;
    }

    public TeleportCountdown accept() {
        return TeleportManager.acceptTeleport(this);
    }

    public void decline() {
        TeleportManager.declineTeleport(this);
    }

    @NotNull
    public Player getFrom() {
        if (!inverted) return from;
        else return to;
    }

    @NotNull
    public Player getTo() {
        if (!inverted) return to;
        else return from;
    }

}
