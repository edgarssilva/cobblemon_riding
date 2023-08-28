package com.edgarssilva.cobblemon_riding.events;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.edgarssilva.cobblemon_riding.CobblemonRiding;
import com.edgarssilva.cobblemon_riding.RideablePokemon;
import com.edgarssilva.cobblemon_riding.networking.PlayerInputs;
import com.edgarssilva.cobblemon_riding.networking.packets.PlayerInputC2SPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CobblemonRiding.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.level.isClientSide()) return;
        if (!(player.getVehicle() instanceof PokemonEntity pokemonEntity)) return;

        PlayerInputC2SPacket packet = PlayerInputs.getPlayerInput(player.getUUID());
        if (packet == null) return;

        RideablePokemon rideablePokemon = (RideablePokemon) (Object) pokemonEntity;
        rideablePokemon.cobblemon_riding$setPlayerInput(packet.up, packet.holdUp, packet.down, packet.sprint);
        pokemonEntity.travel(Vec3.ZERO);
    }
}
