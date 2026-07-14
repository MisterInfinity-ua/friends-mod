package com.friendsmod.taglist;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Показує ніки anomaly-ботів у списку гравців (клавіша Tab) окремим гравцям —
 * без реального підключення бота до сервера.
 *
 * Технічно ми створюємо {@link FakePlayer} (офіційний "гравець без мережі" від
 * NeoForge, який завжди має GameProfile) і транслюємо для нього ті самі пакети,
 * якими сервер оновлює tab-list для справжніх гравців.
 *
 * ВАЖЛИВО: без підписаного Mojang-сервером текстур-property в GameProfile
 * (чого без офіційного акаунта Mojang зробити не можна) у самому tab-list
 * буде показана дефолтна голова Steve/Alex — це обмеження протоколу, а не
 * помилка мода. Страшний вигляд бота "в грі" (сама сутність) не залежить від
 * цього і використовує кастомну текстуру з ресурспаку мода (AnomalyEntityRenderer).
 */
public final class FakeTabListManager {

    private static final Map<String, FakePlayer> FAKE_PLAYERS = new ConcurrentHashMap<>();

    public static UUID uuidFor(String botName) {
        return UUID.nameUUIDFromBytes(("friendsmod:" + botName).getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    public static FakePlayer getOrCreate(ServerLevel level, String botName) {
        return FAKE_PLAYERS.computeIfAbsent(botName, name -> {
            GameProfile profile = new GameProfile(uuidFor(name), name);
            return net.neoforged.neoforge.common.util.FakePlayerFactory.get(level, profile);
        });
    }

    /** Додає/оновлює запис бота в tab-list конкретного гравця. */
    public static void addToTabList(ServerLevel level, ServerPlayer viewer, String botName) {
        FakePlayer fake = getOrCreate(level, botName);
        viewer.connection.send(new ClientboundPlayerInfoUpdatePacket(
                EnumSet.of(
                        ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
                        ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED,
                        ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY
                ),
                List.of(fake)));
    }

    /** Прибирає запис бота з tab-list конкретного гравця. */
    public static void removeFromTabList(ServerPlayer viewer, String botName) {
        viewer.connection.send(new ClientboundPlayerInfoRemovePacket(List.of(uuidFor(botName))));
    }

    private FakeTabListManager() {
    }
}