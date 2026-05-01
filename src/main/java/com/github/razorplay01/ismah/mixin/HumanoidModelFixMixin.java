package com.github.razorplay01.ismah.mixin;

import com.github.razorplay01.ismah.client.util.LivingEntityRenderStateAccessor;
import com.github.razorplay01.ismah.mixin.accesor.ModelPartAccessor;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//? if >=1.21.2{
/*import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
		*///?}

//? if <1.21.2{
@Mixin(HumanoidModel.class)
public abstract class HumanoidModelFixMixin<T extends net.minecraft.world.entity.LivingEntity> extends net.minecraft.client.model.AgeableListModel<T> implements ArmedModel, HeadedModel {
	@Unique
	private Map<ModelPart, List<ModelPart.Cube>> ismah$savedCubes;

	@Inject(
			method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
			at = @At("HEAD")
	)
	private void ismah$restoreCubes(net.minecraft.world.entity.LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if (ismah$savedCubes != null) {
			for (var entry : ismah$savedCubes.entrySet()) {
				((ModelPartAccessor) (Object) entry.getKey()).ismah$setCubes(entry.getValue());
			}
			ismah$savedCubes = null;
		}
	}

	@Inject(
			method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
			at = @At("RETURN")
	)
	private void fixArmRotationInFirstPerson(net.minecraft.world.entity.LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if (!(livingEntity instanceof net.minecraft.client.player.LocalPlayer localPlayer)) return;
		LivingEntityRenderStateAccessor accessor = (LivingEntityRenderStateAccessor) localPlayer;
		HumanoidArm arm = accessor.getHumanoidArm();
		if (arm == null) return;

		HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
		model.setAllVisible(false);

		ModelPart targetArm = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
		targetArm.visible = true;

		// Aplicar pose capturada de primera persona
		float[] pose = accessor.getFirstPersonArmPose();
		if (pose != null) {
			targetArm.xRot = pose[0];
			targetArm.yRot = pose[1];
			targetArm.zRot = pose[2];
			targetArm.x = pose[3];
			targetArm.y = pose[4];
			targetArm.z = pose[5];
		} else {
			targetArm.xRot = 0.0F;
			targetArm.yRot = 0.0F;
			targetArm.zRot = 0.0F;
		}

		Set<ModelPart> armParts = targetArm.getAllParts().collect(java.util.stream.Collectors.toSet());
		ismah$savedCubes = new IdentityHashMap<>();
		for (ModelPart part : this.bodyParts()) {
			ModelPartAccessor partAccessor = ((ModelPartAccessor) (Object) part);
			List<ModelPart.Cube> cubes = partAccessor.ismah$getCubes();
			if (!armParts.contains(part) && !cubes.isEmpty()) {
				ismah$savedCubes.put(part, cubes);
				partAccessor.ismah$setCubes(List.of());
			}
		}
	}
}
//?}


//? if >=1.21.2{
/*@Mixin(HumanoidModel.class)
public abstract class HumanoidModelFixMixin<T extends HumanoidRenderState> extends EntityModel<T>
		implements ArmedModel/^? if >=1.21.9 {^//^<T>^//^?}^/, HeadedModel {

	@Unique
	private Map<ModelPart, List<ModelPart.Cube>> ismah$savedCubes;

	protected HumanoidModelFixMixin(ModelPart modelPart) {
		super(modelPart);
	}

	@Inject(
			method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
			at = @At("HEAD")
	)
	private void ismah$restoreCubes(T state, CallbackInfo ci) {
		if (ismah$savedCubes != null) {
			for (var entry : ismah$savedCubes.entrySet()) {
				((ModelPartAccessor) (Object) entry.getKey()).ismah$setCubes(entry.getValue());
			}
			ismah$savedCubes = null;
		}
	}

	@Inject(
			method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
			at = @At("RETURN")
	)
	private void fixArmRotationInFirstPerson(T state, CallbackInfo ci) {
		LivingEntityRenderStateAccessor accessor = (LivingEntityRenderStateAccessor) state;
		HumanoidArm arm = accessor.getHumanoidArm();
		if (arm == null) return;

		HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
		setAllVisible(model, false);

		ModelPart targetArm = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
		targetArm.visible = true;

		// Aplicar pose capturada de primera persona
		float[] pose = accessor.getFirstPersonArmPose();
		if (pose != null) {
			targetArm.xRot = pose[0];
			targetArm.yRot = pose[1];
			targetArm.zRot = pose[2];
			targetArm.x = pose[3];
			targetArm.y = pose[4];
			targetArm.z = pose[5];
		} else {
			targetArm.xRot = 0.0F;
			targetArm.yRot = 0.0F;
			targetArm.zRot = 0.0F;
		}

		Set<ModelPart> armParts = new HashSet<>(targetArm.getAllParts() /^? if <1.21.6 {^/.collect(java.util.stream.Collectors.toSet())/^?}^/);
		ismah$savedCubes = new IdentityHashMap<>();
		for (ModelPart part : this.root().getAllParts() /^? if <1.21.6 {^/.toList()/^?}^/) {
			ModelPartAccessor partAccessor = ((ModelPartAccessor) (Object) part);
			List<ModelPart.Cube> cubes = partAccessor.ismah$getCubes();
			if (!armParts.contains(part) && !cubes.isEmpty()) {
				ismah$savedCubes.put(part, cubes);
				partAccessor.ismah$setCubes(List.of());
			}
		}
	}

	@Unique
	public void setAllVisible(HumanoidModel model, boolean bl) {
		model.head.visible = bl;
		model.hat.visible = bl;
		model.body.visible = bl;
		model.rightArm.visible = bl;
		model.leftArm.visible = bl;
		model.rightLeg.visible = bl;
		model.leftLeg.visible = bl;
	}
}
*///?}
