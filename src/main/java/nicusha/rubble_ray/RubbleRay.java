package nicusha.rubble_ray;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(RubbleRay.MODID)
public class RubbleRay
{
    public static final String MODID = "rubble_ray";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);

    public static final RegistryObject<Item> RUBBLE_RAY = ITEMS.register("rubble_ray", () -> new ItemRubbleRay());
    public static final RegistryObject<SoundEvent> RAY = SOUNDS.register("ray", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "ray")));


    public RubbleRay()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(RubbleRay::registerTab);
        ITEMS.register(bus);
        SOUNDS.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, MODID + "-common.toml");
        MinecraftForge.EVENT_BUS.register(this);
    }


    public static final ResourceLocation TAB = new ResourceLocation(RubbleRay.MODID, "rubble_ray");

    private static ItemStack makeIcon() {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RubbleRay.MODID, "rubble_ray")));
    }


    public static void registerTab(CreativeModeTabEvent.Register event){
        event.registerCreativeModeTab(TAB, builder -> builder.title(Component.translatable("itemGroup.rubble_ray")).icon(RubbleRay::makeIcon).displayItems((flags, output, isOp) -> {
            for(RegistryObject<Item> item : RubbleRay.ITEMS.getEntries()){
                output.accept(item.get());
            }
        }));

    }
}
