package nicusha.rubble_ray;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.ConfigValue<Integer> height, width, length, lightGap;
    public static ForgeConfigSpec.ConfigValue<String> airReplacement;
    public static ForgeConfigSpec.ConfigValue<Boolean> generateRoof, generateFloor;

    static {
        BUILDER.push("Common config for Tunneler");
        height = BUILDER.comment("Tunnel Height").defineInRange("height", 5, 1, 500);
        width = BUILDER.comment("Tunnel Radius").defineInRange("width", 5, 1, 500);
        length = BUILDER.comment("Tunnel Length").defineInRange("length", 64, 1, 500);
        lightGap = BUILDER.comment("Gap size between lights").defineInRange("lightGap", 5, 1, 500);
        airReplacement = BUILDER.comment("Block to replace air/water/lava/falling blocks on the roof and floor").define("airReplacement", "minecraft:stone");
        generateRoof = BUILDER.comment("Fill in air blocks on the roof of the tunnel").define("generateRoof", true);
        generateFloor = BUILDER.comment("Fill in air blocks on the floor of the tunnel").define("generateFloor", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
