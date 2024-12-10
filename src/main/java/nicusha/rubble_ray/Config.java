package nicusha.rubble_ray;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = RubbleRay.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue GENERATE_ROOF = BUILDER.comment("Fill in air blocks on the roof of the tunnel").define("generateRoof", true), GENERATE_FLOOR = BUILDER.comment("Fill in air blocks on the floor of the tunnel").define("generateFloor", true), GENERATE_WALLS = BUILDER.comment("Fill in air blocks on the walls of the tunnel").define("generateWalls", true);
    private static final ModConfigSpec.IntValue HEIGHT = BUILDER.comment("Tunnel Height").defineInRange("height", 5, 1, 500), WIDTH = BUILDER.comment("Tunnel Width").defineInRange("width", 5, 1, 500), LENGTH = BUILDER.comment("Tunnel Length").defineInRange("length", 64, 1, 500), LIGHT_GAP = BUILDER.comment("Gap size between lights").defineInRange("lightGap", 5, 1, 500);
    public static final ModConfigSpec.ConfigValue<String> AIR_REPLACEMENT = BUILDER.comment("Block to replace air/water/lava/falling blocks on the roof and floor").define("airReplacement", "minecraft:stone");

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean generateRoof, generateFloor, generateWalls;
    public static int height, width, length, lightGap;
    public static String airReplacement;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        generateRoof = GENERATE_ROOF.get();
        generateFloor = GENERATE_FLOOR.get();
        generateWalls = GENERATE_WALLS.get();
        height = HEIGHT.get();
        width = WIDTH.get();
        length = LENGTH.get();
        lightGap = LIGHT_GAP.get();
        airReplacement = AIR_REPLACEMENT.get();


    }
}
