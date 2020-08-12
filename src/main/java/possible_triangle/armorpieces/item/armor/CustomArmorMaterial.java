package possible_triangle.armorpieces.item.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import possible_triangle.armorpieces.ArmorPieces;

import java.util.function.Supplier;

public class CustomArmorMaterial implements IArmorMaterial {

    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final LazyValue<Ingredient> repairMaterial;

    @Override
    public float func_230304_f_() {
        return 0F;
    }

    private static int[] damageReductionArray(EquipmentSlotType slot, int damageReduction) {
        int[] i = { 0, 0, 0, 0 };
        i[slot.getIndex()] = damageReduction;
        return i;
    }

    public CustomArmorMaterial(String name, EquipmentSlotType slot, int maxDamageFactor, int damageReduction, int enchantability, float toughness, Supplier<Ingredient> repairMaterial) {
        this(name, maxDamageFactor, damageReductionArray(slot, damageReduction), enchantability, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, toughness, repairMaterial);
    }

    public CustomArmorMaterial(String name, int maxDamageFactor, int[] damageReduction, int enchantability, SoundEvent soundEvent, float toughness, Supplier<Ingredient> repairMaterial) {
        this.name = ArmorPieces.MODID + ":" + name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReduction;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.repairMaterial = new LazyValue<>(repairMaterial);
    }


    public int getDurability(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
    }

    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return this.damageReductionAmountArray[slotIn.getIndex()];
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }

    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }
}
