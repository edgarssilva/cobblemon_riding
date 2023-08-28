package com.edgarssilva.cobblemon_riding;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PlayerRideableJumping;

public interface RideablePokemon extends PlayerRideableJumping {

    void cobblemon_riding$doPlayerRide(Entity entity);

    void cobblemon_riding$setPlayerInput(boolean up, boolean holdUp, boolean down, boolean sprinting);
}
