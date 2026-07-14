package com.friendsmod.client;

import com.friendsmod.FriendsMod;
import com.friendsmod.network.JumpscarePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

/**
 * Обробляє {@link JumpscarePayload} на клієнті: fullscreen флеш з текстурою бота
 * + різкий поштовх кута огляду камери (проста "тряска").
 */
public final class ClientJumpscareHandler {

    static int scareTicksLeft = 0;
    static ResourceLocation scareTexture = null;

    public static void handle(JumpscarePayload payload) {
        Minecraft mc = Minecraft.getInstance();
        scareTicksLeft = 8; // ~0.4с
        scareTexture = FriendsMod.id("textures/entity/" + payload.variantId() + ".png");

        if (mc.player != null) {
            float jitter = 25f;
            mc.player.setYRot(mc.player.getYRot() + (mc.player.getRandom().nextFloat() - 0.5f) * jitter);
            mc.player.setXRot(mc.player.getXRot() + (mc.player.getRandom().nextFloat() - 0.5f) * jitter);
        }
    }

    private ClientJumpscareHandler() {
    }
}
