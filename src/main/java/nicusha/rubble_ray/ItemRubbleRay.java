package nicusha.rubble_ray;

import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.*;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

public class ItemRubbleRay extends Item {
    TagKey<Block> rubble;
    public ItemRubbleRay() {
        super(new Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(RubbleRay.MODID, "rubble_ray"))));
        rubble = BlockTags.create(ResourceLocation.fromNamespaceAndPath(RubbleRay.MODID, "rubble"));
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
        int height = Config.height;
        int width = Config.width;
        int length = Config.length;
        int torches = Config.lightGap;
        String replacement = Config.airReplacement;
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
                level.playSound(null, player.blockPosition(), RubbleRay.RAY.get(), SoundSource.PLAYERS);
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
                                if (bid.is(rubble)) {
                                    level.setBlockAndUpdate(new BlockPos(x + k * deltax + j * deltaz, y + i, z + k * deltaz + j * deltax), Blocks.AIR.defaultBlockState());
                                }

                                if (i == height - 1) {
                                    bid = level.getBlockState(new BlockPos(x + k * deltax + j * deltaz, y + i + 1, z + k * deltaz + j * deltax));
                                    if (bid != Blocks.AIR.defaultBlockState()) {
                                        ++solid_count;
                                    }

                                    if (bid.is(Blocks.WATER) || bid.is(Blocks.LAVA)) {
                                            level.setBlockAndUpdate(new BlockPos(x + k * deltax + j * deltaz, y + i + 1, z + k * deltaz + j * deltax), Blocks.AIR.defaultBlockState());
                                    }
                                }

                                // FLOOR
                                if (Config.generateFloor) {
                                    bid = level.getBlockState(new BlockPos(x + k * deltax + j * deltaz, y - 1, z + k * deltaz + j * deltax));
                                    if ((bid.is(Blocks.AIR) || bid.is(Blocks.WATER) || bid.is(Blocks.LAVA))) {
                                        level.setBlockAndUpdate(
                                                new BlockPos(x + k * deltax + j * deltaz, y - 1, z + k * deltaz + j * deltax),
                                                getBlockStateForHeight(y - 1)
                                        );
                                    }
                                }

                                // ROOF
                                if (Config.generateRoof) {
                                    bid = level.getBlockState(new BlockPos(x + k * deltax + j * deltaz, y + height, z + k * deltaz + j * deltax));
                                    if ((bid.is(Blocks.AIR) || bid.is(Blocks.WATER) || bid.is(Blocks.LAVA) || bid.getBlock() instanceof FallingBlock)) {
                                        level.setBlockAndUpdate(
                                                new BlockPos(x + k * deltax + j * deltaz, y + height, z + k * deltaz + j * deltax),
                                                getBlockStateForHeight(y + height)
                                        );
                                    }
                                }

                                // WALLS
                                if (Config.generateWalls) {
                                    // Left wall (j == -width)
                                    if (j == -width) {
                                        BlockPos leftWallPos = new BlockPos(x + k * deltax + j * deltaz, y + i, z + k * deltaz + j * deltax);
                                        BlockState leftWallBlock = level.getBlockState(leftWallPos);
                                        if (leftWallBlock.is(Blocks.AIR) || leftWallBlock.is(Blocks.WATER) || leftWallBlock.is(Blocks.LAVA)) {
                                            level.setBlockAndUpdate(leftWallPos, getBlockStateForHeight(y + i));
                                        }
                                    }

                                    // Right wall (j == width)
                                    if (j == width) {
                                        BlockPos rightWallPos = new BlockPos(x + k * deltax + j * deltaz, y + i, z + k * deltaz + j * deltax);
                                        BlockState rightWallBlock = level.getBlockState(rightWallPos);
                                        if (rightWallBlock.is(Blocks.AIR) || rightWallBlock.is(Blocks.WATER) || rightWallBlock.is(Blocks.LAVA)) {
                                            level.setBlockAndUpdate(rightWallPos, getBlockStateForHeight(y + i));
                                        }
                                    }
                                }

                            }

                            if (i == height - 1 && solid_count == 0) {
                                for(j = -width; j <= width; ++j) {
                                    level.setBlockAndUpdate(new BlockPos(x + k * deltax + j * deltaz, y + i + 1, z + k * deltaz + j * deltax), Blocks.AIR.defaultBlockState());
                                }
                            }
                        }
                    }

                    for(k = 0; k < length; k += torches) {
                        bid = level.getBlockState(new BlockPos(x + k * deltax, y - 1, z + k * deltaz));
                        if ((bid.is(rubble))) {
                            level.setBlockAndUpdate(new BlockPos(x + k * deltax, y-1, z + k * deltaz), Blocks.REDSTONE_LAMP.defaultBlockState().setValue(RedstoneLampBlock.LIT, true));
                            level.setBlockAndUpdate(new BlockPos(x + k * deltax, y-2, z + k * deltaz), Blocks.REDSTONE_BLOCK.defaultBlockState());
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
    BlockState getBlockStateForHeight(int y) {
        return y <= -8 ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.STONE.defaultBlockState();
    }
}
