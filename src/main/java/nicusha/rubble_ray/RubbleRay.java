package nicusha.rubble_ray;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(RubbleRay.MODID)
public class RubbleRay
{
    public static final String MODID = "rubble_ray";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MODID);
    public static final DeferredHolder<Item, Item> RUBBLE_RAY = ITEMS.register("rubble_ray", () -> new ItemRubbleRay());
    public static final DeferredHolder<SoundEvent, SoundEvent> RAY = SOUNDS.register("ray", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "ray")));

    public RubbleRay(IEventBus bus, ModContainer container)
    {
        bus.addListener(this::commonSetup);
        ITEMS.register(bus);
        SOUNDS.register(bus);
        bus.addListener(this::addCreative);
        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
            event.accept(RUBBLE_RAY.get());
    }

}
