package com.github.razorplay01.ismah.mixin;
//? if >= 1.21.2 {

/*import com.github.razorplay01.ismah.client.util.LivingEntityRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HumanoidRenderState.class)
public abstract class LivingEntityRenderStateMixin implements LivingEntityRenderStateAccessor {
    @Unique
    private Entity entity;

    @Unique
    private @Nullable HumanoidArm firstPersonArm = null;

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

	@Unique private float[] ismah$armPose;

	@Override
	public void setFirstPersonArmPose(float xRot, float yRot, float zRot, float x, float y, float z) {
		this.ismah$armPose = new float[]{xRot, yRot, zRot, x, y, z};
	}

	@Override
	public @Nullable float[] getFirstPersonArmPose() {
		return this.ismah$armPose;
	}
}
*///?}
