package nicusha.rubble_ray;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.SubscribeEvent;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue GENERATE_ROOF = BUILDER.comment("Fill in air blocks on the roof of the tunnel").define("generateRoof", true);
    public static final ModConfigSpec.BooleanValue GENERATE_FLOOR = BUILDER.comment("Fill in air blocks on the floor of the tunnel").define("generateFloor", true);
    public static final ModConfigSpec.BooleanValue GENERATE_WALLS = BUILDER.comment("Fill in air blocks on the walls of the tunnel").define("generateWalls", true);
    public static final ModConfigSpec.IntValue HEIGHT = BUILDER.comment("Tunnel height").defineInRange("height", 5, 1, 500);
    public static final ModConfigSpec.IntValue WIDTH = BUILDER.comment("Tunnel half-width (e.g., 5 = 11 blocks wide)").defineInRange("width", 5, 1, 500);
    public static final ModConfigSpec.IntValue LENGTH = BUILDER.comment("Tunnel length").defineInRange("length", 64, 1, 500);
    public static final ModConfigSpec.IntValue LIGHT_GAP = BUILDER.comment("Gap between redstone lamp lights (in blocks)").defineInRange("lightGap", 5, 1, 500);
    public static final ModConfigSpec.ConfigValue<String> AIR_REPLACEMENT = BUILDER.comment("Block ID to use for filling air/lava/water on the floor/roof (e.g., minecraft:stone)").define("airReplacement", "minecraft:stone");

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean generateRoof, generateFloor, generateWalls;
    public static int height, width, length, lightGap;
    public static String airReplacement;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        generateRoof = GENERATE_ROOF.get();
        generateFloor = GENERATE_FLOOR.get();
        generateWalls = GENERATE_WALLS.get();
        height = HEIGHT.get();
        width = WIDTH.get();
        length = LENGTH.get();
        lightGap = LIGHT_GAP.get();
        airReplacement = AIR_REPLACEMENT.get();

        System.out.println("[RubbleRay] Config loaded: height=" + height + ", width=" + width + ", length=" + length);
    }

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(Identifier.parse(itemName));
    }
}
