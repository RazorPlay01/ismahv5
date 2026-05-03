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
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.HumanoidArm;
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

@Mixin(/*? >=1.21.9 {*/AvatarRenderer/*? } else { */ /*PlayerRenderer *//*? } */.class)
public abstract class PlayerRendererMixin
//? < 1.21.2 {
/*extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>
 *///?} >= 1.21.2 && < 1.21.9 {
/*extends LivingEntityRenderer<AbstractClientPlayer, PlayerRenderState, PlayerModel>
 *///?} >= 1.21.11 {
<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel>
//?}
implements FirstPersonLayerHolder {
	@Unique
	private HumanoidArmorLayer armorLayer;
	@Unique
	private ArrowLayer arrowLayer;
	@Unique
	private LeashRenderLayer leashRenderLayer;
	@Unique
	private Matrix4f ismah$savedPose;
	@Unique
	private Matrix3f ismah$savedNormal;

	protected PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel entityModel, float shadowRadius) {
		super(context, entityModel, shadowRadius);
	}

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

	@Inject(method = "<init>", at = @At("RETURN"))
	private void initLeashLayer(EntityRendererProvider.Context context, boolean bl, CallbackInfo ci) {
		this.leashRenderLayer = new LeashRenderLayer<>((/*? >=1.21.9 {*/AvatarRenderer/*? } else { */ /*PlayerRenderer *//*? } */) (Object) this);
		((LivingEntityRendererAccesor) this).ismah$addLayer(this.leashRenderLayer);
	}

	@Inject(method = "renderHand", at = @At("HEAD"))
	private void ismah$saveHandPoseStack(CallbackInfo ci, @Local(argsOnly = true) PoseStack poseStack) {
		ismah$savedPose = new Matrix4f(poseStack.last().pose());
		ismah$savedNormal = new Matrix3f(poseStack.last().normal());
	}

	@Unique
	private void renderExtraFirstPersonLayers(PoseStack poseStack, Object instance, int light, Object renderState) {
		poseStack.pushPose();
		try {
			poseStack.last().pose().set(ismah$savedPose);
			poseStack.last().normal().set(ismah$savedNormal);

			if (this.armorLayer != null) {
				//? < 1.21.2 {
				/*this.armorLayer.render(poseStack, (MultiBufferSource) instance, light, (AbstractClientPlayer) renderState, 0f, 0f, 0f, 0f, 0f, 0f);
				*///?} >= 1.21.2 && < 1.21.9 {
				/*this.armorLayer.render(poseStack, (MultiBufferSource) instance, light, (PlayerRenderState) renderState, 0f, 0f);
				 *///?} >= 1.21.11 {
				this.armorLayer.submit(poseStack, (SubmitNodeCollector) instance, light, (AvatarRenderState) renderState, 0f, 0f);
				 //?}
			}
			if (this.arrowLayer != null) {
				//? < 1.21.2 {
				/*this.arrowLayer.render(poseStack, (MultiBufferSource) instance, light, (AbstractClientPlayer) renderState, 0f, 0f, 0f, 0f, 0f, 0f);
				*///?} >= 1.21.2 && < 1.21.9 {
				/*this.arrowLayer.render(poseStack, (MultiBufferSource) instance, light, (PlayerRenderState) renderState, 0f, 0f);
				 *///?} >= 1.21.11 {
				this.arrowLayer.submit(poseStack, (SubmitNodeCollector) instance, light, (AvatarRenderState) renderState, 0f, 0f);
				 //?}
			}
			if (this.leashRenderLayer != null) {
				//? < 1.21.2 {
				/*this.leashRenderLayer.render(poseStack, (MultiBufferSource) instance, light, (AbstractClientPlayer) renderState, 0f, 0f, 0f, 0f, 0f, 0f);
				*///?} >= 1.21.2 && < 1.21.9 {
				/*this.leashRenderLayer.render(poseStack, (MultiBufferSource) instance, light, (PlayerRenderState) renderState, 0f, 0f);
				 *///?} >= 1.21.11 {
				this.leashRenderLayer.submit(poseStack, (SubmitNodeCollector) instance, light, (AvatarRenderState) renderState, 0f, 0f);
				 //?}
			}
		} finally {
			poseStack.popPose();
		}
	}

	//? < 1.21.2 {
	/*@Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
	public void render(AbstractClientPlayer state, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
		*///?} >= 1.21.2 && < 1.21.9 {
	/*@Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", at = @At("RETURN"))
	private void onExtractRenderState(AbstractClientPlayer avatar, PlayerRenderState state, float f, CallbackInfo ci) {
	*///?} >= 1.21.11 {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("RETURN"))
	private void onExtractRenderState(AvatarlikeEntity avatar, AvatarRenderState state, float f, CallbackInfo ci) {
		//?}
		if (!(state instanceof LivingEntityRenderStateAccessor)) return;
		((LivingEntityRenderStateAccessor) state).setEntity(/*? < 1.21.2 {*//*state*//*? } else { */ avatar /*? } */);
	}

	//? < 1.21.2 {
	/*@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;addLayer(Lnet/minecraft/client/renderer/entity/layers/RenderLayer;)Z"))
	private boolean captureLayers(PlayerRenderer instance, RenderLayer renderLayer, Operation<Boolean> original) {
		*///?} >= 1.21.2 && < 1.21.9 {
	/*@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;addLayer(Lnet/minecraft/client/renderer/entity/layers/RenderLayer;)Z"))
	private boolean captureLayers(PlayerRenderer instance, RenderLayer renderLayer, Operation<Boolean> original) {
	*///?} >= 1.21.11 {
	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;addLayer(Lnet/minecraft/client/renderer/entity/layers/RenderLayer;)Z"))
	private boolean captureLayers(AvatarRenderer<?> instance, RenderLayer<AvatarRenderState, PlayerModel> renderLayer, Operation<Boolean> original) {
		//?}
		if (renderLayer instanceof HumanoidArmorLayer armor)
			this.armorLayer = armor;
		if (renderLayer instanceof ArrowLayer arrow)
			this.arrowLayer = arrow;
		return original.call(instance, renderLayer);
	}

	//? <1.21.2 {
	/*@WrapOperation(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"))
	private void renderFirstPersonLayers(ModelPart modelPart, PoseStack poseStack, com.mojang.blaze3d.vertex.VertexConsumer vertexConsumer, int light, int overlay, Operation<Void> original, @Local(ordinal = 0) MultiBufferSource instance, @Local(ordinal = 0) AbstractClientPlayer renderState) {
	*///?} >=1.21.2 && <1.21.9 {
	/*@WrapOperation(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"))
	private void renderFirstPersonLayers(ModelPart modelPart, PoseStack poseStack, com.mojang.blaze3d.vertex.VertexConsumer vertexConsumer, int light, int overlay, Operation<Void> original, @Local(ordinal = 0) MultiBufferSource instance) {
	*///?} >= 1.21.9 && <1.21.11 {
	/*@WrapOperation(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"))
	private void renderFirstPersonLayers(SubmitNodeCollector instance, ModelPart modelPart, PoseStack poseStack, RenderType renderType, int light, int overlay, TextureAtlasSprite textureAtlasSprite, Operation<Void> original) {
	 */
	//?} >= 1.21.11 {
	@WrapOperation(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"))
	private void renderFirstPersonLayers(SubmitNodeCollector instance, ModelPart modelPart, PoseStack poseStack, RenderType renderType, int light, int overlay, TextureAtlasSprite textureAtlasSprite, Operation<Void> original) {
	//?}
		if (modelPart != this.model.leftArm && modelPart != this.model.rightArm) {
			//? < 1.21.9 {
			/*original.call(modelPart, poseStack, vertexConsumer, light, overlay);
			*///?} >= 1.21.9 {
			original.call(instance, modelPart, poseStack, renderType, light, overlay, textureAtlasSprite);
			//?}
			return;
		}

		HumanoidArm arm = modelPart == this.model.leftArm ? HumanoidArm.LEFT : HumanoidArm.RIGHT;

		float xRot = modelPart.xRot, yRot = modelPart.yRot, zRot = modelPart.zRot;
		float x = modelPart.x, y = modelPart.y, z = modelPart.z;

		var client = Minecraft.getInstance();
		var player = client.player;
		if (player == null) {
			//? < 1.21.9 {
			/*original.call(modelPart, poseStack, vertexConsumer, light, overlay);
			*///?} >= 1.21.9 {
			original.call(instance, modelPart, poseStack, renderType, light, overlay, textureAtlasSprite);
			//?}
			return;
		}

		//? >= 1.21.2 {
		var renderState = this.createRenderState();
		this.extractRenderState(/*? >= 1.21.9 { */(AvatarlikeEntity)/*? } */ player, renderState, client.getDeltaTracker().getGameTimeDeltaTicks());
		//?}
		if (!(renderState instanceof LivingEntityRenderStateAccessor accessor)) {
			//? < 1.21.9 {
			/*original.call(modelPart, poseStack, vertexConsumer, light, overlay);
			*///?} >= 1.21.9 {
			original.call(instance, modelPart, poseStack, renderType, light, overlay, textureAtlasSprite);
			//?}
			return;
		}
		accessor.setHumanoidArm(arm);
		accessor.setFirstPersonArmPose(xRot, yRot, zRot, x, y, z);

		if (!player.isInvisible()) {
			//? < 1.21.9 {
			/*original.call(modelPart, poseStack, vertexConsumer, light, overlay);
			*///?} >= 1.21.9 {
			original.call(instance, modelPart, poseStack, renderType, light, overlay, textureAtlasSprite);
			//?}
		}

		renderExtraFirstPersonLayers(poseStack, instance, light, renderState);
		//? < 1.21.9 {
		/*accessor.setHumanoidArm(null);
		*///?}
	}
}
