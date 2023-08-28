package com.edgarssilva.cobblemon_riding.events;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.edgarssilva.cobblemon_riding.CobblemonRiding;
import com.edgarssilva.cobblemon_riding.ModKeyBindings;
import com.edgarssilva.cobblemon_riding.RideablePokemon;
import com.edgarssilva.cobblemon_riding.networking.ModMessages;
import com.edgarssilva.cobblemon_riding.networking.packets.PlayerInputC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


public class ClientEvents {

    @Mod.EventBusSubscriber(modid = CobblemonRiding.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInputEvent(InputEvent.Key event) {
            if (Minecraft.getInstance().getConnection() == null) return;

            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            PlayerInputC2SPacket packet = new PlayerInputC2SPacket(
                    Minecraft.getInstance().options.keyJump.isDown(),
                    ModKeyBindings.HOLD.isDown(),
                    ModKeyBindings.DESCEND.isDown(),
                    player.isSprinting()
            );

            ModMessages.sendToServer(packet);

            if (!(player.getVehicle() instanceof PokemonEntity pokemonEntity)) return;

            RideablePokemon rideablePokemon = (RideablePokemon) (Object) pokemonEntity;
            rideablePokemon.cobblemon_riding$setPlayerInput(packet.up, packet.holdUp, packet.down, packet.sprint);
        }
    }

    @Mod.EventBusSubscriber(modid = CobblemonRiding.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientFMLEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(ModKeyBindings.HOLD);
            event.register(ModKeyBindings.DESCEND);
        }
    }
}
