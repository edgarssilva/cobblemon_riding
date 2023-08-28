package com.edgarssilva.cobblemon_riding;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.edgarssilva.cobblemon_riding.config.RideablePokemonConfig;
import com.edgarssilva.cobblemon_riding.networking.ModMessages;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod(CobblemonRiding.MODID)
public class CobblemonRiding {

    public static final String MODID = "cobblemon_riding";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public CobblemonRiding() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RideablePokemonConfig.SPEC, "cobblemon_riding.toml");

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModMessages.register();

        RideablePokemonConfig.POKEMON_LIST.get().forEach((str) -> {
            if (PokemonSpecies.INSTANCE.getByIdentifier(new ResourceLocation(str)) == null)
                LOGGER.error("Invalid pokemon species:" + str);
        });

        LOGGER.info("Cobblemon Riding has been loaded");
    }


    public static Logger getLogger() {
        return LOGGER;
    }
}