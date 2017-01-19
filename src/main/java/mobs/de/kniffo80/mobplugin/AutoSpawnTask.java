package mobs.de.kniffo80.mobplugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import com.google.common.collect.Lists;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import mobs.de.kniffo80.mobplugin.entities.animal.flying.Bat;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.Chicken;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.Cow;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.Donkey;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.Horse;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.Mooshroom;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.Mule;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.Ocelot;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.Pig;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.Rabbit;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.Sheep;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.SkeletonHorse;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.ZombieHorse;
import mobs.de.kniffo80.mobplugin.entities.monster.flying.Blaze;
import mobs.de.kniffo80.mobplugin.entities.monster.flying.Ghast;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.CaveSpider;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.Creeper;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.Enderman;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.IronGolem;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.PigZombie;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.Silverfish;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.Skeleton;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.SnowGolem;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.Spider;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.Wolf;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.Zombie;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.ZombieVillager;
import mobs.de.kniffo80.mobplugin.entities.utils.Utils;

public class AutoSpawnTask extends TimerTask {

	private Map<Integer, Integer> maxSpawns = new HashMap<>();
	
	public static HashMap<Integer, Integer> hostileMobsOverWorld = new HashMap<>();
	public static HashMap<Integer, Integer> passiveMobsOverWorld = new HashMap<>();

	static {
		hostileMobsOverWorld.put(Creeper.NETWORK_ID, 3);
		hostileMobsOverWorld.put(Enderman.NETWORK_ID, 1);
		hostileMobsOverWorld.put(Skeleton.NETWORK_ID, 3);
		hostileMobsOverWorld.put(Spider.NETWORK_ID, 3);
		hostileMobsOverWorld.put(Zombie.NETWORK_ID, 5);
		
		passiveMobsOverWorld.put(Chicken.NETWORK_ID, 3);
		passiveMobsOverWorld.put(Cow.NETWORK_ID, 3);
		passiveMobsOverWorld.put(Pig.NETWORK_ID, 3);
		passiveMobsOverWorld.put(Sheep.NETWORK_ID, 3);
	}
	private Config pluginConfig = null;

	public AutoSpawnTask(MobPlugin plugin) {
		this.pluginConfig = plugin.getConfig();

		prepareMaxSpawns();

		FileLogger.info("Starting AutoSpawnTask");
	}

	@Override
	public void run() {
		List<Player> players = Lists.newArrayList(Server.getInstance().players.values().iterator());
		for (Player p : players)	{
			//System.out.println("player");
			Level lvl = p.getLevel();
			if (lvl.getId() == Server.getInstance().getDefaultLevel().getId())	{ //Overworld
				//System.out.println("level");
				int time = lvl.getTime() % Level.TIME_FULL;
				if (time >= Level.TIME_NIGHT && time < Level.TIME_SUNRISE)	{ //NIGHT
					//System.out.println("night");
					for (int id : hostileMobsOverWorld.keySet())	{
						//System.out.println("key");
						int targetTries = hostileMobsOverWorld.get(id);
						int x = 0, y = 0, z = 0;
						for (int tries = 0; tries < targetTries; tries++)	{
							x = Utils.rand((int) p.x - 40, (int) p.x + 40);
							z = Utils.rand((int) p.z - 40, (int) p.z + 40);
							y = Utils.rand(60, 256);
							if (isSafe(x, y, z, lvl, true))	{
								createEntity(id, new Position(x + 0.5, y, z + 0.5, lvl));
								//System.out.println("spawned");
							}
						}
					}
				} else { //DAY
					//System.out.println("day");
					for (int id : passiveMobsOverWorld.keySet())	{
						//System.out.println("key");
						int targetTries = passiveMobsOverWorld.get(id);
						int x = 0, y = 0, z = 0;
						for (int tries = 0; tries < targetTries; tries++)	{
							x = Utils.rand((int) p.x - 40, (int) p.x + 40);
							z = Utils.rand((int) p.z - 40, (int) p.z + 40);
							y = Utils.rand(62, 256);
							if (isSafe(x, y, z, lvl, false) && entitySpawnAllowed(lvl, id) && Utils.rand(0, 20) == 4)	{
								createEntity(id, new Position(x + 0.5, y, z + 0.5, lvl));
								//System.out.println("spawned");
							}
						}
					}
				}
			}
		}
	}
	
	public boolean isSafe(int x, int y, int z, Level lvl, boolean monster)	{
		Level world = lvl;

		if (world.getBlock(x, y, z).getId() == Block.AIR
				&& world.getBlock(x, y + 1, z).getId() == Block.AIR
				&& world.getBlock(x, y - 1, z).getId() != Block.LAVA
				&& world.getBlock(x, y - 1, z).getId() != Block.STILL_LAVA
				&& world.getBlock(x, y - 1, z).getId() != Block.WATER
				&& world.getBlock(x, y - 1, z).getId() != Block.STILL_WATER
				&& world.getBlock(x, y - 1, z).getId() != Block.FIRE
				&& world.getBlock(x, y - 1, z).getId() != Block.AIR) {
			return true;
		}
		
		return false;
	}

