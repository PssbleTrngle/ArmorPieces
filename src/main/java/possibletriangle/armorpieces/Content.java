package possibletriangle.armorpieces;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CoralBlock;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.fish.PufferfishEntity;
import net.minecraft.entity.passive.fish.TropicalFishEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import possibletriangle.armorpieces.entity.PufferfishSummoned;
import possibletriangle.armorpieces.entity.TropicalFishSummoned;
import possibletriangle.armorpieces.item.armor.*;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Content {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArmorPieces.MODID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ArmorPieces.MODID);

    static void init() {

        ITEMS.register("rabbit_boots", RabbitBoots::new);
        ITEMS.register("prismarine_boots", PrismarineBoots::new);
        ITEMS.register("leap_leggings", LeapLeggings::new);
        ITEMS.register("magma_leggings", MagmaLeggings::new);
        ITEMS.register("warp_boots", WarpBoots::new);
        ITEMS.register("spore_chestplate", SporeChestplate::new);
        ITEMS.register("piglin_crown", PiglinCrown::new);

        IForgeRegistry<Block> BLOCKS = GameRegistry.findRegistry(Block.class);

        Stream.of(

                Stream.of(
                        Blocks.TUBE_CORAL_BLOCK,
                        Blocks.FIRE_CORAL_BLOCK,
                        Blocks.HORN_CORAL_BLOCK,
                        Blocks.BRAIN_CORAL_BLOCK,
                        Blocks.BUBBLE_CORAL_BLOCK
                ),

                Stream.of(
                      "coral_block"
                ).map(path -> new ResourceLocation("upgradeaquatic", path))
                .filter(BLOCKS::containsKey)
                .map(BLOCKS::getValue)

        ).flatMap(Function.identity()).forEach(coral -> {
                String type = coral.getRegistryName().getPath().replace("_coral_block", "");
                ITEMS.register("coral_chestplate_" + type, () -> new CoralChestplate(coral));
        });

        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private static Item ingredient(Consumer<Item.Properties> consumer) {
        Item.Properties properties = new Item.Properties().group(ItemGroup.MISC);
        consumer.accept(properties);
        return new Item(properties);
    }

    private static Item ingredient() {
        return ingredient($ -> { });
    }

    public static final RegistryObject<Item> MYCELIUM_SPORE = ITEMS.register("mycelium_spore", () -> ingredient(p -> p.rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> STRIDER_SKIN = ITEMS.register("strider_skin", Content::ingredient);
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", Content::ingredient);
    public static final RegistryObject<Item> ROYAL_GOLD_NUGGET = ITEMS.register("royal_gold_nugget", Content::ingredient);
    public static final RegistryObject<Item> ROYAL_GOLD_INGOT = ITEMS.register("royal_gold_ingot", Content::ingredient);
    public static final RegistryObject<Item> ENDERMITE_SCALE = ITEMS.register("endermite_scale", Content::ingredient);

    public static final RegistryObject<EntityType<TropicalFishEntity>> TROPICAL_FISH = ENTITIES.register("tropical_fish", () ->
            EntityType.Builder.<TropicalFishEntity>create(TropicalFishSummoned::new, EntityClassification.WATER_CREATURE)
                    .size(1, 1)
                    .build("tropical_fish")
    );

    public static final RegistryObject<EntityType<PufferfishEntity>> PUFFERFISH = ENTITIES.register("pufferfish", () ->
            EntityType.Builder.<PufferfishEntity>create(PufferfishSummoned::new, EntityClassification.WATER_CREATURE)
                    .size(1, 1)
                    .build("pufferfish")
    );

}
