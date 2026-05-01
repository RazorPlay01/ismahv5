package com.github.razorplay01.ismah.mixin;

import com.github.razorplay01.ismah.client.util.LivingEntityRenderStateAccessor;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
//? if >=1.21.2{
/*import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
 *///?}
//? if >=1.21.9{
/*import net.minecraft.client.renderer.SubmitNodeCollector;
*///?}

//? if <1.21.2{
@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends net.minecraft.world.entity.LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
	@Unique private net.minecraft.world.entity.LivingEntity ismah$livingEntity;
	protected HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderLayerParent) {
		super(renderLayerParent);
	}

	//? if fabric || forge {
	@WrapWithCondition(
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V"
			)
	)
	private boolean renderChest(
			HumanoidArmorLayer instance,
			PoseStack poseStack,
			net.minecraft.client.renderer.MultiBufferSource multiBufferSource,
			net.minecraft.world.entity.LivingEntity livingEntity,
			EquipmentSlot equipmentSlot,
			int i,
			HumanoidModel humanoidModel,
			@com.llamalad7.mixinextras.sugar.Local(ordinal = 0) T livingEntityInstance
	) {
	//? }
	//? if neoforge {
		/*@WrapWithCondition(
				method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
				at = @At(
						value = "INVOKE",
						target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V"
				)
		)
		private boolean renderChest(
				HumanoidArmorLayer instance,
				PoseStack poseStack,
				net.minecraft.client.renderer.MultiBufferSource multiBufferSource,
				net.minecraft.world.entity.LivingEntity livingEntity,
				EquipmentSlot equipmentSlot,
				int i,
				HumanoidModel humanoidModel,
				float f, float g, float h, float j, float k, float l,
				@com.llamalad7.mixinextras.sugar.Local(ordinal = 0) T livingEntityInstance
	) {
	*///? }

		ismah$livingEntity = livingEntityInstance;
		if (!(livingEntityInstance instanceof net.minecraft.client.player.LocalPlayer)) return true;
		LivingEntityRenderStateAccessor accessor = (LivingEntityRenderStateAccessor) (Object) livingEntityInstance;
		return accessor.getHumanoidArm() == null || equipmentSlot == EquipmentSlot.CHEST;
	}

	@org.spongepowered.asm.mixin.injection.Inject(
			method = "setPartVisibility",
			at = @At("RETURN")
	)
	private void onlyRenderArmInFirstPerson(
			HumanoidModel humanoidModel, EquipmentSlot equipmentSlot, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci
	) {
		if (!(ismah$livingEntity instanceof net.minecraft.client.player.LocalPlayer)) return;
		LivingEntityRenderStateAccessor accessor = (LivingEntityRenderStateAccessor) (Object) ismah$livingEntity;
		net.minecraft.world.entity.HumanoidArm arm = accessor.getHumanoidArm();
		if (arm == null) return;
		humanoidModel.setAllVisible(false);
		if (arm == net.minecraft.world.entity.HumanoidArm.RIGHT) {
			if (equipmentSlot == EquipmentSlot.CHEST) humanoidModel.rightArm.visible = true;
			if (equipmentSlot == EquipmentSlot.LEGS || equipmentSlot == EquipmentSlot.FEET)
				humanoidModel.rightLeg.visible = true;
		} else {
			if (equipmentSlot == EquipmentSlot.CHEST) humanoidModel.leftArm.visible = true;
			if (equipmentSlot == EquipmentSlot.LEGS || equipmentSlot == EquipmentSlot.FEET)
				humanoidModel.leftLeg.visible = true;
		}
	}
}
//?}

//? if >=1.21.2{
/*@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> extends RenderLayer<S, M> {
	protected HumanoidArmorLayerMixin(RenderLayerParent<S, M> renderLayerParent) {
		super(renderLayerParent);
	}

	@Unique
	private S renderState;

	//? if <1.21.9{
	@WrapWithCondition(
			method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V"
			)
	)
	private boolean renderChest(
			HumanoidArmorLayer instance,
			PoseStack poseStack,
			net.minecraft.client.renderer.MultiBufferSource multiBufferSource,
			ItemStack itemStack,
			EquipmentSlot equipmentSlot,
			int i,
			HumanoidModel humanoidModel,
			@com.llamalad7.mixinextras.sugar.Local(ordinal = 0) S humanoidRenderState
	) {
		renderState = humanoidRenderState;
		return ((LivingEntityRenderStateAccessor) humanoidRenderState).getHumanoidArm() == null || equipmentSlot == EquipmentSlot.CHEST;
	}

	@org.spongepowered.asm.mixin.injection.Inject(
			method = "setPartVisibility",
			at = @At("RETURN")
	)
	private void onlyRenderArmInFirstPerson(
			HumanoidModel humanoidModel, EquipmentSlot equipmentSlot, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci
	) {
		net.minecraft.world.entity.HumanoidArm arm = ((LivingEntityRenderStateAccessor) renderState).getHumanoidArm();
		if (arm == null) return;
		humanoidModel.setAllVisible(false);
		if (arm == net.minecraft.world.entity.HumanoidArm.RIGHT) {
			if (equipmentSlot == EquipmentSlot.CHEST) humanoidModel.rightArm.visible = true;
			if (equipmentSlot == EquipmentSlot.LEGS || equipmentSlot == EquipmentSlot.FEET)
				humanoidModel.rightLeg.visible = true;
		} else {
			if (equipmentSlot == EquipmentSlot.CHEST) humanoidModel.leftArm.visible = true;
			if (equipmentSlot == EquipmentSlot.LEGS || equipmentSlot == EquipmentSlot.FEET)
				humanoidModel.leftLeg.visible = true;
		}
	}
	//?}


	//? if >=1.21.9{
	/^@WrapWithCondition(
			method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V"
			)
	)
	private boolean renderChest(
			HumanoidArmorLayer instance,
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			ItemStack itemStack,
			EquipmentSlot equipmentSlot,
			int i,
			HumanoidRenderState humanoidRenderState
	) {
		return ((LivingEntityRenderStateAccessor) humanoidRenderState).getHumanoidArm() == null || equipmentSlot == EquipmentSlot.CHEST;
	}
	^///?}
}
*///?}
