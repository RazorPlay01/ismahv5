package com.github.razorplay01.ismah.mixin;

import com.github.razorplay01.ismah.client.util.LivingEntityRenderStateAccessor;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.2 && <1.21.9{
/*import net.minecraft.client.renderer.entity.state.PlayerRenderState;
 *///?}

//? if >=1.21.9{
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
//?}

//? if <1.21.11{
/*import net.minecraft.client.model.PlayerModel;
 *///?}
//? if >=1.21.11{
import net.minecraft.client.model.player.PlayerModel;
//?}

//? if <1.21.2{
/*@Mixin(StuckInBodyLayer.class)
public abstract class StuckInBodyLayerMixin<T extends net.minecraft.world.entity.LivingEntity, M extends PlayerModel<T>> extends RenderLayer<T, M> {
*///?}
//? if >=1.21.2 && <1.21.9{
/*@Mixin(StuckInBodyLayer.class)
public abstract class StuckInBodyLayerMixin<M extends PlayerModel> extends RenderLayer<PlayerRenderState, M> {
*///?}
//? if >=1.21.9{
@Mixin(StuckInBodyLayer.class)
public abstract class StuckInBodyLayerMixin<M extends PlayerModel, S> extends RenderLayer<AvatarRenderState, M> {
//?}

	@Unique
	private ModelPart ismah$currentModelPart;
	@Unique
	private Object ismah$renderState;

	//? if <1.21.2{
	/*protected StuckInBodyLayerMixin(RenderLayerParent<T, M> renderLayerParent) {
	 *///?}
	//? if >=1.21.2 && <1.21.9{
	/*protected StuckInBodyLayerMixin(RenderLayerParent<PlayerRenderState, M> renderLayerParent) {
	 *///?}
	//? if >=1.21.9{
	protected StuckInBodyLayerMixin(RenderLayerParent<AvatarRenderState, M> renderLayerParent) {
		//?}
		super(renderLayerParent);
	}

	//? if <1.21.2{
	/*@Inject(
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;getRandomCube(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/client/model/geom/ModelPart$Cube;", shift = At.Shift.AFTER)
	)
	private void captureModelPart(PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, net.minecraft.world.entity.LivingEntity livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci, @Local ModelPart modelPart) {
		this.ismah$currentModelPart = modelPart;
		this.ismah$renderState = livingEntity;
	}
	*///?}
	//? if >=1.21.2 && <1.21.9{
	/*@Inject(
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/PlayerRenderState;FF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;getRandomCube(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/client/model/geom/ModelPart$Cube;", shift = At.Shift.AFTER)
	)
	private void captureModelPart(PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, PlayerRenderState state, float f, float g, CallbackInfo ci, @Local ModelPart modelPart) {
		this.ismah$currentModelPart = modelPart;
		this.ismah$renderState = state;
	}
	*///?}
	//? if >=1.21.9{
	@Inject(
			method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;getRandomCube(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/client/model/geom/ModelPart$Cube;", shift = At.Shift.AFTER)
	)
	private void captureModelPart(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, AvatarRenderState state, float f, float g, CallbackInfo ci, @Local ModelPart modelPart) {
		this.ismah$currentModelPart = modelPart;
		this.ismah$renderState = state;
	}
	//?}

	//? if <1.21.2{
	/*@WrapWithCondition(
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/StuckInBodyLayer;renderStuckItem(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/Entity;FFFF)V")
	)
	private boolean shouldRenderObject(StuckInBodyLayer instance, PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, net.minecraft.world.entity.Entity entity, float vz, float vx, float vc, float vv) {
	*///?}
	//? if >=1.21.2 && <1.21.9{
	/*@WrapWithCondition(
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/PlayerRenderState;FF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/StuckInBodyLayer;renderStuckItem(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IFFF)V")
	)
	private boolean shouldRenderObject(StuckInBodyLayer instance, PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, float f, float g, float h) {
	*///?}
	//? if >=1.21.9{
	@WrapWithCondition(
			method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/StuckInBodyLayer;submitStuckItem(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IFFFI)V")
	)
	private boolean shouldRenderObject(StuckInBodyLayer instance, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, float f, float g, float h, int j) {
		//?}
		if (!(ismah$renderState instanceof LivingEntityRenderStateAccessor livingEntityRenderStateAccessor))
			return true;
		HumanoidArm arm = livingEntityRenderStateAccessor.getHumanoidArm();
		if (arm == null) return true;
		if (!(this.getParentModel() instanceof HumanoidModel<?>)) return true;
		HumanoidModel<?> model = this.getParentModel();
		ModelPart armPart = arm == HumanoidArm.LEFT ? model.leftArm : model.rightArm;
		return ismah$currentModelPart == armPart;
	}
}
