package com.friendsmod.entity.ai;

import com.friendsmod.entity.AnomalyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Бот шукає найближчого гравця в темних місцях і зрідка тихо телепортується
 * ближче до нього (за спиною, поза полем зору), замість того щоб бігти по прямій.
 */
public class StalkPlayerGoal extends Goal {

    private final AnomalyEntity anomaly;
    private final double speed;
    private Player target;
    private int teleportCooldown;

    public StalkPlayerGoal(AnomalyEntity anomaly, double speed) {
        this.anomaly = anomaly;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        target = anomaly.level().getNearestPlayer(anomaly, 48);
        return target != null && !target.isCreative() && !target.isSpectator()
                && anomaly.level().getMaxLocalRawBrightness(anomaly.blockPosition()) <= 7;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() && !target.isCreative() && !target.isSpectator()
                && anomaly.distanceToSqr(target) < 48 * 48;
    }

    @Override
    public void start() {
        teleportCooldown = 60 + anomaly.getRandom().nextInt(60);
    }

    @Override
    public void tick() {
        if (target == null) return;
        anomaly.getLookControl().setLookAt(target, 30f, 30f);

        double distSq = anomaly.distanceToSqr(target);
        if (teleportCooldown-- <= 0 && distSq > 6 * 6 && !target.hasLineOfSight(anomaly)) {
            attemptStalkTeleport();
            teleportCooldown = 80 + anomaly.getRandom().nextInt(100);
        } else if (distSq > 4 * 4) {
            anomaly.getNavigation().moveTo(target, speed);
        }
    }

    /** Телепортує бота на кілька блоків від гравця, у місце, де темно. */
    private void attemptStalkTeleport() {
        var random = anomaly.getRandom();
        for (int i = 0; i < 12; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double dist = 6 + random.nextDouble() * 8;
            double x = target.getX() + Math.cos(angle) * dist;
            double z = target.getZ() + Math.sin(angle) * dist;
            BlockPos pos = BlockPos.containing(x, target.getY(), z);
            BlockPos ground = anomaly.level().getHeightmapPos(
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, pos);

            if (anomaly.level().getMaxLocalRawBrightness(ground) <= 7) {
                anomaly.teleportTo(ground.getX() + 0.5, ground.getY(), ground.getZ() + 0.5);
                return;
            }
        }
    }
}
