package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFire;
import cn.nukkit.block.BlockNetherPortal;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemFlintSteel extends ItemTool {

    public ItemFlintSteel() {
        this(0, 1);
    }

    public ItemFlintSteel(Integer meta) {
        this(meta, 1);
    }

    public ItemFlintSteel(Integer meta, int count) {
        super(FLINT_STEEL, meta, count, "Flint and Steel");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block target, int face, double fx, double fy, double fz) {
        if (block.getId() == AIR && (target instanceof BlockSolid)) {
            if (target.getId() == OBSIDIAN) {
                int targetX = target.getFloorX();
                int targetY = target.getFloorY();
                int targetZ = target.getFloorZ();
                int x_max = targetX;
                int x_min = targetX;
                int x;
                for (x = targetX + 1; level.getBlock(new Vector3(x, targetY, targetZ)).getId() == OBSIDIAN; x++) {
                    x_max++;
                }
                for (x = targetX - 1; level.getBlock(new Vector3(x, targetY, targetZ)).getId() == OBSIDIAN; x--) {
                    x_min--;
                }
                int count_x = x_max - x_min + 1;
                int z_max = targetZ;
                int z_min = targetZ;
                int z;
                for (z = targetZ + 1; level.getBlock(new Vector3(targetX, targetY, z)).getId() == OBSIDIAN; z++) {
                    z_max++;
                }
                for (z = targetZ - 1; level.getBlock(new Vector3(targetX, targetY, z)).getId() == OBSIDIAN; z--) {
                    z_min--;
                }
                int count_z = z_max - z_min + 1;
                int z_max_y = targetY;
                int z_min_y = targetY;
                int y;
                for (y = targetY; level.getBlock(new Vector3(targetX, y, z_max)).getId() == OBSIDIAN; y++) {
                    z_max_y++;
                }
                for (y = targetY; level.getBlock(new Vector3(targetX, y, z_min)).getId() == OBSIDIAN; y++) {
                    z_min_y++;
                }
                int x_max_y = targetY;
                int x_min_y = targetY;
                for (y = targetY; level.getBlock(new Vector3(x_max, y, targetZ)).getId() == OBSIDIAN; y++) {
                    x_max_y++;
                }
                for (y = targetY; level.getBlock(new Vector3(x_min, y, targetZ)).getId() == OBSIDIAN; y++) {
                    x_min_y++;
                }
                int y_max = Math.min(z_max_y, z_min_y) - 1;
                int count_y = y_max - targetY + 2;
                if (!(count_y >= 5 && count_y <= 23))	{
                	//System.out.println("Got to if thingy #1");
                	y_max = Math.min(x_max_y, x_min_y) - 1;
                	count_y = y_max - targetY + 2;
                }
                if ((count_x >= 4 && count_x <= 23 || count_z >= 4 && count_z <= 23) && count_y >= 5 && count_y <= 23) {
                	//System.out.println("Got to if thingy #2");
                    int count_up = 0;
                    for (int up_z = z_min; level.getBlock(new Vector3(targetX, y_max, up_z)).getId() == OBSIDIAN && up_z <= z_max; up_z++) {
                        count_up++;
                    }
                    System.out.println("\n" + count_up + " " + count_z);
                    if (count_up == count_z && count_up > 1) {
                        for (int block_z = z_min + 1; block_z < z_max; block_z++) {
                            for (int block_y = targetY + 1; block_y < y_max; block_y++) {
                                level.setBlock(new Vector3(targetX, block_y, block_z), new BlockNetherPortal());
                            }
                        }
                        return true;
                    }
                    count_up = 0;
                    for (int up_x = x_min; level.getBlock(new Vector3(up_x, y_max, targetZ)).getId() == OBSIDIAN && up_x <= x_max; up_x++) {
                        count_up++;
                    }
                    System.out.println("\n" + count_up + " " + count_x);
                    if (count_up == count_x && count_up > 1)	{
                    	System.out.println("placing blocks");
                    	int xa = targetX, ya = targetY + 1;
                    	while (level.getBlock(xa, ya, targetZ).getId() != Block.OBSIDIAN)	{
                    		while (level.getBlock(xa, ya, targetZ).getId() != Block.OBSIDIAN)	{
                    			level.setBlockIdAt(xa, ya, targetZ, Block.NETHER_PORTAL);
                    			xa++;
                    		}
                    		xa--;
                    		while (level.getBlock(xa, ya, targetZ).getId() != Block.OBSIDIAN)	{
                    			level.setBlockIdAt(xa, ya, targetZ, Block.NETHER_PORTAL);
                    			xa--;
                    		}
                    		xa++;
                    		ya++;
                    	}
                    	/*for (int block_x = x_min + 1; block_x < x_max; block_x++) {
                            for (int block_y = targetY + 1; block_y < y_max; block_y++) {
                                level.setBlock(new Vector3(block_x, block_y, targetZ), new BlockNetherPortal());
                            }
                        }*/
                    	return true;
                    }
                }
            }
            BlockFire fire = new BlockFire();
            fire.x = block.x;
            fire.y = block.y;
            fire.z = block.z;
            fire.level = level;

            if (fire.isBlockTopFacingSurfaceSolid(fire.getSide(Vector3.SIDE_DOWN)) || fire.canNeighborBurn()) {
                BlockIgniteEvent e = new BlockIgniteEvent(block, null, player, BlockIgniteEvent.BlockIgniteCause.FLINT_AND_STEEL);
                block.getLevel().getServer().getPluginManager().callEvent(e);

                if (!e.isCancelled()) {
                    level.setBlock(fire, fire, true);
                    level.scheduleUpdate(fire, fire.tickRate() + level.rand.nextInt(10));
                }
                return true;
            }
            if ((player.gamemode & 0x01) == 0 && this.useOn(block)) {
                if (this.getDamage() >= this.getMaxDurability()) {
                    player.getInventory().setItemInHand(new Item(Item.AIR, 0, 0));
                } else {
                    this.meta++;
                    player.getInventory().setItemInHand(this);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int getMaxDurability() {
        return ItemTool.DURABILITY_FLINT_STEEL;
    }
}
