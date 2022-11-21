package nicusha.tunneler;

import net.minecraft.core.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.*;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemTunneler extends Item {
    TagKey<Block> tunnelable;
    public ItemTunneler() {
        super(new Properties().tab(CreativeModeTab.TAB_MISC));
        tunnelable = BlockTags.create(new ResourceLocation(TunnelerMod.MODID, "tunnelable"));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        int cposx = context.getClickedPos().getX(), cposz = context.getClickedPos().getZ();
        int deltax = 0;
        int deltaz = 0;
        int dirx = 0;
        int dirz = 0;
        int height = Config.height.get();
        int width = Config.width.get();
        int length = Config.length.get();
        int torches = Config.lightGap.get();
        String replacement = Config.airReplacement.get();
        int solid_count = 0;
        if (cposx < 0) {
            dirx = -1;
        }

        if (cposz < 0) {
            dirz = -1;
        }

        int pposx = (int)(player.blockPosition().getX() + 0.99 * (double)dirx);
        int pposy = player.blockPosition().getY();
        int pposz = (int)(player.blockPosition().getZ() + 0.99 * (double)dirz);
        if (cposx - pposx != 0 && cposz - pposz != 0) {
            return InteractionResult.FAIL;
        } else {
            int x = cposx;
            int y = pposy;
            int z = cposz;
            if (cposx - pposx < 0) {
                deltax = -1;
            }

            if (cposx - pposx > 0) {
                deltax = 1;
            }

            if (cposz - pposz < 0) {
                deltaz = -1;
            }

            if (cposz - pposz > 0) {
                deltaz = 1;
            }

            if (deltax == 0 && deltaz == 0) {
                return InteractionResult.FAIL;
            } else if (deltax != 0 && deltaz != 0) {
                return InteractionResult.FAIL;
            } else {
                player.playSound(SoundEvents.GENERIC_EXPLODE);
                if (level.isClientSide) {
                    return InteractionResult.SUCCESS;
                } else {
                    int k;
                    BlockState bid;
                    for(int i = 0; i < height; ++i) {
                        for(k = 0; k < length; ++k) {
                            solid_count = 0;

                            int j;
                            for(j = -width; j <= width; ++j) {
                                bid = level.getBlockState(new BlockPos(x + k * deltax + j * deltaz, y + i, z + k * deltaz + j * deltax));
                                if (bid.is(tunnelable)) {
                                    level.setBlock(new BlockPos(x + k * deltax + j * deltaz, y + i, z + k * deltaz + j * deltax), Blocks.AIR.defaultBlockState(), 2);
                                }

                                if (i == height - 1) {
                                    bid = level.getBlockState(new BlockPos(x + k * deltax + j * deltaz, y + i + 1, z + k * deltaz + j * deltax));
                                    if (bid != Blocks.AIR.defaultBlockState()) {
                                        ++solid_count;
                                    }

                                    if (bid.is(Blocks.WATER) || bid.is(Blocks.LAVA)) {
                                            level.setBlock(new BlockPos(x + k * deltax + j * deltaz, y + i + 1, z + k * deltaz + j * deltax), Blocks.AIR.defaultBlockState(), 2);
                                    }
                                }

                                //FLOOR
                                if(Config.generateFloor.get()) {
                                    bid = level.getBlockState(new BlockPos(x + k * deltax + j * deltaz, y - 1, z + k * deltaz + j * deltax));
                                    if ((bid.is(Blocks.AIR) || bid.is(Blocks.WATER) || bid.is(Blocks.LAVA))) {
                                        level.setBlock(new BlockPos(x + k * deltax + j * deltaz, y - 1, z + k * deltaz + j * deltax), ForgeRegistries.BLOCKS.getValue(new ResourceLocation(replacement)).defaultBlockState(), 2);
                                    }
                                }
                                //ROOF
                                if(Config.generateRoof.get()) {
                                    bid = level.getBlockState(new BlockPos(x + k * deltax + j * deltaz, y + height, z + k * deltaz + j * deltax));
                                    if ((bid.is(Blocks.AIR) || bid.is(Blocks.WATER) || bid.is(Blocks.LAVA) || bid.getBlock() instanceof FallingBlock)) {
                                        level.setBlock(new BlockPos(x + k * deltax + j * deltaz, y + height, z + k * deltaz + j * deltax), ForgeRegistries.BLOCKS.getValue(new ResourceLocation(replacement)).defaultBlockState(), 2);
                                    }
                                }
                            }

                            if (i == height - 1 && solid_count == 0) {
                                for(j = -width; j <= width; ++j) {
                                    level.setBlock(new BlockPos(x + k * deltax + j * deltaz, y + i + 1, z + k * deltaz + j * deltax), Blocks.AIR.defaultBlockState(), 2);
                                }
                            }
                        }
                    }

                    for(k = 0; k < length; k += torches) {
                        bid = level.getBlockState(new BlockPos(x + k * deltax, y - 1, z + k * deltaz));
                        if ((bid.is(tunnelable))) {
                            level.setBlock(new BlockPos(x + k * deltax, y-1, z + k * deltaz), Blocks.REDSTONE_LAMP.defaultBlockState().setValue(RedstoneLampBlock.LIT, true), 2);
                            level.setBlock(new BlockPos(x + k * deltax, y-2, z + k * deltaz), Blocks.REDSTONE_BLOCK.defaultBlockState(), 2);
                        }
                    }

                    if (!player.isCreative()) {
                        context.getItemInHand().shrink(1);
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        }
    }
}
