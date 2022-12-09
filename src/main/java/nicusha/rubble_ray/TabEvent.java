package nicusha.rubble_ray;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = RubbleRay.MODID)
public class TabEvent {
    @SubscribeEvent
    public static void registerCreativeTab(CreativeModeTabEvent.BuildContents event) {
        event.registerSimple(CreativeModeTabs.TOOLS_AND_UTILITIES, RubbleRay.RUBBLE_RAY.get());
    }
}