	private void prepareMaxSpawns() {
		maxSpawns.put(Bat.NETWORK_ID, this.pluginConfig.getInt("max-spawns.bat", 0));
		maxSpawns.put(Blaze.NETWORK_ID, this.pluginConfig.getInt("max-spawns.blaze", 0));
		maxSpawns.put(CaveSpider.NETWORK_ID, this.pluginConfig.getInt("max-spawns.cave-spider", 0));
		maxSpawns.put(Chicken.NETWORK_ID, this.pluginConfig.getInt("max-spawns.chicken", 0));
		maxSpawns.put(Cow.NETWORK_ID, this.pluginConfig.getInt("max-spawns.cow", 0));
		maxSpawns.put(Creeper.NETWORK_ID, this.pluginConfig.getInt("max-spawns.creeper", 0));
		maxSpawns.put(Donkey.NETWORK_ID, this.pluginConfig.getInt("max-spawns.donkey", 0));
		maxSpawns.put(Enderman.NETWORK_ID, this.pluginConfig.getInt("max-spawns.enderman", 0));
		maxSpawns.put(Ghast.NETWORK_ID, this.pluginConfig.getInt("max-spawns.ghast", 0));
		maxSpawns.put(Horse.NETWORK_ID, this.pluginConfig.getInt("max-spawns.horse", 0));
		maxSpawns.put(IronGolem.NETWORK_ID, this.pluginConfig.getInt("max-spawns.iron-golem", 0));
		maxSpawns.put(Mooshroom.NETWORK_ID, this.pluginConfig.getInt("max-spawns.mooshroom", 0));
		maxSpawns.put(Mule.NETWORK_ID, this.pluginConfig.getInt("max-spawns.mule", 0));
		maxSpawns.put(Ocelot.NETWORK_ID, this.pluginConfig.getInt("max-spawns.ocelot", 0));
		maxSpawns.put(Pig.NETWORK_ID, this.pluginConfig.getInt("max-spawns.pig", 0));
		maxSpawns.put(PigZombie.NETWORK_ID, this.pluginConfig.getInt("max-spawns.pig-zombie", 0));
		maxSpawns.put(Rabbit.NETWORK_ID, this.pluginConfig.getInt("max-spawns.rabbit", 0));
		maxSpawns.put(Silverfish.NETWORK_ID, this.pluginConfig.getInt("max-spawns.silverfish", 0));
		maxSpawns.put(Sheep.NETWORK_ID, this.pluginConfig.getInt("max-spawns.sheep", 0));
		maxSpawns.put(Skeleton.NETWORK_ID, this.pluginConfig.getInt("max-spawns.skeleton", 0));
		maxSpawns.put(SkeletonHorse.NETWORK_ID, this.pluginConfig.getInt("max-spawns.skeleton-horse", 0));
		maxSpawns.put(SnowGolem.NETWORK_ID, this.pluginConfig.getInt("max-spawns.snow-golem", 0));
		maxSpawns.put(Spider.NETWORK_ID, this.pluginConfig.getInt("max-spawns.spider", 0));
		maxSpawns.put(Wolf.NETWORK_ID, this.pluginConfig.getInt("max-spawns.wolf", 0));
		maxSpawns.put(Zombie.NETWORK_ID, this.pluginConfig.getInt("max-spawns.zombie", 0));
		maxSpawns.put(ZombieHorse.NETWORK_ID, this.pluginConfig.getInt("max-spawns.zombie-horse", 0));
		maxSpawns.put(ZombieVillager.NETWORK_ID, this.pluginConfig.getInt("max-spawns.zombie-villager", 0));
	}

	public boolean entitySpawnAllowed(Level level, int networkId) {
		int count = countEntity(level, networkId);
		if (count < maxSpawns.get(networkId)) {
			return true;
		}
		return false;
	}

	private int countEntity(Level level, int networkId) {
		int count = 0;
		for (Entity entity : level.getEntities()) {
			if (entity.isAlive() && entity.getNetworkId() == networkId) {
				count++;
			}
		}
		return count;
	}

	public void createEntity(Object type, Position pos) {
		Entity entity = MobPlugin.create(type, pos);
		if (entity != null) {
			entity.spawnToAll();
		}
	}

	public int getRandomSafeXZCoord(int degree, int safeDegree, int correctionDegree) {
		int addX = Utils.rand(degree / 2 * -1, degree / 2);
		if (addX >= 0) {
			if (degree < safeDegree) {
				addX = safeDegree;
				addX += Utils.rand(correctionDegree / 2 * -1, correctionDegree / 2);
			}
		} else {
			if (degree > safeDegree) {
				addX = -safeDegree;
				addX += Utils.rand(correctionDegree / 2 * -1, correctionDegree / 2);
			}
		}
		return addX;
	}

	public int getSafeYCoord(Level level, Position pos, int needDegree) {
		int x = (int) pos.x;
		int y = (int) pos.y;
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