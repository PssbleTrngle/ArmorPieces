package possible_triangle.armorpieces.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.fml.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import possible_triangle.armorpieces.Content;
import possible_triangle.armorpieces.item.armor.BaseArmor;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    public Recipes(DataGenerator generator) {
        super(generator);
    }

    private ShapedRecipeBuilder createRecipe(BaseArmor armor) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shapedRecipe(armor);
        armor.buildRecipe(builder);
        return builder;
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

        Content.ITEMS.getEntries().stream()
                .filter(RegistryObject::isPresent)
                .map(RegistryObject::get)
                .filter(BaseArmor.class::isInstance)
                .map(i -> (BaseArmor) i)
                .forEach(i -> {
                    try {
                        this.createRecipe(i).build(consumer);
                    } catch(IllegalStateException ignored) {
                        LOGGER.info("No recipe defined for '{}'", i.getRegistryName().getPath());
                    }
                });
    }

}
