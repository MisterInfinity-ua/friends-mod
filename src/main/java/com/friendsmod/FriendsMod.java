package com.friendsmod;

import com.friendsmod.config.FriendsConfig;
import com.friendsmod.entity.AnomalyEntity;
import com.friendsmod.network.JumpscarePayload;
import com.friendsmod.registry.ModEntities;
import com.friendsmod.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Friends Mod
 * -----------
 * Додає "anomaly"-ботів (anomaly006, anomaly008, anomaly009, anomaly010, anomaly011),
 * які виглядають як гравці, переслідують у темряві, лякають скримерами і показуються
 * за ніком у списку гравців (Tab).
 */
@Mod(FriendsMod.MOD_ID)
public class FriendsMod {

    public static final String MOD_ID = "friendsmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public FriendsMod(IEventBus modEventBus, ModContainer modContainer) {
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);

        modEventBus.addListener(this::onEntityAttributeCreation);
        modEventBus.addListener(this::onRegisterPayloads);

        modContainer.registerConfig(ModConfig.Type.COMMON, FriendsConfig.SPEC);

        NeoForge.EVENT_BUS.register(com.friendsmod.event.ServerEvents.class);

        LOGGER.info("[FriendsMod] init... something is watching.");
    }

    private void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        for (var holder : ModEntities.ALL.keySet()) {
            event.put(holder.get(), AnomalyEntity.createAttributes().build());
        }
    }

    private void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MOD_ID).versioned("1");
        JumpscarePayload.register(registrar);
    }
}
