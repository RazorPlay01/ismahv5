package com.github.razorplay01.ismah.mixin;
//? if >= 1.21.9 {

/*import com.github.razorplay01.ismah.client.util.LivingEntityRenderStateAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Collectors;

@Mixin(ModelFeatureRenderer.class)
public class ModelFeatureRendererMixin {
	//? if <1.21.11{
    @WrapOperation(
            method = "renderModel(Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/OutlineBufferSource;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V")
    )
    //?}
	//? if >=1.21.11{
	/^@WrapOperation(
			method = "renderModel(Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;Lnet/minecraft/client/renderer/rendertype/RenderType;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/OutlineBufferSource;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V")
	)
			^///?}
	private <S> void renderOnlyArms(
			Model<S> sModel,
			PoseStack poseStack,
			VertexConsumer vertexConsumer,
			int light,
			int overlay,
			int color,
			Operation<Void> original,
			@Local(argsOnly = true) SubmitNodeStorage.ModelSubmit<S> sModelSubmit
	) {
		if (!(sModel instanceof HumanoidModel<?> armorModel)
				|| !(sModelSubmit.state() instanceof HumanoidRenderState livingEntityRenderState)
				|| !(((LivingEntityRenderStateAccessor) livingEntityRenderState).getHumanoidArm() instanceof HumanoidArm arm)) {
			original.call(sModel, poseStack, vertexConsumer, light, overlay, color);
			return;
		}

		// Solo manejar visibilidad — la rotación ya fue aplicada por HumanoidModelFixMixin
		var previousVisibilities = armorModel.allParts().stream()
				.collect(Collectors.toMap(mp -> mp, mp -> mp.visible));

		setAllVisible(armorModel, false);
		if (arm == HumanoidArm.RIGHT) {
			armorModel.rightArm.visible = true;
		} else {
			armorModel.leftArm.visible = true;
		}

		original.call(sModel, poseStack, vertexConsumer, light, overlay, color);

		for (var entry : previousVisibilities.entrySet()) {
			entry.getKey().visible = entry.getValue();
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
