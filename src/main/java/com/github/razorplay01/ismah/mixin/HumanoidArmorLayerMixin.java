package com.github.razorplay01.ismah.mixin;

import com.github.razorplay01.ismah.client.util.LivingEntityRenderStateAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >=1.21.2{
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
 //?}
//? if >=1.21.9{
import net.minecraft.client.renderer.SubmitNodeCollector;
 //?}

@Mixin(HumanoidArmorLayer.class)
//? < 1.21.2 {
/*public abstract class HumanoidArmorLayerMixin<T extends net.minecraft.world.entity.LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
*///?} >= 1.21.2 {
	public abstract class HumanoidArmorLayerMixin<T extends HumanoidRenderState, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
	 //?}

	@Unique
	private LivingEntityRenderStateAccessor ismah$renderState;

	protected HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderLayerParent) {
		super(renderLayerParent);
	}

	//? < 1.21.2 && fabric || forge {
	/*@WrapWithCondition(
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V"
			)
	)
	private boolean renderChest(HumanoidArmorLayer instance, PoseStack poseStack, MultiBufferSource multiBufferSource, LivingEntity livingEntity, EquipmentSlot equipmentSlot, int i, HumanoidModel humanoidModel, @Local(ordinal = 0) T humanoidRenderState) {
		*///?} < 1.21.2 && neoforge {
	/*@WrapWithCondition(
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V"
			)
	)
	private boolean renderChest(HumanoidArmorLayer instance, PoseStack poseStack, MultiBufferSource multiBufferSource, LivingEntity livingEntity, EquipmentSlot equipmentSlot, int i, HumanoidModel humanoidModel, float f, float g, float h, float j, float k, float l, @Local(ordinal = 0) T humanoidRenderState) {*/
		//?} >= 1.21.2 && < 1.21.9 {
	/*@WrapWithCondition(
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V"
			)
	)
	private boolean renderChest(HumanoidArmorLayer instance, PoseStack poseStack, MultiBufferSource multiBufferSource, ItemStack itemStack, EquipmentSlot equipmentSlot, int i, HumanoidModel humanoidModel, @Local(ordinal = 0) T humanoidRenderState) {
		*///?} >= 1.21.9 {
	@WrapWithCondition(
			method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V"
			)
	)
	private boolean renderChest(HumanoidArmorLayer instance, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, EquipmentSlot equipmentSlot, int i, HumanoidRenderState humanoidRenderState) {
	//?}
		if (!(humanoidRenderState instanceof LivingEntityRenderStateAccessor accessor)) return false;
		ismah$renderState = accessor;
		return accessor.getHumanoidArm() == null || equipmentSlot == EquipmentSlot.CHEST;
	}


	//? < 1.21.9 {
	/*@Inject(method = "setPartVisibility", at = @At("RETURN"))
	private void onlyRenderArmInFirstPerson(HumanoidModel humanoidModel, EquipmentSlot equipmentSlot, CallbackInfo ci) {
		HumanoidArm arm = ismah$renderState.getHumanoidArm();
		if (arm == null) return;
		humanoidModel.setAllVisible(false);
		if (equipmentSlot == EquipmentSlot.CHEST) {
			if (arm == HumanoidArm.RIGHT) {
				humanoidModel.rightArm.visible = true;
			} else {
				humanoidModel.leftArm.visible = true;
			}
		}
	}
	*///?}
}
