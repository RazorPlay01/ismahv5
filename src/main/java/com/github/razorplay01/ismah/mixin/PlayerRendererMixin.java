package com.github.razorplay01.ismah.mixin;

import com.github.razorplay01.ismah.client.util.FirstPersonLayerHolder;
import com.github.razorplay01.ismah.client.util.LeashRenderLayer;
import com.github.razorplay01.ismah.client.util.LivingEntityRenderStateAccessor;
import com.github.razorplay01.ismah.mixin.accesor.LivingEntityRendererAccesor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.2 && <1.21.9{
/*import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
*///?}

//? if <1.21.9{
/*import net.minecraft.client.renderer.entity.player.PlayerRenderer;
 *///?}

//? if >=1.21.9{
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
//?}

//? if <1.21.11{
/*import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.RenderType;
*///?}
//? if >=1.21.11{
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.model.player.PlayerModel;
//?}

//? if <1.21.2{
/*@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>
		implements FirstPersonLayerHolder {

	@Unique
	private HumanoidArmorLayer armorLayer;
	@Unique
	private net.minecraft.client.renderer.entity.layers.ArrowLayer arrowLayer;
	@Unique
	private LeashRenderLayer leashRenderLayer;
	@Unique
	private Matrix4f ismah$savedPose;
	@Unique
	private Matrix3f ismah$savedNormal;

	@Override
	public @Nullable Object ismah$getArmorLayer() {
		return this.armorLayer;
	}

	@Override
	public @Nullable Object ismah$getArrowLayer() {
		return this.arrowLayer;
	}

	@Override
	public @Nullable Object ismah$getLeashRenderLayer() {
		return this.leashRenderLayer;
	}

	protected PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> entityModel, float f) {
		super(context, entityModel, f);
	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;addLayer(Lnet/minecraft/client/renderer/entity/layers/RenderLayer;)Z"))
	private boolean captureLayers(PlayerRenderer instance, RenderLayer renderLayer, Operation<Boolean> original) {
		if (renderLayer instanceof HumanoidArmorLayer armor) this.armorLayer = armor;
		if (renderLayer instanceof net.minecraft.client.renderer.entity.layers.ArrowLayer arrow)
			this.arrowLayer = arrow;
		return original.call(instance, renderLayer);
	}

	@Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
	public void render(AbstractClientPlayer abstractClientPlayer, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
		if (!(abstractClientPlayer instanceof net.minecraft.client.player.LocalPlayer)) return;
		LivingEntityRenderStateAccessor accessor = (LivingEntityRenderStateAccessor) (Object) abstractClientPlayer;
		accessor.setEntity(abstractClientPlayer);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void initLeashLayer(EntityRendererProvider.Context context, boolean bl, CallbackInfo ci) {
		this.leashRenderLayer = new LeashRenderLayer<>((PlayerRenderer) (Object) this);
		((LivingEntityRendererAccesor) this).ismah$addLayer(this.leashRenderLayer);
	}

	@Inject(method = "renderHand", at = @At("HEAD"))
	private void ismah$saveHandPoseStack(CallbackInfo ci, @Local(argsOnly = true) PoseStack poseStack) {
		ismah$savedPose = new Matrix4f(poseStack.last().pose());
		ismah$savedNormal = new Matrix3f(poseStack.last().normal());
	}

	@WrapOperation(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"))
	private void renderFirstPersonLayers(
			ModelPart modelPart, PoseStack poseStack, com.mojang.blaze3d.vertex.VertexConsumer vertexConsumer,
			int light, int overlay, Operation<Void> original,
			@Local(ordinal = 0) MultiBufferSource multiBufferSource,
			@Local(ordinal = 0) AbstractClientPlayer abstractClientPlayer
	) {
		if (modelPart != this.model.leftArm && modelPart != this.model.rightArm) {
			original.call(modelPart, poseStack, vertexConsumer, light, overlay);
			return;
		}

		HumanoidArm arm = modelPart == this.model.leftArm ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
		float xRot = modelPart.xRot, yRot = modelPart.yRot, zRot = modelPart.zRot;
		float x = modelPart.x, y = modelPart.y, z = modelPart.z;

		var client = Minecraft.getInstance();
		var player = client.player;
		if (player == null) {
			original.call(modelPart, poseStack, vertexConsumer, light, overlay);
			return;
		}
		if (!(abstractClientPlayer instanceof net.minecraft.client.player.LocalPlayer localPlayer)) return;
		LivingEntityRenderStateAccessor accessor = (LivingEntityRenderStateAccessor) localPlayer;
		accessor.setHumanoidArm(arm);
		accessor.setFirstPersonArmPose(xRot, yRot, zRot, x, y, z);

		if (!player.isInvisible()) {
			original.call(modelPart, poseStack, vertexConsumer, light, overlay);
		}

		// Restore PoseStack to pre-mod state (before EMF etc. modified it)
		poseStack.pushPose();
		poseStack.last().pose().set(ismah$savedPose);
		poseStack.last().normal().set(ismah$savedNormal);

		if (this.armorLayer != null) {
			this.armorLayer.render(poseStack, multiBufferSource, light, abstractClientPlayer, 0f, 0f, 0f, 0f, 0f, 0f);
		}
		if (this.arrowLayer != null) {
			this.arrowLayer.render(poseStack, multiBufferSource, light, abstractClientPlayer, 0f, 0f, 0f, 0f, 0f, 0f);
		}
		if (this.leashRenderLayer != null) {
			this.leashRenderLayer.render(poseStack, multiBufferSource, light, abstractClientPlayer, 0f, 0f, 0f, 0f, 0f, 0f);
		}

		poseStack.popPose();
		accessor.setHumanoidArm(null);
	}
}
*///?}


//? if >=1.21.2 && <1.21.9{
/*@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<net.minecraft.client.player.AbstractClientPlayer, PlayerRenderState, PlayerModel>
		implements FirstPersonLayerHolder {
	@Unique
	private HumanoidArmorLayer<PlayerRenderState, PlayerModel, PlayerModel> armorLayer;
	@Unique
	private net.minecraft.client.renderer.entity.layers.ArrowLayer arrowLayer;
	@Unique
	private LeashRenderLayer<PlayerRenderState, PlayerModel> leashRenderLayer;
	@Unique
	private Matrix4f ismah$savedPose;
	@Unique
	private Matrix3f ismah$savedNormal;

	@Override
	public @Nullable Object ismah$getArmorLayer() {
		return this.armorLayer;
	}

	@Override
	public @Nullable Object ismah$getArrowLayer() {
		return this.arrowLayer;
	}

	@Override
	public @Nullable Object ismah$getLeashRenderLayer() {
		return this.leashRenderLayer;
	}

	protected PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", at = @At("RETURN"))
	private void onExtractRenderState(net.minecraft.client.player.AbstractClientPlayer player, PlayerRenderState state, float f, CallbackInfo ci) {
		((LivingEntityRenderStateAccessor) state).setEntity(player);
	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;addLayer(Lnet/minecraft/client/renderer/entity/layers/RenderLayer;)Z"))
	private boolean captureLayers(PlayerRenderer instance, RenderLayer renderLayer, Operation<Boolean> original) {
		if (renderLayer instanceof HumanoidArmorLayer armor) this.armorLayer = armor;
		if (renderLayer instanceof net.minecraft.client.renderer.entity.layers.ArrowLayer arrow)
			this.arrowLayer = arrow;
		return original.call(instance, renderLayer);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void initLeashLayer(EntityRendererProvider.Context context, boolean bl, CallbackInfo ci) {
		this.leashRenderLayer = new LeashRenderLayer<>((PlayerRenderer) (Object) this);
		((LivingEntityRendererAccesor) this).ismah$addLayer(this.leashRenderLayer);
	}

	@Inject(method = "renderHand", at = @At("HEAD"))
	private void ismah$saveHandPoseStack(CallbackInfo ci, @Local(argsOnly = true) PoseStack poseStack) {
		ismah$savedPose = new Matrix4f(poseStack.last().pose());
		ismah$savedNormal = new Matrix3f(poseStack.last().normal());
	}

	@WrapOperation(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"))
	private void renderFirstPersonLayers(
			ModelPart modelPart, PoseStack poseStack, com.mojang.blaze3d.vertex.VertexConsumer vertexConsumer,
			int light, int overlay, Operation<Void> original,
			@Local(ordinal = 0) net.minecraft.client.renderer.MultiBufferSource multiBufferSource
	) {
		if (modelPart != this.model.leftArm && modelPart != this.model.rightArm) {
			original.call(modelPart, poseStack, vertexConsumer, light, overlay);
			return;
		}

		HumanoidArm arm = modelPart == this.model.leftArm ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
		float xRot = modelPart.xRot, yRot = modelPart.yRot, zRot = modelPart.zRot;
		float x = modelPart.x, y = modelPart.y, z = modelPart.z;

		var client = Minecraft.getInstance();
		var player = client.player;
		if (player == null) {
			original.call(modelPart, poseStack, vertexConsumer, light, overlay);
			return;
		}

		var renderState = this.createRenderState();
		this.extractRenderState(player, renderState, client.getDeltaTracker().getGameTimeDeltaTicks());
		((LivingEntityRenderStateAccessor) renderState).setHumanoidArm(arm);
		((LivingEntityRenderStateAccessor) renderState).setFirstPersonArmPose(xRot, yRot, zRot, x, y, z);

		if (!player.isInvisible()) {
			original.call(modelPart, poseStack, vertexConsumer, light, overlay);
		}

		// Restore PoseStack to pre-mod state (before EMF etc. modified it)
		poseStack.pushPose();
		poseStack.last().pose().set(ismah$savedPose);
		poseStack.last().normal().set(ismah$savedNormal);

		if (this.armorLayer != null) {
			this.armorLayer.render(poseStack, multiBufferSource, light, renderState, 0f, 0f);
		}
		if (this.arrowLayer != null && renderState.arrowCount > 0) {
			this.arrowLayer.render(poseStack, multiBufferSource, light, renderState, 0f, 0f);
		}
		if (this.leashRenderLayer != null) {
			System.out.println(3);
			this.leashRenderLayer.render(poseStack, multiBufferSource, light, renderState, 0f, 0f);
		}

		poseStack.popPose();
		((LivingEntityRenderStateAccessor) renderState).setHumanoidArm(null);
	}
}
*///?}


//? if >= 1.21.9 {
@Mixin(AvatarRenderer.class)
public abstract class PlayerRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity>
		extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel>
		implements FirstPersonLayerHolder {

	@Unique
	private HumanoidArmorLayer<AvatarRenderState, PlayerModel, PlayerModel> armorLayer;
	@Unique
	private net.minecraft.client.renderer.entity.layers.ArrowLayer arrowLayer;
	@Unique
	private LeashRenderLayer<AvatarRenderState, PlayerModel> leashRenderLayer;
	@Unique
	private Matrix4f ismah$savedPose;
	@Unique
	private Matrix3f ismah$savedNormal;

	@Override
	public @Nullable Object ismah$getArmorLayer() {
		return this.armorLayer;
	}

	@Override
	public @Nullable Object ismah$getArrowLayer() {
		return this.arrowLayer;
	}

	@Override
	public @Nullable Object ismah$getLeashRenderLayer() {
		return this.leashRenderLayer;
	}

	protected PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel entityModel, float shadowRadius) {
		super(context, entityModel, shadowRadius);
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
			at = @At("RETURN"))
	private void onExtractRenderState(AvatarlikeEntity avatar, AvatarRenderState state, float f, CallbackInfo ci) {
		if(state instanceof LivingEntityRenderStateAccessor) return;
		((LivingEntityRenderStateAccessor) state).setEntity(avatar);
	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;addLayer(Lnet/minecraft/client/renderer/entity/layers/RenderLayer;)Z"))
	private boolean captureLayers(AvatarRenderer<?> instance,
	                              RenderLayer<AvatarRenderState, PlayerModel> renderLayer,
	                              Operation<Boolean> original) {
		if (renderLayer instanceof HumanoidArmorLayer<?, ?, ?> armor) {
			@SuppressWarnings("unchecked")
			var casted = (HumanoidArmorLayer<AvatarRenderState, PlayerModel, PlayerModel>) armor;
			this.armorLayer = casted;
		}
		if (renderLayer instanceof net.minecraft.client.renderer.entity.layers.ArrowLayer arrow)
			this.arrowLayer = arrow;
		return original.call(instance, renderLayer);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void initLeashLayer(EntityRendererProvider.Context context, boolean bl, CallbackInfo ci) {
		this.leashRenderLayer = new LeashRenderLayer<>((AvatarRenderer) (Object) this);
		((LivingEntityRendererAccesor) this).ismah$addLayer(this.leashRenderLayer);
	}

	// Captura el PoseStack ANTES de que EMF u otro mod lo modifique
	@Inject(method = "renderHand", at = @At("HEAD"))
	private void ismah$saveHandPoseStack(CallbackInfo ci, @Local(argsOnly = true) PoseStack poseStack) {
		ismah$savedPose = new Matrix4f(poseStack.last().pose());
		ismah$savedNormal = new Matrix3f(poseStack.last().normal());
	}

	//? if <1.21.11{
	/*@WrapOperation(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"))
	 *///?}
	//? if >=1.21.11{
	@WrapOperation(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"))
			//?}
	private void renderFirstPersonLayers(SubmitNodeCollector instance, ModelPart modelPart, PoseStack poseStack,
	                                     RenderType renderType, int light, int overlay,
	                                     TextureAtlasSprite textureAtlasSprite, Operation<Void> original) {
		if (modelPart != this.model.leftArm && modelPart != this.model.rightArm) {
			original.call(instance, modelPart, poseStack, renderType, light, overlay, textureAtlasSprite);
			return;
		}

		HumanoidArm arm = modelPart == this.model.leftArm ? HumanoidArm.LEFT : HumanoidArm.RIGHT;

		float xRot = modelPart.xRot, yRot = modelPart.yRot, zRot = modelPart.zRot;
		float x = modelPart.x, y = modelPart.y, z = modelPart.z;

		var client = Minecraft.getInstance();
		var player = client.player;
		if (player == null) {
			original.call(instance, modelPart, poseStack, renderType, light, overlay, textureAtlasSprite);
			return;
		}

		var renderState = this.createRenderState();
		this.extractRenderState((AvatarlikeEntity) player, renderState, client.getDeltaTracker().getGameTimeDeltaTicks());
		if(renderState instanceof LivingEntityRenderStateAccessor) return;
		((LivingEntityRenderStateAccessor) renderState).setHumanoidArm(arm);
		((LivingEntityRenderStateAccessor) renderState).setFirstPersonArmPose(xRot, yRot, zRot, x, y, z);
		renderState.outlineColor = 0;

		if (!player.isInvisible()) {
			original.call(instance, modelPart, poseStack, renderType, light, overlay, textureAtlasSprite);
		}

		poseStack.pushPose();
		poseStack.last().pose().set(ismah$savedPose);
		poseStack.last().normal().set(ismah$savedNormal);

		if (this.armorLayer != null) {
			this.armorLayer.submit(poseStack, instance, light, renderState, 0f, 0f);
		}

		if (this.arrowLayer != null && renderState.arrowCount > 0) {
			this.arrowLayer.submit(poseStack, instance, light, renderState, 0f, 0f);
		}

		if (this.leashRenderLayer != null) {
			this.leashRenderLayer.submit(poseStack, instance, light, renderState, 0f, 0f);
		}

		poseStack.popPose();
	}
}
//?}
