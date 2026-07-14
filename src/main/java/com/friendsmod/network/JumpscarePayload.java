package com.friendsmod.network;

import com.friendsmod.FriendsMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * S2C пакет: сервер каже клієнту "тебе щойно налякав бот <variantId>".
 * Клієнт малює fullscreen флеш з текстурою відповідного анти-бота.
 */
public record JumpscarePayload(String variantId) implements CustomPacketPayload {

    public static final Type<JumpscarePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(FriendsMod.MOD_ID, "jumpscare"));

    public static final StreamCodec<RegistryFriendlyByteBuf, JumpscarePayload> CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeUtf(payload.variantId()),
                    buf -> new JumpscarePayload(buf.readUtf())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void register(PayloadRegistrar registrar) {
        registrar.playToClient(TYPE, CODEC, (payload, context) ->
                context.enqueueWork(() -> com.friendsmod.client.ClientJumpscareHandler.handle(payload)));
    }
}
