package com.edgarssilva.cobblemon_riding.networking;

import com.edgarssilva.cobblemon_riding.networking.packets.PlayerInputC2SPacket;

import java.util.HashMap;
import java.util.UUID;

public class PlayerInputs {
    private static final HashMap<UUID, PlayerInputC2SPacket> playerInputs = new HashMap<>();

    public static void addPlayerInput(UUID uuid, PlayerInputC2SPacket movementC2SPacket) {
        playerInputs.put(uuid, movementC2SPacket);
    }

    public static PlayerInputC2SPacket getPlayerInput(UUID uuid) {
        return playerInputs.get(uuid);
    }

}
