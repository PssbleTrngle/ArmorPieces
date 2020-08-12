package possible_triangle.armorpieces.item.armor;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import possible_triangle.armorpieces.Content;

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
