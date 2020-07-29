package possibletriangle.armorpieces.item.armor;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.omg.DynamicAny.DynEnumHelper;
import possibletriangle.armorpieces.entity.ISummonedFish;
import possibletriangle.armorpieces.entity.PufferfishSummoned;
import possibletriangle.armorpieces.entity.TropicalFishSummoned;

import java.util.Optional;

public class CoralChestplate extends BaseArmor {

    public static final int SPAWN_RANGE = 3;
    public static final int MAX_FISHES = 5;

    private final IItemProvider coral;

    public CoralChestplate(IItemProvider coral) {
        super(new CustomArmorMaterial(
                        coral.asItem().getRegistryName().getPath().replace("_block", ""),
                        EquipmentSlotType.CHEST,
                        15,
                        4,
                        12,
                        0.0F, () -> Ingredient.fromItems(coral)),
                EquipmentSlotType.CHEST
        );
        this.coral = coral;
    }

    public boolean possibleSpawn(AbstractFishEntity entity) {
        return entity instanceof ISummonedFish;
    }

    public Optional<Vector3d> findFishSpawn(LivingEntity entity) {
        Vector3d pos = entity.getPositionVec();

        int r = SPAWN_RANGE;
        for (int i = 0; i < 15; i++) {
            Vector3d random = pos.add((Math.random() * 2 - 1) * r, (Math.random() * 2 - 1) * r, (Math.random() * 2 - 1) * r);
            BlockState block = entity.world.getBlockState(new BlockPos(random));
            if (block.getFluidState().getFluid().isIn(FluidTags.WATER)) return Optional.of(random);
        }
        return Optional.empty();
    }

    @Override
    public void onRandomTick(LivingEntity entity, ItemStack stack) {
        if (entity.isInWater()) {

            Vector3d pos = entity.getPositionVec();
            AxisAlignedBB box = new AxisAlignedBB(pos, pos).grow(SPAWN_RANGE + TropicalFishSummoned.MAX_DISTANCE);
            int fishes = entity.world.getEntitiesWithinAABB(AbstractFishEntity.class, box, this::possibleSpawn).size();

            if (fishes < MAX_FISHES) {

                findFishSpawn(entity).ifPresent(p -> {
                    boolean summonPuffer = Math.random() < 0.3F;
                    AbstractFishEntity fish = summonPuffer ? new PufferfishSummoned(entity) : new TropicalFishSummoned(entity);
                    fish.setPosition(p.x, p.y, p.z);
                    entity.world.addEntity(fish);
                });

            }
        }
    }

    @Override
    public int getCooldown() {
        return 20 * 3;
    }

    @Override
    public void buildRecipe(ShapedRecipeBuilder builder) {
        builder
                .patternLine("c c")
                .patternLine("coc")
                .patternLine("ccc")
                .key('c', coral)
                .key('o', Items.HEART_OF_THE_SEA)
                .setGroup("coral_chestplate")
                .addCriterion("ingredients", InventoryChangeTrigger.Instance.forItems(Items.HEART_OF_THE_SEA, coral));
    }
}
