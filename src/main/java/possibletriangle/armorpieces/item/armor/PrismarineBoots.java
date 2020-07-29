package possibletriangle.armorpieces.item.armor;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.command.arguments.ItemPredicateArgument;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class PrismarineBoots extends BaseArmor {

    public PrismarineBoots() {
        super(new CustomArmorMaterial(
                        "prismarine",
                        EquipmentSlotType.FEET,
                        15,
                        4,
                        12,
                        0.0F, () -> Ingredient.fromItems(Items.PRISMARINE_SHARD)),
                EquipmentSlotType.FEET);
    }

    @Override
    public boolean isCharging(LivingEntity entity) {
        float h = entity.getHealth() / entity.getMaxHealth();
        return entity.isInWater() && h > 0.5F && entity.isCrouching();
    }

    @Override
    public int getRequiredCharge() {
        return 20 * 2;
    }

    private boolean targets(LivingEntity user, LivingEntity entity) {
        return !user.isEntityEqual(entity)
                && user.canAttack(entity)
                && !(entity instanceof TameableEntity && ((TameableEntity) entity).isOwner(user));
    }

    private void suck(Entity e, Vector3d to) {
        if (e.isInWater()) {

            if (e.areEyesInFluid(FluidTags.WATER) && !(e instanceof LivingEntity && ((LivingEntity) e).canBreatheUnderwater())) {
                int air = e.getAir();
                e.setAir(Math.max(0, air - 1));
            }

            float resistance = 0;
            if (e instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) e;
                resistance = Optional.ofNullable(living.getAttribute(Attributes.field_233820_c_)).map(ModifiableAttributeInstance::getValue).orElse(0.0).floatValue();
            }

            Vector3d v = e.getPositionVec().subtract(to);
            float f = 5F - resistance;
            if (f > 0) e.moveRelative(f * 0.002F, v);
        }
    }

    @Override
    public void onWornTick(LivingEntity entity, ItemStack stack) {
        ServerWorld world = (ServerWorld) entity.world;
        if (isCharged(stack)) {

            Vector3d pos = entity.getPositionVec().add(0, 1, 0);
            int charge = BaseArmor.getTimeCharged(stack);

            for (int i = -2; i <= 2; i++) {

                int l = (3 - Math.abs(i));
                int deg = charge * (10 / l) % 360;
                if (i > 0) deg += 90;
                float sin = MathHelper.sin((float) Math.PI * deg / 180F);
                float cos = MathHelper.cos((float) Math.PI * deg / 180F);

                float r = l * 0.5F;
                float h = i * 0.4F;
                world.spawnParticle(ParticleTypes.BUBBLE, pos.x + sin * r, pos.y + h, pos.z + cos * r, 1, 0, 0, 0, 0);
                world.spawnParticle(ParticleTypes.BUBBLE, pos.x - sin * r, pos.y + h, pos.z - cos * r, 1, 0, 0, 0, 0);
            }

            if (charge % 5 == 0)
                world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, entity.getSoundCategory(), 30F, 2F);

            AxisAlignedBB box = new AxisAlignedBB(pos, pos).grow(3);
            world.getEntitiesWithinAABB(ItemEntity.class, box).forEach(e -> suck(e, pos));
            world.getEntitiesWithinAABB(LivingEntity.class, box, e -> targets(entity, e)).forEach(e -> suck(e, pos));

        }
    }

    @Override
    public void buildRecipe(ShapedRecipeBuilder builder) {
        builder
                .patternLine("p p")
                .patternLine("p p")
                .key('p', Items.PRISMARINE_SHARD)
                .addCriterion("ingredients", InventoryChangeTrigger.Instance.forItems(Items.PRISMARINE_SHARD));
    }
}
