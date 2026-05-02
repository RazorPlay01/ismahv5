package com.github.razorplay01.ismah.mixin;

import com.github.razorplay01.ismah.client.util.LeashStateAccess;
import com.github.razorplay01.ismah.client.util.LivingEntityRenderStateAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin implements LeashStateAccess
//? if >= 1.20 && <= 1.21.1 {
/*, LivingEntityRenderStateAccessor
*///?}
{
	@Unique
	private boolean leashState = false;

	@Override
	public boolean getLeashState() {
		return leashState;
	}

	@Override
	public void setLeashState(boolean state) {
		leashState = state;
	}

	//? if >= 1.20 && <= 1.21.1 {
	/*@Unique
	private Entity entity;

	@Unique
	private @Nullable HumanoidArm firstPersonArm = null;

	@Unique
	private float[] ismah$armPose;

	@Override
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	@Override
	public void setHumanoidArm(@Nullable HumanoidArm arm) {
		this.firstPersonArm = arm;
	}

	@Override
	public @Nullable HumanoidArm getHumanoidArm() {
		return firstPersonArm;
	}

	@Override
	public void setFirstPersonArmPose(float xRot, float yRot, float zRot, float x, float y, float z) {
		this.ismah$armPose = new float[]{xRot, yRot, zRot, x, y, z};
	}

	@Override
	public @Nullable float[] getFirstPersonArmPose() {
		return this.ismah$armPose;
	}
	*///?}
}
