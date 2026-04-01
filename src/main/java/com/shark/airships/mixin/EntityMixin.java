package com.shark.airships.mixin;

import com.shark.airships.entity.AirshipEntity;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author TheSharkHour
 * @since 03/28/2024
 * <p>Entity mixin to fix the dismount height for Airships.</p>
 */
@Mixin(value = Entity.class)
public abstract class EntityMixin
{
	@Redirect(method = "setVehicle", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;height:F", opcode = Opcodes.GETFIELD))
	private float fixDismountHeight(Entity entity)
	{
		if (entity instanceof AirshipEntity)
			return (float) entity.getPassengerRidingHeight() + 2F;

		return entity.height;
	}
}
