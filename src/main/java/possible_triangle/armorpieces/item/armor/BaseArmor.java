package possible_triangle.armorpieces.item.armor;

import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public abstract class BaseArmor extends ArmorItem {

    public static Properties baseProperties() {
        return new Properties().group(ItemGroup.COMBAT);
    }

    public BaseArmor(IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
        super(material, slot, properties);
    }

    public BaseArmor(IArmorMaterial material, EquipmentSlotType slot) {
        this(material, slot, baseProperties());
    }

    public boolean isCharging(LivingEntity entity) {
        return false;
    }

    public int getCharge(ItemStack stack) {
        return Math.min(getRequiredCharge(), getTimeCharged(stack));
    }

    public static int getTimeCharged(ItemStack stack) {
        return stack.getOrCreateTag().getInt("charge");
    }

    public boolean isCharged(ItemStack stack) {
        return getCharge(stack) >= getRequiredCharge();
    }

    public static void charge(ItemStack stack) {
        int current = stack.getOrCreateTag().getInt("charge");
        stack.getOrCreateTag().putInt("charge", current + 1);
    }

    public static void resetCharge(ItemStack stack) {
        stack.getOrCreateTag().putInt("charge", 0);
    }

    public static Stream<ItemStack> findPieces(LivingEntity entity) {
        Stream<EquipmentSlotType> slots = Stream.of(EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET);
        Stream<Optional<ItemStack>> stacks = slots.map(slot -> {
            ItemStack stack = entity.getItemStackFromSlot(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof BaseArmor) {
                return Optional.of(stack);
            } else return Optional.empty();
        });
        return stacks.filter(Optional::isPresent).map(Optional::get);
    }

    public static void callFor(LivingEntity entity, Function<BaseArmor, BiConsumer<LivingEntity, ItemStack>> func) {
        findPieces(entity).forEach(stack -> {
            BaseArmor armor = (BaseArmor) stack.getItem();
            func.apply(armor).accept(entity, stack);
        });
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.world.isRemote) return;

        callFor(event.player, i -> i::onWornTick);
        callFor(event.player, i -> (entity, stack) -> {

            if(i.isCharging(entity)) i.charge(stack);
            else resetCharge(stack);

            int cooldown = i.getCooldown();
            int currentCooldown = stack.getOrCreateTag().getInt("cooldown");
            if (cooldown < 0 || currentCooldown <= 0) {
                if (cooldown > 0) stack.getOrCreateTag().putInt("cooldown", cooldown);
                i.onRandomTick(entity, stack);
            } else if (cooldown > 0) {
                stack.getOrCreateTag().putInt("cooldown", currentCooldown - 1);
            }

        });
    }

    @SubscribeEvent
    public static void playerBreak(PlayerEvent.BreakSpeed event) {
        if (event.getPlayer().world.isRemote) return;

        findPieces(event.getPlayer()).forEach(stack -> {
            BaseArmor armor = (BaseArmor) stack.getItem();
            float factor = armor.breakSpeed(event.getPlayer(), stack);
            event.setNewSpeed(event.getNewSpeed() * factor);
        });
    }

    @SubscribeEvent
    public static void entityDamage(LivingDamageEvent event) {
        findPieces(event.getEntityLiving()).forEach(stack -> {
            BaseArmor armor = (BaseArmor) stack.getItem();
            boolean cancel = armor.onDamage(event.getEntityLiving(), stack, event.getSource());
            if(cancel) event.setCanceled(true);
        });
    }

    @SubscribeEvent
    public static void pearlTeleport(EnderTeleportEvent event) {
        findPieces(event.getEntityLiving()).forEach(stack -> {
            BaseArmor armor = (BaseArmor) stack.getItem();
            boolean cancel = armor.onTeleport(event.getEntityLiving(), stack);
            if(cancel) event.setAttackDamage(0F);
        });
    }

    @SubscribeEvent
    public static void playerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving().world.isRemote) return;
        callFor(event.getEntityLiving(), i -> i::onJump);
    }

    public void onWornTick(LivingEntity entity, ItemStack stack) { }

    public float breakSpeed(LivingEntity entity, ItemStack stack) {
        return 1F;
    }

    public void onRandomTick(LivingEntity entity, ItemStack stack) {}

    public void onJump(LivingEntity entity, ItemStack stack) {}

    public boolean onDamage(LivingEntity entity, ItemStack stack, DamageSource source) { return false; }

    public boolean onTeleport(LivingEntity entity, ItemStack stack) { return false; }

    public int getCooldown() {
        return -1;
    }

    public int getRequiredCharge() {
        return -1;
    }

    public void buildRecipe(ShapedRecipeBuilder builder) {}

}
