package possibletriangle.armorpieces.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.SetContents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class LootTables extends LootTableProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<ResourceLocation, Pair<LootTable.Builder, LootParameterSet>> lootTables = new HashMap<>();

    private final DataGenerator generator;
    private final String modid;

    public static ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    public static ResourceLocation extend(String prefix, ResourceLocation rl) {
        return new ResourceLocation(rl.getNamespace(), prefix + rl.getPath());
    }

    public LootTables(DataGenerator generator, String modid) {
        super(generator);
        this.modid = modid;
        this.generator = generator;
    }

    protected abstract void addTables();

    protected ResourceLocation mcLoc(String path) {
        return new ResourceLocation(path);
    }

    protected ResourceLocation modLoc(String path) {
        return new ResourceLocation(this.modid, path);
    }

    protected LootPool.Builder pool(String name) {
        return LootPool.builder().name(name);
    }

    protected void addTable(ResourceLocation name, LootTable.Builder table, LootParameterSet set) {
        this.lootTables.put(name, new Pair<>(table, set));
    }

    @Override
    public void act(DirectoryCache cache) {
        addTables();

        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        lootTables.forEach((key, value) -> tables.put(key, value.getFirst().setParameterSet(value.getSecond()).build()));
        writeTables(cache, tables);
    }

    private void writeTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "Loot Tables";
    }
}