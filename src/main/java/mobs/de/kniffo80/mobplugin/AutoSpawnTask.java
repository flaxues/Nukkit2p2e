package mobs.de.kniffo80.mobplugin;

import java.util.TimerTask;

import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import mobs.de.kniffo80.mobplugin.entities.utils.Utils;

public class AutoSpawnTask extends TimerTask {

	@SuppressWarnings("deprecation")
	public void run() {
		Server.getInstance().getOnlinePlayers().forEach((name, player) -> {
			if (Utils.rand()) {
				return;
			}

			Position pos = player.getPosition();
			pos.x += this.getRandomSafeXZCoord();
			pos.z += this.getRandomSafeXZCoord();
			pos.y = this.getSafeYCoord(player.getLevel(), pos, 3);

			if (pos.y > 127 || pos.y < 1
					|| player.getLevel().getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) {
				return;
			}

			int blockId = player.getLevel().getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
			int biomeId = player.getLevel().getBiomeId((int) pos.x, (int) pos.z);
			int blockLightLevel = Math.max(player.getLevel().getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z),
					player.getLevel().getBlockSkyLightAt((int) pos.x, (int) pos.y, (int) pos.z));

			if (player.getLevel() == Server.getInstance().getNetherLevel()) {
				switch (blockId) {
				case Block.NETHER_BRICKS_STAIRS:
				case Block.NETHER_BRICKS:
					if (Utils.rand(0, 5) == 5) {
						this.createEntity("Blaze", pos.add(0, 2.8, 0));
					}
					break;
				case Block.NETHERRACK:
				case Block.SOUL_SAND:
					if (Utils.rand()) {
						if (Utils.rand(0, 200) < 199) {
							this.createEntity("PigZombie", pos.add(0, 3.8, 0));
						} else {
							if (!player.getLevel().getBlock(pos.add(0, 10, 0)).isSolid() && pos.y < 123) {
								this.createEntity("Ghast", pos.add(0, 5, 0));
							}
						}
					}
					break;
				}
			} else if (player.getLevel() == Server.getInstance().getDefaultLevel()) {
				if (blockLightLevel < 6) { // check if it's dark enough
					switch (Utils.rand(1, 6)) {
					case 1:
						this.createEntity("Creeper", pos.add(0, 2.8, 0));
						break;
					case 2:
						this.createEntity("Enderman", pos.add(0, 3.8, 0));
						break;
					case 3:
						this.createEntity("Skeleton", pos.add(0, 2.8, 0));
						break;
					case 4:
						if (Utils.rand()) {
							this.createEntity("Spider", pos.add(0, 2.12, 0));
						} else {
							this.createEntity("CaveSpider", pos.add(0, 1.8, 0));
						}
						break;
					case 5:
						this.createEntity("Zombie", pos.add(0, 2.8, 0));
						break;
					case 6:
						this.createEntity("ZombieVillager", pos.add(0, 2.8, 0));
						break;
					}
				} else {
					if (biomeId == Biome.JUNGLE && (blockId == Block.GRASS || blockId == Block.LEAVE)
							&& Utils.rand(1, 100) <= 20) {
						this.createEntity("Ocelot", pos.add(0, 1.9, 0));
						return;
					}
					if (biomeId == Biome.TAIGA && blockId == Block.GRASS && Utils.rand(1, 100) <= 1) {
						this.createEntity("Wolf", pos.add(0, 1.9, 0));
						return;
					}

					if (blockId == Block.GRASS) {
						switch (Utils.rand(1, 5)) {
						case 1:
							this.createEntity("Chicken", pos.add(0, 1.7, 0));
							break;
						case 2:
							this.createEntity("Cow", pos.add(0, 2.3, 0));
							break;
						case 3:
							this.createEntity("Pig", pos.add(0, 1.9, 0));
							break;
						case 4:
							this.createEntity("Rabbit", pos.add(0, 1.75, 0));
							break;
						case 5:
							this.createEntity("Sheep", pos.add(0, 2.3, 0));
							break;
						}
					}
				}
			} else if (player.getLevel() == Server.getInstance().getEndLevel()) {
				if (Utils.rand(0, 5) == 1) {
					this.createEntity("Enderman", pos.add(0, 3.8, 0));
				}
			} else {
				Server.getInstance().getLogger().alert("Trying to spawn mob in impossible dimension!!!");
			}
		});
	}

	public void createEntity(Object type, Position pos) {
		Entity entity = MobPlugin.create(type, pos);
		if (entity != null) {
			entity.spawnToAll();
		}
	}

	public int getRandomSafeXZCoord() {
		int addX = Utils.rand(-80, 80);
		while (addX > -16 && addX < 16) {
			addX = Utils.rand(-80, 80);
		}
		return addX;
	}

	public int getSafeYCoord(Level level, Position pos, int needDegree) {
		int x = (int) pos.x;
		int y = level == Server.getInstance().getNetherLevel() && pos.y > 128 ? 64 : (int) pos.y;
		int z = (int) pos.z;

		if (level.getBlockIdAt(x, y, z) == Block.AIR) {
			while (true) {
				y--;
				if (y > 127) {
					y = 128;
					break;
				}
				if (y < 1) {
					y = 0;
					break;
				}
				if (level.getBlockIdAt(x, y, z) != Block.AIR) {
					int checkNeedDegree = needDegree;
					int checkY = y;
					while (true) {
						checkY++;
						checkNeedDegree--;
						if (checkY > 255 || checkY < 1 || level.getBlockIdAt(x, checkY, z) != Block.AIR) {
							break;
						}

						if (checkNeedDegree <= 0) {
							return y;
						}
					}
				}
			}
		} else {
			while (true) {
				y++;
				if (y > 127) {
					y = 128;
					break;
				}

				if (y < 1) {
					y = 0;
					break;
				}

				if (level.getBlockIdAt(x, y, z) != Block.AIR) {
					int checkNeedDegree = needDegree;
					int checkY = y;
					while (true) {
						checkY--;
						checkNeedDegree--;
						if (checkY > 255 || checkY < 1 || level.getBlockIdAt(x, checkY, z) != Block.AIR) {
							break;
						}

						if (checkNeedDegree <= 0) {
							return y;
						}
					}
				}
			}
		}
		return y;
	}
}