package possible_triangle.armorpieces;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArmorPieces.MODID)
public class ArmorPieces {
    public static final String MODID = "armor_pieces";

    private static final Logger LOGGER = LogManager.getLogger();

    public ArmorPieces() {
        Content.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        Content.setup();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        Minecraft mc = event.getMinecraftSupplier().get();
        Content.setupClient(mc);
    }
}
