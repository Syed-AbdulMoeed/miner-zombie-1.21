package miner.zombie.Entity.MinerZombie;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class MinerZombieEntity extends ZombieEntity {

    private MinerState currentState = MinerState.IDLE;
    private BlockPos targetBlock;
    private final Set<BlockPos> unreachableBlocks = new HashSet<>();
    private BlockPos lastPos;
    private int stuckTicks = 0;
    private final int STUCK_THRESHOLD = 30; // 1.5 second at 20 ticks/sec


    public MinerZombieEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    protected void initGoals() {
        // No vanilla goals
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) return; // AI runs server-side only

        switch (currentState) {
            case IDLE -> searchForTarget();
            case MOVING_TO_TARGET -> moveToTarget();
            case MINING -> mineTarget();
        }
        // ----- Stuck detection -----
        BlockPos currentPos = this.getBlockPos();
        if (currentPos.equals(lastPos)) {
            stuckTicks++;
        } else {
            stuckTicks = 0;
        }
        lastPos = currentPos;

        if (stuckTicks > STUCK_THRESHOLD) {
            unreachableBlocks.add(targetBlock);
            targetBlock = null;
            currentState = MinerState.IDLE;
            stuckTicks = 0;
            System.out.println("[MinerZombie] Block unreachable, skipping.");
        }
    }

    private void searchForTarget() {
        int radius = 10;
        BlockPos origin = this.getBlockPos();

        for (BlockPos pos : BlockPos.iterateOutwards(origin, radius, radius, radius)) {
            if (unreachableBlocks.contains(pos)) continue; // ignore unreachable

            Block block = this.getWorld().getBlockState(pos).getBlock();

            if (isDesiredBlock(block)) {
                targetBlock = pos;
                currentState = MinerState.MOVING_TO_TARGET;
                return;
            }
        }
    }

    private void moveToTarget() {
        if (targetBlock == null) {
            currentState = MinerState.IDLE;
            return;
        }

        this.getNavigation().startMovingTo(
                targetBlock.getX(),
                targetBlock.getY(),
                targetBlock.getZ(),
                1.0
        );
        System.out.println("[MinerZombie] State: MOVING_TO_TARGET to " + targetBlock);
        if (this.getBlockPos().isWithinDistance(targetBlock, 2)) {
            currentState = MinerState.MINING;

        }
    }

    private void mineTarget() {
        if (targetBlock == null) {
            currentState = MinerState.IDLE;
            return;
        }

        World world = this.getWorld();
        if (!(world instanceof ServerWorld serverWorld)) return;

        var blockState = world.getBlockState(targetBlock);

        if (!isDesiredBlock(blockState.getBlock())) {
            targetBlock = null;
            currentState = MinerState.IDLE;
            return;
        }

        // Generate proper loot-table drops
        java.util.List<ItemStack> drops = Block.getDroppedStacks(
                blockState,
                serverWorld,
                targetBlock,
                world.getBlockEntity(targetBlock),
                this,
                ItemStack.EMPTY
        );

        // Deliver to nearest player (since using /summon)
        PlayerEntity player = serverWorld.getClosestPlayer(this, 32);

        if (player != null) {
            for (ItemStack stack : drops) {
                if (!player.getInventory().insertStack(stack)) {
                    player.dropItem(stack, false);
                }
            }
        }

        // Remove the block
        world.setBlockState(targetBlock, Blocks.AIR.getDefaultState(), 3);

        targetBlock = null;
        currentState = MinerState.IDLE;
    }




    private boolean isDesiredBlock(Block block) {
        return block == Blocks.COAL_ORE
                || block == Blocks.IRON_ORE
                || block == Blocks.DIAMOND_ORE;
    }

    public static DefaultAttributeContainer.Builder createMinerAttributes() {
        return ZombieEntity.createZombieAttributes();
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }

}