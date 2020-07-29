package possibletriangle.armorpieces.data;

import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import possibletriangle.armorpieces.ArmorPieces;
import possibletriangle.armorpieces.Content;
import possibletriangle.armorpieces.data.predicates.InFluid;

import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockDrops extends LootTables {

    public BlockDrops(DataGenerator generator) {
        super(generator, ArmorPieces.MODID);
    }

    @Override
    protected void addTables() {
        Content.MYCELIUM_SPORE.ifPresent(spore ->
                addTable(extend("block/", spore.getRegistryName()), LootTable.builder().addLootPool(
                        pool("spore")
                                .name("spore")
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(spore)
                                        .acceptCondition(TableBonus.builder(Enchantments.FORTUNE,
                                                0, 0, 0.01F, 0.02F, 0.03F))
                                )
                        ),
                        LootParameterSets.BLOCK)
        );

        Content.ROYAL_GOLD_NUGGET.ifPresent(gold ->
                addTable(extend("block/", gold.getRegistryName()), LootTable.builder().addLootPool(
                        pool("royal_gold")
                                .name("royal_gold")
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(gold)
                                        .acceptCondition(TableBonus.builder(Enchantments.FORTUNE,
                                                0, 0, 0.01F, 0.02F, 0.03F))
                                )
                        ),
                        LootParameterSets.BLOCK)
        );

        Content.STRIDER_SKIN.ifPresent(skin ->
                addTable(extend("entity/", skin.getRegistryName()), LootTable.builder().addLootPool(
                        pool("skin")
                                .name("skin")
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(skin)
                                        .acceptCondition(InFluid.builder(FluidTags.LAVA))
                                        .acceptCondition(RandomChanceWithLooting.builder(0.02F, 1.1F))
                                )
                        ),
                        LootParameterSets.ENTITY)
        );

        Content.ENDERMITE_SCALE.ifPresent(scale ->
                addTable(extend("entity/", scale.getRegistryName()), LootTable.builder().addLootPool(
                        pool("scale")
                                .name("scale")
                                .rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(scale)
                                        .acceptCondition(RandomChanceWithLooting.builder(0.1F, 1.1F))
                                )
                        ),
                        LootParameterSets.ENTITY)
        );
    }

    @SubscribeEvent
    public static void lootLoaded(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();

        new HashMap<ResourceLocation,ResourceLocation>() {{

            Content.MYCELIUM_SPORE.map(ForgeRegistryEntry::getRegistryName).ifPresent(r  -> put(Blocks.MYCELIUM.getLootTable(), extend("block/", r)));
            Content.ROYAL_GOLD_NUGGET.map(ForgeRegistryEntry::getRegistryName).ifPresent(r  -> put(Blocks.field_235387_nA_.getLootTable(), extend("block/", r)));
            Content.STRIDER_SKIN.map(ForgeRegistryEntry::getRegistryName).ifPresent(r  -> put(EntityType.field_233589_aE_.getLootTable(), extend("entity/", r)));
            Content.ENDERMITE_SCALE.map(ForgeRegistryEntry::getRegistryName).ifPresent(r  -> put(EntityType.ENDERMITE.getLootTable(), extend("entity/", r)));

        }}.forEach((table, inject) -> {
            if(name.equals(table)) {
                LootEntry.Builder<?> entry = TableLootEntry.builder(inject);
                event.getTable().addPool(LootPool.builder().addEntry(entry).build());
            }
        });
    }

}