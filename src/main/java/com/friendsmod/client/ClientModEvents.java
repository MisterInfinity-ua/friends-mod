package com.friendsmod.client;

import com.friendsmod.FriendsMod;
import com.friendsmod.registry.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = FriendsMod.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModEntities.ALL.keySet().forEach(holder ->
                event.registerEntityRenderer(holder.get(), AnomalyEntityRenderer::new));
    }

    private ClientModEvents() {
    }
}
