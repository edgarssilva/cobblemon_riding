package com.edgarssilva.cobblemon_riding.networking.packets;

import com.edgarssilva.cobblemon_riding.networking.PlayerInputs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerInputC2SPacket {

    public boolean up;
    public boolean holdUp;
    public boolean down;

    public boolean sprint;

    public PlayerInputC2SPacket(boolean up, boolean holdUp, boolean down, boolean sprint) {
        this.up = up;
        this.holdUp = holdUp;
        this.down = down;
        this.sprint = sprint;
    }

    public PlayerInputC2SPacket(FriendlyByteBuf buf) {
        this.up = buf.readBoolean();
        this.holdUp = buf.readBoolean();
        this.down = buf.readBoolean();
        this.sprint = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.up);
        buf.writeBoolean(this.holdUp);
        buf.writeBoolean(this.down);
        buf.writeBoolean(this.sprint);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //To be run on the server
            ServerPlayer player = context.getSender();
            if (player == null) return;

            PlayerInputs.addPlayerInput(player.getUUID(), this);
        });
        context.setPacketHandled(true);
    }
}
