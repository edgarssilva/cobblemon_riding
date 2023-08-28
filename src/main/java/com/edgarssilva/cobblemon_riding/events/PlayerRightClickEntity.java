package com.edgarssilva.cobblemon_riding.events;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.edgarssilva.cobblemon_riding.CobblemonRiding;
import com.edgarssilva.cobblemon_riding.RideablePokemon;
import com.edgarssilva.cobblemon_riding.config.RideablePokemonConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = CobblemonRiding.MODID)
public class PlayerRightClickEntity {

    @SubscribeEvent
    public static void playerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();

        if (!player.getMainHandItem().isEmpty()) return;
        if (!(entity instanceof PokemonEntity pokemonEntity)) return;
        if (!pokemonEntity.isOwnedBy(player)) return;


        List<? extends String> rideableSpecies = RideablePokemonConfig.POKEMON_LIST.get();
        if (!rideableSpecies.contains(pokemonEntity.getPokemon().getSpecies().getResourceIdentifier().toString()))
            return;

        RideablePokemon rideable = ((RideablePokemon) (Object) pokemonEntity);
        rideable.cobblemon_riding$doPlayerRide(player);
    }
}
