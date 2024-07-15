package net.survivalboom.survivalboomteleport.events;

import org.jetbrains.annotations.NotNull;

public class EventCreationFailed extends RuntimeException {

    public EventCreationFailed(@NotNull String msg) {
        super(msg);
    }

}
