package com.github.razorplay01.ismah.mixin;

import com.github.razorplay01.ismah.client.util.LeashStateAccess;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

//? if < 1.21.1 {
import net.minecraft.world.entity.Mob;
//?}
//? if >= 1.21.1 {
/*import net.minecraft.world.entity.Leashable;
 *///?}

@Mixin(ClientPacketListener.class)
public abstract class LeashAttachMixin {
	@Shadow
	private ClientLevel level;

	//? if < 1.21.1 {
	@Inject(method = "handleEntityLinkPacket(Lnet/minecraft/network/protocol/game/ClientboundSetEntityLinkPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;setDelayedLeashHolderId(I)V"))
	//?}
	//? if >= 1.21.1 {
	/*@Inject(method = "handleEntityLinkPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Leashable;setDelayedLeashHolderId(I)V"))
	*///?}
	private void onEntityAttach(ClientboundSetEntityLinkPacket clientboundSetEntityLinkPacket, CallbackInfo ci) {
		Entity entity1 = this.level.getEntity(clientboundSetEntityLinkPacket.getSourceId());
		if (entity1 instanceof /*? if >=1.21.1 {*//*Leashable*//*?} else {*/ Mob/*?}*/ target) {
			Entity currentLeashHolder = getLeashHolder(target);

			if (clientboundSetEntityLinkPacket.getDestId() == 0) {
				if (currentLeashHolder instanceof AbstractClientPlayer player) {
					AABB box = new AABB(player.getX() - 16.0, player.getY() - 16.0, player.getZ() - 16.0,
							player.getX() + 16.0, player.getY() + 16.0, player.getZ() + 16.0);
					List<Entity> nearby = player.level().getEntitiesOfClass(Entity.class, box);

					boolean hasOtherLeashedEntities = false;
					for (Entity entity : nearby) {
						if (entity instanceof /*? if >=1.21.1 {*//*Leashable*//*?} else {*/ Mob/*?}*/ leashable && leashable != target) {
							if (getLeashHolder(leashable) == player) {
								hasOtherLeashedEntities = true;
								break;
							}
						}
					}

					if (!hasOtherLeashedEntities) {
						if (!(player instanceof LeashStateAccess leashAccess)) return;
						leashAccess.setLeashState(false);
					}
				}
			} else {
				Entity newLeashHolder = level.getEntity(clientboundSetEntityLinkPacket.getDestId());
				if (newLeashHolder instanceof LeashStateAccess player) {
					player.setLeashState(true);
				} else {
					if (currentLeashHolder instanceof AbstractClientPlayer player) {
						AABB box = new AABB(player.getX() - 16.0, player.getY() - 16.0, player.getZ() - 16.0,
								player.getX() + 16.0, player.getY() + 16.0, player.getZ() + 16.0);
						List<Entity> nearby = player.level().getEntitiesOfClass(Entity.class, box);

						boolean hasOtherLeashedEntities = false;
						for (Entity entity : nearby) {
							if (entity instanceof /*? if >=1.21.1 {*//*Leashable*//*?} else {*/ Mob/*?}*/ leashable && leashable != target) {
								if (getLeashHolder(leashable) == player) {
									hasOtherLeashedEntities = true;
									break;
								}
							}
						}

						if (!hasOtherLeashedEntities) {
							if (!(player instanceof LeashStateAccess leashAccess)) return;
							leashAccess.setLeashState(false);
						}
					}
				}
			}
		}
	}

	@Unique
	private Entity getLeashHolder(/*? if >=1.21.1 {*//*Leashable*//*?} else {*/ Mob/*?}*/ leashable) {
		return leashable.getLeashHolder();
	}
}
