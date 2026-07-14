package com.friendsmod.registry;

import com.friendsmod.FriendsMod;
import com.friendsmod.entity.AnomalyEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, FriendsMod.MOD_ID);

    /** entity-holder -> ім'я варіанту (напр. "anomaly011"), в порядку реєстрації. */
    public static final Map<DeferredHolder<EntityType<?>, EntityType<AnomalyEntity>>, String> ALL = new LinkedHashMap<>();

    public static final DeferredHolder<EntityType<?>, EntityType<AnomalyEntity>> ANOMALY_006 = register("anomaly006");
    public static final DeferredHolder<EntityType<?>, EntityType<AnomalyEntity>> ANOMALY_008 = register("anomaly008");
    public static final DeferredHolder<EntityType<?>, EntityType<AnomalyEntity>> ANOMALY_009 = register("anomaly009");
    public static final DeferredHolder<EntityType<?>, EntityType<AnomalyEntity>> ANOMALY_010 = register("anomaly010");
    public static final DeferredHolder<EntityType<?>, EntityType<AnomalyEntity>> ANOMALY_011 = register("anomaly011");

    @SuppressWarnings("unchecked")
    private static DeferredHolder<EntityType<?>, EntityType<AnomalyEntity>> register(String name) {
        DeferredHolder<EntityType<?>, EntityType<AnomalyEntity>> holder = ENTITY_TYPES.register(name, () ->
                EntityType.Builder.<AnomalyEntity>of(
                                (type, level) -> new AnomalyEntity((EntityType<? extends AnomalyEntity>) type, level, name),
                                MobCategory.MONSTER)
                        .sized(0.6f, 1.95f)
                        .clientTrackingRange(10)
                        .build(name));
        ALL.put(holder, name);
        return holder;
    }

    public static void bootstrap() {
        FriendsMod.LOGGER.info("[FriendsMod] Registered {} anomaly entity types.", ALL.size());
    }
}
