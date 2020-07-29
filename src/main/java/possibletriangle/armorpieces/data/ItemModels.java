package possibletriangle.armorpieces.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import possibletriangle.armorpieces.ArmorPieces;
import possibletriangle.armorpieces.Content;

public class ItemModels extends ItemModelProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private final ExistingFileHelper fileHelper;

    public ItemModels(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, ArmorPieces.MODID, fileHelper);
        this.fileHelper = fileHelper;
    }

    private boolean textureExists(ResourceLocation texture) {
        return this.fileHelper.exists(texture, ResourcePackType.CLIENT_RESOURCES, ".png", "textures");
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    private ResourceLocation extend(String prefix, ResourceLocation rl) {
        return new ResourceLocation(rl.getNamespace(), prefix + rl.getPath());
    }

    private ModelFile simpleItem(Item item) {
        ResourceLocation r = item.getRegistryName();
        assert r != null;
        return simpleItem(r.getPath(), extend("item/", r));
    }

    private ModelFile simpleItem(String path, ResourceLocation texture) {
        if(textureExists(texture)) {
            return withExistingParent("item/" + path, mcLoc("item/handheld"))
                    .texture("layer0", texture);
        } else {
            LOGGER.warn("Texture for '{}' does not exist", path);
            return null;
        }
    }

    @Override
    protected void registerModels() {

        Content.ITEMS.getEntries().stream()
                .filter(RegistryObject::isPresent)
                .map(RegistryObject::get)
                .forEach(this::simpleItem);

    }

    @Override
    public String getName() {
        return "Item Models";
    }
}
