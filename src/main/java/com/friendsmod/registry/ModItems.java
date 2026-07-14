package com.friendsmod.registry;

import com.friendsmod.FriendsMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FriendsMod.MOD_ID);

    public static final DeferredItem<SpawnEggItem> ANOMALY_006_EGG = registerEgg("anomaly006_spawn_egg", ModEntities.ANOMALY_006, 0x0a0a0a, 0xff0000);
    public static final DeferredItem<SpawnEggItem> ANOMALY_008_EGG = registerEgg("anomaly008_spawn_egg", ModEntities.ANOMALY_008, 0x151515, 0x00ff66);
    public static final DeferredItem<SpawnEggItem> ANOMALY_009_EGG = registerEgg("anomaly009_spawn_egg", ModEntities.ANOMALY_009, 0x101018, 0xffaa00);
    public static final DeferredItem<SpawnEggItem> ANOMALY_010_EGG = registerEgg("anomaly010_spawn_egg", ModEntities.ANOMALY_010, 0x1a0000, 0xffffff);
    public static final DeferredItem<SpawnEggItem> ANOMALY_011_EGG = registerEgg("anomaly011_spawn_egg", ModEntities.ANOMALY_011, 0x000000, 0x6a00ff);

    private static DeferredItem<SpawnEggItem> registerEgg(
            String path,
            net.neoforged.neoforge.registries.DeferredHolder<net.minecraft.world.entity.EntityType<?>, ?> entityType,
            int primary, int secondary) {
        return ITEMS.register(path, () -> new SpawnEggItem(
                (net.minecraft.world.entity.EntityType) entityType.get(),
                primary, secondary,
                new Item.Properties()));
    }
}
