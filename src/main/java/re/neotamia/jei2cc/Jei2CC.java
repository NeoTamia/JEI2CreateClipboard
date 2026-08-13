package re.neotamia.jei2cc;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = Jei2CC.ID, dist = { Dist.CLIENT })
public class Jei2CC {
    public static final String ID = "jei2createclipboard";
    private static final Logger LOGGER = LogManager.getLogger(ID);

    public Jei2CC(IEventBus modBus) {
        LOGGER.log(Level.INFO, "Hello world!");

        modBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.log(Level.INFO, "Initializing client...");
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.log(Level.INFO, "Hello! This is working!");
    }
}
