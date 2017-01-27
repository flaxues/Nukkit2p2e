package cn.nukkit.level.generator.populator;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockChest;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.item.Item;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import mobs.de.kniffo80.mobplugin.entities.utils.Utils;

public class PopulatorEndFortress extends Populator {

	@Override
	public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
		if (true/*Utils.rand(1, 1000) == 394*/) {
			BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);

			int x = 2,
					y = 65,
					z = 2,
					floorCount = Utils.rand(1, 5);
			
			int usableX = x + chunkX * 16;
			int usableZ = z + chunkZ * 16;

			for (int currentFloor = 0; currentFloor < floorCount; currentFloor++) {
				
				y = 64 + currentFloor * 5;
				
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						for (int k = 0; k < 5; k++)	{
							//chunk.setBlock(x + i, y + k, z + j, Block.PURPUR_BLOCK);
							level.setBlockIdAt(usableX + i, y + k, usableZ + j, Block.PURPUR_BLOCK);
						}
					}
				}
				for (int i = 1; i < 7; i++) {
					for (int j = 1; j < 7; j++) {
						//chunk.setBlock(x + i, y, z + j, Block.END_BRICKS);
						level.setBlockIdAt(usableX + i, y, usableZ + j, Block.END_BRICKS);
					}
				}
				//Floor with end bricks
				
				for (int i = 0; i < 8; i++)	{
					for (int j = 1; j < 7; j++)	{
						for (int k = 1; k < 4; k++)	{
							//chunk.setBlock(x + i, y + k, z + j, Block.END_BRICKS);
							level.setBlockIdAt(usableX + i, y + k, usableZ + j, Block.END_BRICKS);
						}
					}
				}
				for (int i = 1; i < 7; i++)	{
					for (int j = 0; j < 8; j++)	{
						for (int k = 1; k < 4; k++)	{
							//chunk.setBlock(x + i, y + k, z + j, Block.END_BRICKS);
							level.setBlockIdAt(usableX + i, y + k, usableZ + j, Block.END_BRICKS);
						}
					}
				}
				//Fill everything up with end bricks
				
				for (int i = 1; i < 7; i++)	{
					for (int j = 1; j < 7; j++)	{
						for (int k = 1; k < 4; k++)	{
							//chunk.setBlock(x + i, y + k, z + j, Block.AIR);
							level.setBlockIdAt(usableX + i, y + k, usableZ + j, Block.AIR);
						}
					}
				}
				//Fill the inside up with air
				
				//   |
				//   |
				// \ | /
				//  \|/
				//
				//Decorations
				
				if (currentFloor != 0)	{
					//chunk.setBlock(x + 4, y, z + 1, Block.LADDER);
					level.setBlockIdAt(usableX + 4, y, usableZ + 1, Block.LADDER);
				}
				if (currentFloor == floorCount)	{
					//chunk.setBlock(x + 4, y + 5, z + 1, Block.LADDER);
					level.setBlockIdAt(usableX + 4, y + 5, usableZ + 1, Block.LADDER);
				}
				for (int i = 1; i < 4; i++)	{
					//chunk.setBlock(x + 4, y + i, z + 1, Block.LADDER);
					level.setBlockIdAt(usableX + 4, y + i, usableZ + 1, Block.LADDER);
				}
				//Ladders
				
				if (Utils.rand())	{
					//((Level) level).setBlock(new Vector3(x + 4, y + 1, z + 7), new BlockChest());
					//chunk.setBlock(x + 4, y + 1, z + 7, Block.CHEST);
					/*BlockEntityChest chest = (BlockEntityChest) chunk.getTile(x + 4, y + 1, z + 7);
					
					for (int i = 0; i < chest.getInventory().getSize(); i++)	{
						chest.getInventory().setItem(i, chooseChestItem());
					}*/
				}
				
			}
		}
	}
	
	public Item chooseChestItem()	{
		
		if (Utils.rand(0, 15) == 5)	{
			return Item.get(Item.END_ROD, 0, Utils.rand(1, 5));
		}
		
		if (Utils.rand(0, 10) == 5)	{
			return Item.get(Item.ENDER_PEARL, 0, Utils.rand(1, 3));
		}
		
		if (Utils.rand(0, 50) == 5)	{
			return Item.get(Item.ELYTRA);
		}
		
		return Item.get(Item.AIR);
	}
}
