package possibletriangle.armorpieces;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PufferfishRenderer;
import net.minecraft.client.renderer.entity.TropicalFishRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.fish.TropicalFishEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import possibletriangle.armorpieces.gui.ChargeOverlay;

import java.util.stream.Collectors;

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
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        Minecraft mc = event.getMinecraftSupplier().get();
        EntityRendererManager manager = mc.getRenderManager();

        Content.TROPICAL_FISH.ifPresent(e -> manager.register(e, new TropicalFishRenderer(manager)));
        Content.PUFFERFISH.ifPresent(e -> manager.register(e, new PufferfishRenderer(manager)));

        MinecraftForge.EVENT_BUS.register(new ChargeOverlay(mc));

    }
}
