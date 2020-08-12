package possible_triangle.armorpieces.item.armor;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import possible_triangle.armorpieces.Content;

public class PiglinCrown extends BaseArmor {

    public PiglinCrown() {
        super(ArmorMaterial.GOLD, EquipmentSlotType.HEAD);
    }

    @Override
    public void buildRecipe(ShapedRecipeBuilder builder) {
        builder
                .patternLine("ggg")
                .patternLine("g g")
                .key('g', Content.ROYAL_GOLD.orElse(Items.GOLD_INGOT))
                .addCriterion("ingredients", InventoryChangeTrigger.Instance.forItems(Items.PRISMARINE_SHARD));
    }
}
