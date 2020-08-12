package possible_triangle.armorpieces.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.fluid.Fluid;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import possible_triangle.armorpieces.item.armor.CrabLeggings;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    protected LivingEntityMixin(EntityType<? extends LivingEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/entity/LivingEntity;func_230285_a_(Lnet/minecraft/fluid/Fluid;)Z", cancellable = true, remap = false)
    public void canSwim(Fluid fluid, CallbackInfoReturnable<Boolean> callback) {
        if(fluid.isIn(FluidTags.WATER) && CrabLeggings.preventSwimming((Entity) (Object) this)) callback.setReturnValue(true);
    }


    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/entity/LivingEntity;travel(Lnet/minecraft/util/math/vector/Vector3d;)V")
    public void travel(Vector3d vec, CallbackInfo callback) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.isServerWorld() || entity.canPassengerSteer()) {
            if (entity.isInWater() && CrabLeggings.preventSwimming(entity)) {

                BlockPos below = new BlockPos(entity.getPositionVec().x, entity.getBoundingBox().minY - 0.5000001D, entity.getPositionVec().z);
                float slipperiness = entity.world.getBlockState(below).getSlipperiness(entity.world, below, entity);
                float velocity = this.onGround ? slipperiness * 0.91F : 0.91F;
                Vector3d vector3d5 = entity.func_233633_a_(vec, slipperiness);
                double pull = vector3d5.y;
                if (entity.isPotionActive(Effects.LEVITATION)) {
                    pull += (0.05D * (double) (entity.getActivePotionEffect(Effects.LEVITATION).getAmplifier() + 1) - vector3d5.y) * 0.2D;
                    entity.fallDistance = 0.0F;
                } else if (entity.world.isRemote && !entity.world.isBlockLoaded(below)) {
                    if (entity.getPosY() > 0.0D) {
                        pull = -0.1D;
                    } else {
                        pull = 0.0D;
                    }
                } else if (!entity.hasNoGravity()) {
                    ModifiableAttributeInstance gravity = entity.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
                    pull -= gravity != null ? gravity.getValue() : 0.08D;
                }

                entity.setMotion(vector3d5.x * (double) velocity, pull * (double) 0.98F, vector3d5.z * (double) velocity);

            }
        }
    }

}
