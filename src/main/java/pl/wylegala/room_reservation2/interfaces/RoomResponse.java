package pl.wylegala.room_reservation2.interfaces;

import java.util.UUID;

public record RoomResponse(
        UUID id,
        String name,
        int capacity
) {}
