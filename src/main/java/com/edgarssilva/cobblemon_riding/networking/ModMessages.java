package com.edgarssilva.cobblemon_riding.networking;

import com.edgarssilva.cobblemon_riding.CobblemonRiding;
import com.edgarssilva.cobblemon_riding.networking.packets.PlayerInputC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(CobblemonRiding.MODID, "main_channel"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions("1.0"::equals)
                .serverAcceptedVersions("1.0"::equals)
                .simpleChannel();

        INSTANCE.messageBuilder(PlayerInputC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(PlayerInputC2SPacket::encode)
                .decoder(PlayerInputC2SPacket::new)
                .consumerMainThread(PlayerInputC2SPacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
        if (INSTANCE == null) return;
        INSTANCE.sendToServer(message);
    }
}