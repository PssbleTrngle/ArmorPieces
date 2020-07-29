package possibletriangle.armorpieces.item.armor;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
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

public class MagmaLeggings extends BaseArmor {

    public MagmaLeggings() {
        super(new CustomArmorMaterial(
                        "magma",
                        EquipmentSlotType.LEGS,
                        15,
                        4,
                        12,
                        0.0F, () -> Ingredient.fromItems(Content.STRIDER_SKIN.orElse(Items.MAGMA_CREAM))),
                EquipmentSlotType.LEGS);
    }

    @Override
    public void onWornTick(LivingEntity entity, ItemStack stack) {
        if (!entity.isInLava() && !entity.isBurning()) {
            EffectInstance effect = new EffectInstance(Effects.FIRE_RESISTANCE, 20 * 6 + 2, 0, false, Config.SHOW_PARTICLES);
            entity.addPotionEffect(effect);
        }
    }

    @Override
    public void buildRecipe(ShapedRecipeBuilder builder) {
        IItemProvider skin = Content.STRIDER_SKIN.orElse(Items.LEATHER);
        builder
                .patternLine("#o#")
                .patternLine("# #")
                .patternLine("# #")
                .key('#', skin)
                .key('o', Items.MAGMA_CREAM)
                .addCriterion("ingredients", InventoryChangeTrigger.Instance.forItems(skin, Items.STRING));
    }
}
