package possible_triangle.armorpieces.item.armor;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import possible_triangle.armorpieces.Content;

public class CrabLeggings extends BaseArmor {

    public static boolean preventSwimming(Entity entity) {
        return entity instanceof PlayerEntity;
    }

    public CrabLeggings() {
        super(new CustomArmorMaterial(
                        "crab",
                        EquipmentSlotType.LEGS,
                        15,
                        4,
                        12,
                        0.0F, () -> Ingredient.fromItems(Content.STRIDER_SKIN.orElse(Items.MAGMA_CREAM))),
                EquipmentSlotType.LEGS);

        //this.getAttributeModifiers(this.getEquipmentSlot()).put(Attributes.field_233828_k_, new AttributeModifier(uuid, "Gravity modifier", (double)this.damageReduceAmount, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public void onWornTick(LivingEntity entity, ItemStack stack) {
        if(entity.isSwimming()) entity.setPose(Pose.STANDING);
    }

    @Override
    public void buildRecipe(ShapedRecipeBuilder builder) {
        IItemProvider crab_shell = Items.NAUTILUS_SHELL;
        builder
                .patternLine("non")
                .patternLine("# #")
                .patternLine("# #")
                .key('#', crab_shell)
                .key('o', Items.KELP)
                .key('n', Items.NAUTILUS_SHELL)
                .addCriterion("ingredients", InventoryChangeTrigger.Instance.forItems(crab_shell, Items.NAUTILUS_SHELL));
    }
}
