package possible_triangle.armorpieces.item.armor;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import possible_triangle.armorpieces.Config;
import possible_triangle.armorpieces.Content;

import java.util.Optional;
import java.util.UUID;

public class SporeChestplate extends BaseArmor {

    public SporeChestplate() {
        super(new CustomArmorMaterial(
                        "warp_boots",
                        EquipmentSlotType.CHEST,
                        15,
                        4,
                        12,
                        0.0F, () -> Ingredient.fromItems(Content.MYCELIUM_SPORE.orElse(Items.STRING))),
                EquipmentSlotType.CHEST);
    }

    public Optional<MobEntity> findSource(DamageSource source) {
        if (source.getTrueSource() instanceof MobEntity) return Optional.of((MobEntity) source.getTrueSource());
        if (source.getImmediateSource() instanceof MobEntity)
            return Optional.of((MobEntity) source.getImmediateSource());
        return Optional.empty();
    }

    public Optional<LivingEntity> findTarget(MobEntity entity, LivingEntity sporer) {
        AxisAlignedBB box = new AxisAlignedBB(entity.getPositionVec(), entity.getPositionVec()).grow(10);
        return entity.world.getEntitiesWithinAABB(LivingEntity.class, box, e -> {
            UUID uuid = e.getUniqueID();
            return !uuid.equals(sporer.getUniqueID()) && !uuid.equals(entity.getUniqueID())
                    && sporer.canAttack(e) && e.canAttack(sporer);
        }).stream().findAny();
    }

    @Override
    public boolean onDamage(LivingEntity entity, ItemStack stack, DamageSource source) {
        findSource(source).ifPresent(mob -> {
            if (Math.random() <= Config.SPORE_CHANGE) {
                findTarget(mob, entity).ifPresent(target -> {
                    mob.setAttackTarget(target);

                    ServerWorld world = (ServerWorld) entity.world;
                    Vector3d pos = target.getPositionVec().add(0, target.getEyeHeight(), 0);
                    world.spawnParticle(ParticleTypes.MYCELIUM, pos.x, pos.y, pos.z, 30, 0.2, 0.1, 0.2, 1);
                    world.playMovingSound(null, target, SoundEvents.ENTITY_WITCH_DRINK, SoundCategory.HOSTILE, 1F, 0.3F);

                    stack.damageItem(4, entity, $ -> {
                    });
                });
            }
        });

        return false;
    }

    @Override
    public void buildRecipe(ShapedRecipeBuilder builder) {
        IItemProvider spore = Content.MYCELIUM_SPORE.map(i -> (IItemProvider) i).orElse(Blocks.MYCELIUM);
        builder
                .patternLine("# #")
                .patternLine("#s#")
                .patternLine("###")
                .key('s', spore)
                .key('#', Items.STRING)
                .addCriterion("ingredients", InventoryChangeTrigger.Instance.forItems(spore, Items.STRING));
    }

}
