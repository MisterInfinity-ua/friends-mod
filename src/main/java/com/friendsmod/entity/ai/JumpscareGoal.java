package com.friendsmod.entity.ai;

import com.friendsmod.config.FriendsConfig;
import com.friendsmod.entity.AnomalyEntity;
import com.friendsmod.network.JumpscarePayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.EnumSet;

/**
 * Коли бот дуже близько до гравця і той його не бачить (за спиною) -> скример:
 * пакет клієнту (флеш + звук на екрані), реальні статус-ефекти (нудота/сліпота) та
 * моторошне повідомлення в чат. Після цього бот відходить у "кулдаун".
 */
public class JumpscareGoal extends Goal {

    private static final double TRIGGER_RANGE_SQ = 3.0 * 3.0;
    private final AnomalyEntity anomaly;
    private int cooldown;

    public JumpscareGoal(AnomalyEntity anomaly) {
        this.anomaly = anomaly;
        this.setFlags(EnumSet.noneOf(Flag.class));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        if (!FriendsConfig.JUMPSCARE_ENABLED.get() || anomaly.isJumpscaring()) return false;

        Player player = anomaly.level().getNearestPlayer(anomaly, 4);
        return player != null && anomaly.distanceToSqr(player) <= TRIGGER_RANGE_SQ
                && !player.isCreative() && !player.isSpectator();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return false;
    }

    @Override
    public void start() {
        Player nearby = anomaly.level().getNearestPlayer(anomaly, 4);
        if (!(nearby instanceof ServerPlayer player)) return;

        anomaly.setJumpscaring(true);
        cooldown = FriendsConfig.SCARE_COOLDOWN_TICKS.get();

        // Справжні статус-ефекти для "глітчу" екрана
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 50, 1));
        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 30, 0));

        BlockPos pos = anomaly.blockPosition();
        anomaly.level().playSound(null, pos, SoundEvents.ENDERMAN_SCREAM, SoundSource.HOSTILE,
                1.6f, 0.85f + anomaly.getRandom().nextFloat() * 0.25f);

        player.sendSystemMessage(Component.literal("ВІН ТЕБЕ БАЧИТЬ")
                .withStyle(s -> s.withBold(true).withColor(0xAA0000)));

        // S2C пакет клієнту: fullscreen флеш + текстура варіанта + тряска камери
        PacketDistributor.sendToPlayer(player, new JumpscarePayload(anomaly.getVariant()));
    }

    @Override
    public boolean canContinueToUse() {
        return false; // одноразова дія
    }
}
