package cn.nukkit.level.generator.populator;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockClay;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.math.NukkitRandom;

public class PopulatorClay extends Populator {

	@SuppressWarnings("unused")
	private OreType type = new OreType(new BlockClay(), 1, 20, 1, 64);

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
        /*int bx = chunkX * 16;
        int bz = chunkZ * 16;
        int x = random.nextRange(0, 15);
        int z = random.nextRange(0, 15);
        int y = this.getLowestWorkableBlock(level, x, z);
        if (y < 65) {
        	new ObjectOre(random, type, Block.DIRT).placeObject(level, bx + x, y, bz + z);
        }*/
    }

    @SuppressWarnings("unused")
	private int getLowestWorkableBlock(ChunkManager chunk, int x, int z) {
        int y;
        for (y = 1; y < 256; y++) {
            int b = chunk.getBlockIdAt(x, y, z);
            if (b == Block.WATER) {
                break;
            }
        }
        return y == 1 ? 45 : y - 1;
    }
}
