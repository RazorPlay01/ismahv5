package com.github.razorplay01.ismah.client.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.Nullable;

public interface LivingEntityRenderStateAccessor {
    void setEntity(Entity player);
    Entity getEntity();

    void setHumanoidArm(@Nullable HumanoidArm arm);
    @Nullable HumanoidArm getHumanoidArm();

	void setFirstPersonArmPose(float xRot, float yRot, float zRot, float x, float y, float z);
	@Nullable float[] getFirstPersonArmPose();
}
