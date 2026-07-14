package com.friendsmod.client;

import com.friendsmod.FriendsMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = FriendsMod.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class ClientGameEvents {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (ClientJumpscareHandler.scareTicksLeft <= 0) return;
        ClientJumpscareHandler.scareTicksLeft--;

        var graphics = event.getGuiGraphics();
        int w = graphics.guiWidth();
        int h = graphics.guiHeight();

        int alpha = (int) (200 * (ClientJumpscareHandler.scareTicksLeft / 8.0));
        int color = (alpha << 24) | 0x8B0000;
        graphics.fill(0, 0, w, h, color);

        if (ClientJumpscareHandler.scareTexture != null) {
            int size = Math.min(w, h) - 40;
            graphics.blit(ClientJumpscareHandler.scareTexture, (w - size) / 2, (h - size) / 2,
                    0, 0, size, size, size, size);
        }
    }

    private ClientGameEvents() {
    }
}
