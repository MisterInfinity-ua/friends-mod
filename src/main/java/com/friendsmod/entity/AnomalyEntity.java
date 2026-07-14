package com.friendsmod.entity;

import com.friendsmod.entity.ai.JumpscareGoal;
import com.friendsmod.entity.ai.StalkPlayerGoal;
import com.friendsmod.taglist.FakeTabListManager;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/**
 * Один клас на всі варіанти (anomaly006, anomaly008, ...). Варіант визначає лише
 * ім'я/текстуру/інтенсивність, поведінка спільна.
 */
public class AnomalyEntity extends Monster {

    private static final EntityDataAccessor<Boolean> JUMPSCARING =
            SynchedEntityData.defineId(AnomalyEntity.class, EntityDataSerializers.BOOLEAN);

    private final String variant;
    private int despawnAfterScareTicks = -1;

    public AnomalyEntity(EntityType<? extends AnomalyEntity> type, Level level, String variant) {
        super(type, level);
        this.variant = variant;
        this.setCustomName(Component.literal(variant));
        this.setCustomNameVisible(true);
        this.setPersistenceRequired();
        this.xpReward = 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.27)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0); // не б'є фізично — лякає психологічно
    }

    public String getVariant() {
        return variant;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(JUMPSCARING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new StalkPlayerGoal(this, 1.15));
        this.goalSelector.addGoal(2, new JumpscareGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F));
    }

    public boolean isJumpscaring() {
        return this.entityData.get(JUMPSCARING);
    }

    public void setJumpscaring(boolean value) {
        this.entityData.set(JUMPSCARING, value);
        if (value) {
            this.despawnAfterScareTicks = 12; // моторошно зникає ~0.6с після скримера
        }
    }

    @Override
    public boolean checkSpawnObstruction(net.minecraft.world.level.LevelReader level) {
        return level.getRawBrightness(this.blockPosition(), 0) <= 5 && super.checkSpawnObstruction(level);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level() instanceof ServerLevel serverLevel) {
            // Тримаємо бота в списку гравців (Tab) для всіх, хто поруч.
            if (this.tickCount % 20 == 0) {
                double range = com.friendsmod.config.FriendsConfig.TAB_LIST_RANGE.get();
                if (com.friendsmod.config.FriendsConfig.TAB_LIST_ENABLED.get()) {
                    for (ServerPlayer player : serverLevel.players()) {
                        if (player.distanceToSqr(this) <= range * range) {
                            FakeTabListManager.addToTabList(serverLevel, player, variant);
                        }
                    }
                }
            }

            if (despawnAfterScareTicks > 0) {
                despawnAfterScareTicks--;
                if (despawnAfterScareTicks == 0) {
                    this.discard();
                }
            }
        }
    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();
        if (this.level() instanceof ServerLevel serverLevel) {
            for (ServerPlayer player : serverLevel.players()) {
                FakeTabListManager.removeFromTabList(player, variant);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMAN_STARE;
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource source) {
        return SoundEvents.ENDERMAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
    }

    @Override
    public void playAmbientSound() {
        // Рідкісні статичні звуки замість звичайних
        if (this.random.nextInt(200) == 0) {
            super.playAmbientSound();
        }
    }

    @Override
    protected float getSoundVolume() {
        return 0.6F;
    }
}