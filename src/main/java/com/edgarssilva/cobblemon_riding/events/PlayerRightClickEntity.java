package com.edgarssilva.cobblemon_riding.events;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.edgarssilva.cobblemon_riding.CobblemonRiding;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CobblemonRiding.MODID)
public class PlayerRightClickEntity {

    @SubscribeEvent
    public static void playerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        if (!(entity instanceof PokemonEntity pokemonEntity)) return;

        Pokemon pokemon = pokemonEntity.getPokemon();
        //TODO: Filter by the showdownId of the pokemon

        if (!pokemonEntity.isOwnedBy(player)) return;

        player.startRiding(pokemonEntity);
    }
}
