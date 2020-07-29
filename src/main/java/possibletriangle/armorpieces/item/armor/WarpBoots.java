package possibletriangle.armorpieces.item.armor;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import possibletriangle.armorpieces.Content;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WarpBoots extends BaseArmor {

    public WarpBoots() {
        super(new CustomArmorMaterial(
                    "warp_boots",
                    EquipmentSlotType.FEET,
                    15,
                    4,
                    12,
                    0.0F, () -> Ingredient.fromItems(Content.ENDERMITE_SCALE.orElse(Items.OBSIDIAN))),
            EquipmentSlotType.FEET
        );
    }

    @Override
    public boolean onTeleport(LivingEntity entity, ItemStack stack) {
        stack.damageItem(4, entity, $ -> {});
        return true;
    }

    @Override
    public void buildRecipe(ShapedRecipeBuilder builder) {
        IItemProvider scale = Content.ENDERMITE_SCALE.orElse(Items.POPPED_CHORUS_FRUIT);
        builder
                .patternLine("s s")
                .patternLine("# #")
                .key('s', scale)
                .key('#', Blocks.OBSIDIAN)
                .addCriterion("ingredients", InventoryChangeTrigger.Instance.forItems(scale, Blocks.OBSIDIAN));
    }

}
