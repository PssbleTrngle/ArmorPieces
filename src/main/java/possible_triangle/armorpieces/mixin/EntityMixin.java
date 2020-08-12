package possible_triangle.armorpieces.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import possible_triangle.armorpieces.item.armor.CrabLeggings;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/entity/Entity;canSwim()Z", cancellable = true)
    public void canSwim(CallbackInfoReturnable<Boolean> callback) {
        if(CrabLeggings.preventSwimming((Entity) (Object) this)) callback.setReturnValue(false);
    }

}
