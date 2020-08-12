package possible_triangle.armorpieces.data.predicates;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import possible_triangle.armorpieces.data.DataGenerators;

import java.util.Set;

public class InFluid implements ILootCondition {

    @Override
    public LootConditionType func_230419_b_() {
        return DataGenerators.IN_FLUID;
    }

    private final ITag.INamedTag<Fluid> tag;

    public InFluid(ITag.INamedTag<Fluid> fluid) {
        this.tag = fluid;
    }

    public Set<LootParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootParameters.THIS_ENTITY);
    }

    public boolean test(LootContext context) {
        Entity entity = context.get(LootParameters.THIS_ENTITY);
        return entity != null && entity.areEyesInFluid(this.tag);
    }

    public static ILootCondition.IBuilder builder(ITag.INamedTag<Fluid> fluid) {
        return () -> new InFluid(fluid);
    }

    public static class Serializer implements ILootSerializer<InFluid> {

        public void func_230424_a_(JsonObject json, InFluid value, JsonSerializationContext context) {
            json.addProperty("fluid", value.tag.func_230234_a_().toString());
        }

        public InFluid func_230423_a_(JsonObject json, JsonDeserializationContext context) {
            ResourceLocation name = new ResourceLocation(JSONUtils.getString(json, "fluid"));
            ITag<Fluid> tag = FluidTags.getCollection().getOrCreate(name);
            return tag instanceof ITag.INamedTag
                    ? new InFluid((ITag.INamedTag<Fluid>) tag)
                    : null;
        }
    }

}
