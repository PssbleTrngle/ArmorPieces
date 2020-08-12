package possible_triangle.armorpieces.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.settings.AttackIndicatorStatus;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import possible_triangle.armorpieces.ArmorPieces;
import possible_triangle.armorpieces.item.armor.BaseArmor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@OnlyIn(Dist.CLIENT)
public class ChargeOverlay extends AbstractGui {

    public static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation(ArmorPieces.MODID, "textures/gui/icons.png");

    private final Minecraft mc;

    public ChargeOverlay(Minecraft mc) {
        this.mc = mc;
    }

    @SubscribeEvent
    public void renderEvent(RenderGameOverlayEvent event) {
        if (mc.playerController != null && mc.player != null) render(event.getMatrixStack());
    }

    private void render(MatrixStack matrizes) {
        if (mc.playerController.getCurrentGameType() != GameType.SPECTATOR && !mc.gameSettings.hideGUI) {
            renderHotbar(matrizes);
        }
    }

    private void renderHotbar(MatrixStack matrizes) {
        matrizes.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        List<ItemStack> charging = BaseArmor.findPieces(mc.player)
                .filter(stack -> BaseArmor.getTimeCharged(stack) > 0)
                .collect(Collectors.toList());

        IntStream.range(0, charging.size())
                .forEach(i -> {
                    ItemStack stack = charging.get(i);
                    BaseArmor armor = (BaseArmor) stack.getItem();

                    int charge = armor.getCharge(stack);
                    int required = armor.getRequiredCharge();
                    float c = (float) charge / required;
                    renderCharge(matrizes, c, armor.getEquipmentSlot(), i);
                });

        RenderSystem.disableBlend();
        matrizes.pop();
    }

    private void renderCharge(MatrixStack matrizes, float c, EquipmentSlotType slot, int offset) {
        int width = mc.getMainWindow().getScaledWidth();
        int height = mc.getMainWindow().getScaledHeight();
        if(mc.player == null) return;
        HandSide handSide = mc.player.getPrimaryHand();

        if (this.mc.gameSettings.attackIndicator == AttackIndicatorStatus.HOTBAR) {
            float f = this.mc.player.getCooledAttackStrength(0.0F);
            if (f < 1.0F) offset++;
        }

        int center = width / 2;
        int top = height - 20;
        int l = offset * 19;
        int left = center + (handSide == HandSide.LEFT ? (-(91 + l) - 22) : ((91 + l) + 6));
        int e = (3 - slot.getIndex()) * 18;

        mc.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
        int ch = (int) (c * 19.0F);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.func_238474_b_(matrizes, left, top, 0, e, 18, 18);
        this.func_238474_b_(matrizes, left, top + 18 - ch, 18, e + 18 - ch, 18, ch);
        mc.getTextureManager().bindTexture(AbstractGui.field_230663_f_);
    }

}
