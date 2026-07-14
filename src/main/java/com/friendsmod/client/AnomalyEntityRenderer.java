package com.friendsmod.client;

import com.friendsmod.FriendsMod;
import com.friendsmod.entity.AnomalyEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;

/**
 * Рендерить anomaly-ботів як гуманоїдів з моделлю гравця, використовуючи
 * textures/entity/&lt;variant&gt;.png (наприклад anomaly011.png) як скін.
 */
public class AnomalyEntityRenderer extends HumanoidMobRenderer<AnomalyEntity, PlayerModel<AnomalyEntity>> {

    public AnomalyEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(AnomalyEntity entity) {
        return FriendsMod.id("textures/entity/" + entity.getVariant() + ".png");
    }
}
