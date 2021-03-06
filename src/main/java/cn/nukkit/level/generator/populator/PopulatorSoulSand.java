package cn.nukkit.level.generator.populator;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSoulSand;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.object.ore.ObjectOre;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.math.NukkitRandom;

public class PopulatorSoulSand extends Populator {
	
	private OreType type = new OreType(new BlockSoulSand(), 1, 35, 1, 128);

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);
        int bx = chunkX << 4;
        int bz = chunkZ << 4;
        int x = random.nextRange(0, 15);
        int z = random.nextRange(0, 15);
        int y = this.getLowestWorkableBlock(chunk, x, z);
        if (y != -1) {
        	new ObjectOre(random, type, Block.NETHERRACK).placeObject(level, bx + x, y, bz + z);
        }
    }

    private int getLowestWorkableBlock(FullChunk chunk, int x, int z) {
        int y;
        for (y = 0; y < 256; y++) {
            int b = chunk.getBlockId(x, y, z);
            if (b == Block.AIR) {
                break;
            }
        }
        return y == 0 ? -1 : y - 1;
    }
}
