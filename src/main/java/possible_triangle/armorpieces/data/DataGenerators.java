package possible_triangle.armorpieces.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.loot.LootConditionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import possible_triangle.armorpieces.ArmorPieces;
import possible_triangle.armorpieces.data.predicates.InFluid;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    public static final LootConditionType IN_FLUID = Registry.register(Registry.field_239704_ba_, new ResourceLocation(ArmorPieces.MODID, "in_fluid"), new LootConditionType(new InFluid.Serializer()));

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        generator.addProvider(new ItemModels(generator, fileHelper));
        generator.addProvider(new Recipes(generator));
        generator.addProvider(new BlockDrops(generator));

    }

}