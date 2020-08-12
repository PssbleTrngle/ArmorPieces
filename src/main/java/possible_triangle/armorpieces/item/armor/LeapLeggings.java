package possible_triangle.armorpieces.item.armor;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LeapLeggings extends BaseArmor {

    public LeapLeggings() {
        super(new CustomArmorMaterial(
                        "leap",
                        EquipmentSlotType.LEGS,
                        15,
                        4,
                        12,
                        0.0F, () -> Ingredient.EMPTY),
                EquipmentSlotType.LEGS);
    }

    @Override
    public int getRequiredCharge() {
        return 20;
    }

    @Override
    public boolean isCharging(LivingEntity entity) {
        return entity.func_233570_aj_() && !entity.isInWater() && entity.isCrouching();
    }

    public boolean canLeap(LivingEntity entity, ItemStack stack) {
        return isCharging(entity) && isCharged(stack);
    }

    @Override
    public void onJump(LivingEntity entity, ItemStack stack) {
        if (canLeap(entity, stack)) {
            ServerWorld world = (ServerWorld) entity.world;
            Vector3d pos = entity.getPositionVec();
            BlockPos down = new BlockPos(pos).down();
            float r = 1F;

            Map<BlockPos, IParticleData> blocks = new HashMap<>();
            int ri = (int) Math.ceil(r);
            for (int x = -ri - 1; x <= ri + 1; x++)
                for (int z = -ri - 1; z <= ri + 1; z++) {

                    BlockPos block = down.add(x, 0, z);
                    BlockState state = world.getBlockState(block);
                    FluidState fluid = state.getFluidState();

                    if (state.isSolid()) {
                        blocks.put(block, new BlockParticleData(ParticleTypes.BLOCK, state));
                    } else if (!fluid.isEmpty()) {
                        if (fluid.getFluid().isIn(FluidTags.LAVA))
                            blocks.put(block, ParticleTypes.LAVA);
                        else if (fluid.getFluid().isIn(FluidTags.WATER))
                            blocks.put(block, ParticleTypes.SPLASH);
                    }
                }

            world.playMovingSound(null, entity, SoundEvents.ENTITY_PLAYER_BIG_FALL, entity.getSoundCategory(), 1F, 0F);

            for (int i = 0; i < 360; i += 3) {
                float x = MathHelper.sin((float) Math.PI * (i / 180F)) * r;
                float z = MathHelper.cos((float) Math.PI * (i / 180F)) * r;

                BlockPos block = new BlockPos(pos.add(x, -1, z));
                Optional.ofNullable(blocks.get(block)).ifPresent(data ->
                        world.spawnParticle(data, pos.x + x, pos.y, pos.z + z, 1, 0, 0, 0, 1)
                );
            }

            stack.damageItem(4, entity, $ -> {
            });
        }
    }

    @Override
    public void onWornTick(LivingEntity entity, ItemStack stack) {
        ServerWorld world = (ServerWorld) entity.world;

        if (canLeap(entity, stack)) {
            EffectInstance effect = new EffectInstance(Effects.JUMP_BOOST, 1, 10, false, false);
            entity.addPotionEffect(effect);

            BlockState down = world.getBlockState(new BlockPos(entity.getPositionVec()).down());
            if (down.isSolid()) {
                Vector3d pos = entity.getPositionVec();
                BlockParticleData data = new BlockParticleData(ParticleTypes.BLOCK, down);
                world.spawnParticle(data, pos.x, pos.y, pos.z, 1, 0, 0, 0, 1);

                //int charge = getCharge(stack);
                //SoundType type = down.getSoundType();
                //if(charge % 3 == 0) world.playMovingSound(null, entity, type.getFallSound(), SoundCategory.PLAYERS, 1, 1);
            }
        }
    }

}
