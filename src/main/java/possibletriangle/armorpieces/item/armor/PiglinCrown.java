package possibletriangle.armorpieces.item.armor;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import possibletriangle.armorpieces.Content;

public class PiglinCrown extends BaseArmor {

    public PiglinCrown() {
        super(ArmorMaterial.GOLD, EquipmentSlotType.HEAD);
    }

    @Override
    public void buildRecipe(ShapedRecipeBuilder builder) {
        builder
                .patternLine("grg")
                .patternLine("g g")
                .key('g', Content.ROYAL_GOLD_INGOT.orElse(Items.GOLD_INGOT))
                .key('r', Content.RUBY.orElse(Items.field_234759_km_))
                .addCriterion("ingredients", InventoryChangeTrigger.Instance.forItems(Items.PRISMARINE_SHARD));
    }
}
