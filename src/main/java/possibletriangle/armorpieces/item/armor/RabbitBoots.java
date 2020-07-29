package possibletriangle.armorpieces.item.armor;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.IItemProvider;
import possibletriangle.armorpieces.Config;
import possibletriangle.armorpieces.Content;

public class RabbitBoots extends BaseArmor {

    public RabbitBoots() {
        super(new CustomArmorMaterial(
                        "traveller",
                        EquipmentSlotType.FEET,
                        15,
                        4,
                        12,
                        0.0F, () -> Ingredient.fromItems(Items.RABBIT_HIDE)),
                EquipmentSlotType.FEET);
    }

    @Override
    public void onWornTick(LivingEntity entity, ItemStack stack) {
        EffectInstance effect = new EffectInstance(Effects.JUMP_BOOST, 20 + 2, 1, false, Config.SHOW_PARTICLES);
        entity.addPotionEffect(effect);
    }

    @Override
    public void buildRecipe(ShapedRecipeBuilder builder) {
        builder
                .patternLine("w w")
                .patternLine("h h")
                .key('h', Items.RABBIT_HIDE)
                .key('w', Items.BROWN_WOOL)
                .addCriterion("ingredients", InventoryChangeTrigger.Instance.forItems(Items.RABBIT, Items.STRING));
    }

}
