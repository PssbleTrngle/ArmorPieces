package possible_triangle.armorpieces;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PufferfishRenderer;
import net.minecraft.client.renderer.entity.TropicalFishRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.fish.PufferfishEntity;
import net.minecraft.entity.passive.fish.TropicalFishEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import possible_triangle.armorpieces.entity.PufferfishSummoned;
import possible_triangle.armorpieces.entity.TropicalFishSummoned;
import possible_triangle.armorpieces.gui.ChargeOverlay;
import possible_triangle.armorpieces.item.armor.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class Content {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArmorPieces.MODID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ArmorPieces.MODID);

    static void init() {

        ITEMS.register("rabbit_boots", RabbitBoots::new);
        ITEMS.register("prismarine_boots", PrismarineBoots::new);
        ITEMS.register("leap_leggings", LeapLeggings::new);
        ITEMS.register("magma_leggings", MagmaLeggings::new);
        ITEMS.register("crab_leggings", CrabLeggings::new);
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

    public static void setup() {
        Stream.of(TROPICAL_FISH, PUFFERFISH)
                .filter(RegistryObject::isPresent)
                .map(RegistryObject::get)
                .forEach(type -> GlobalEntityTypeAttributes.put(type, AbstractFishEntity.func_234176_m_().func_233813_a_()));
    }

    public static void setupClient(Minecraft mc) {
        EntityRendererManager manager = mc.getRenderManager();

        Content.TROPICAL_FISH.ifPresent(e -> manager.register(e, new TropicalFishRenderer(manager)));
        Content.PUFFERFISH.ifPresent(e -> manager.register(e, new PufferfishRenderer(manager)));

        MinecraftForge.EVENT_BUS.register(new ChargeOverlay(mc));
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
    public static final RegistryObject<Item> ROYAL_GOLD = ITEMS.register("royal_gold", () -> ingredient(p -> p.rarity(Rarity.UNCOMMON)));
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
