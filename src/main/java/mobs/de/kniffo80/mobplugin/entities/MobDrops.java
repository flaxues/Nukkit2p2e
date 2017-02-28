package mobs.de.kniffo80.mobplugin.entities;

import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import mobs.de.kniffo80.mobplugin.entities.animal.flying.Bat;
import mobs.de.kniffo80.mobplugin.entities.animal.walking.*;
import mobs.de.kniffo80.mobplugin.entities.monster.flying.Blaze;
import mobs.de.kniffo80.mobplugin.entities.monster.flying.Ghast;
import mobs.de.kniffo80.mobplugin.entities.monster.walking.*;
import mobs.de.kniffo80.mobplugin.entities.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by daporkchop on 22.02.17.
 */
public class MobDrops {
    public static final HashMap<Class<? extends BaseEntity>, MobDrop[]> DROPS = new HashMap<>();

    static {
        //Bat
        DROPS.put(Bat.class, new MobDrop[]{});

        //Chicken
        DROPS.put(Chicken.class, new MobDrop[]{
                new NormalMobDrop(0,2, Item.FEATHER,0),
                new BurningMobDrop(
                        new NormalMobDrop(0, 1, Item.COOKED_CHICKEN, 0),
                        new NormalMobDrop(0, 1, Item.RAW_CHICKEN, 0)
                )
        });

        //Cow
        DROPS.put(Cow.class, new MobDrop[]{
                new NormalMobDrop(0, 2, Item.LEATHER, 0),
                new BurningMobDrop(
                        new NormalMobDrop(1, 3, Item.COOKED_BEEF, 0),
                        new NormalMobDrop(1, 3, Item.RAW_BEEF, 0)
                )
        });

        //Horse
        //TODO: implement horse lul

        //Mooshroom
        DROPS.put(Mooshroom.class, new MobDrop[]{
                new NormalMobDrop(0, 2, Item.LEATHER, 0),
                new BurningMobDrop(
                        new NormalMobDrop(1, 3, Item.COOKED_BEEF, 0),
                        new NormalMobDrop(1, 3, Item.RAW_BEEF, 0)
                )
        });

        //Ocelot
        DROPS.put(Ocelot.class, new MobDrop[]{});

        //Pig
        DROPS.put(Pig.class, new MobDrop[]{
                new BurningMobDrop(
                        new NormalMobDrop(1, 3, Item.COOKED_PORKCHOP, 0),
                        new NormalMobDrop(1, 3, Item.RAW_PORKCHOP, 0)
                )
        });

        //Rabbit
        DROPS.put(Rabbit.class, new MobDrop[]{
                new NormalMobDrop(0, 1, Item.RABBIT_HIDE, 0),
                new BurningMobDrop(
                        new NormalMobDrop(0, 1, Item.COOKED_RABBIT, 0),
                        new NormalMobDrop(0, 1, Item.RAW_RABBIT, 0)
                ),
                new RareMobDrop(1, 1, Item.RABBIT_FOOT, 0)
        });

        //Sheep
        DROPS.put(Sheep.class, new MobDrop[]{
                new NormalMobDrop(1, 1, Item.WOOL, 0),
                new BurningMobDrop(
                        new NormalMobDrop(1, 2, Item.COOKED_MUTTON, 0),
                        new NormalMobDrop(1, 2, Item.RAW_MUTTON, 0)
                )
        });

        //Iron Golem
        DROPS.put(IronGolem.class, new MobDrop[]{
                new NormalMobDrop(3, 5, Item.IRON_INGOT, 0),
                new NormalMobDrop(0, 2, Item.FLOWER, 0)  //poppy
        });

        //Snow Golem
        DROPS.put(SnowGolem.class, new MobDrop[]{
                new NormalMobDrop(0, 15, Item.SNOWBALL, 0)
        });

        //Blaze
        DROPS.put(Blaze.class, new MobDrop[]{
                new NormalMobDrop(0, 1, Item.BLAZE_ROD, 0)
        });

        //Cave Spider
        DROPS.put(CaveSpider.class, new MobDrop[]{
                new NormalMobDrop(0, 2, Item.STRING, 0),
                new NormalMobDrop(0, 1, Item.SPIDER_EYE, 0)
        });

        //Spider
        DROPS.put(Spider.class, new MobDrop[]{
                new NormalMobDrop(0, 2, Item.STRING, 0),
                new NormalMobDrop(0, 1, Item.SPIDER_EYE, 0)
        });

        //Creeper
        DROPS.put(Creeper.class, new MobDrop[]{
                new NormalMobDrop(0, 2, Item.GUNPOWDER, 0)
        });

        //Enderman
        DROPS.put(Enderman.class, new MobDrop[]{
                new NormalMobDrop(0, 1, Item.ENDER_PEARL, 0)
        });

        //Ghast
        DROPS.put(Ghast.class, new MobDrop[]{
                new NormalMobDrop(0, 1, Item.GHAST_TEAR, 0),
                new NormalMobDrop(0, 2, Item.GUNPOWDER, 0)
        });

        //Silverfish
        DROPS.put(Silverfish.class, new MobDrop[]{});

        //Skeleton
        DROPS.put(Skeleton.class, new MobDrop[]{
                new NormalMobDrop(0, 2, Item.ARROW, 0),
                new NormalMobDrop(0, 2, Item.BONE, 0)
        });

        //Zombie
        DROPS.put(Zombie.class, new MobDrop[]{
                new NormalMobDrop(0, 2, Item.ROTTEN_FLESH, 0),
                new RareMobDrop(1, 1, Item.IRON_INGOT, 0),
                new RareMobDrop(1, 1, Item.CARROT, 0),
                new RareMobDrop(1, 1, Item.POTATO, 0),
        });

        //Zombie Villager
        DROPS.put(ZombieVillager.class, new MobDrop[]{
                new NormalMobDrop(0, 2, Item.ROTTEN_FLESH, 0),
                new RareMobDrop(1, 1, Item.IRON_INGOT, 0),
                new RareMobDrop(1, 1, Item.CARROT, 0),
                new RareMobDrop(1, 1, Item.POTATO, 0),
        });

        //Zombie Pigman
        DROPS.put(PigZombie.class, new MobDrop[]{
                new NormalMobDrop(0, 1, Item.ROTTEN_FLESH, 0),
                new NormalMobDrop(0, 1, Item.GOLD_NUGGET, 0),
                new RareMobDrop(1, 1, Item.GOLD_INGOT, 0)
        });

        //Wolf
        DROPS.put(Wolf.class, new MobDrop[]{});
    }

    public static Item[] getDrops(BaseEntity entity)    {
        ArrayList<Item> arr = new ArrayList<>();
        for (MobDrop drop : DROPS.get(entity.getClass()))   {
            arr.add(drop.getDrop(entity));
        }
        return arr.toArray(new Item[arr.size()]);
    }

    public static interface MobDrop   {
        public Item getDrop(Entity e);
    }

    public static class NormalMobDrop implements MobDrop {
        public int minCount = 0;
        public int maxCout = 0;
        public int meta;
        public int item = Item.AIR;

        public NormalMobDrop(int min, int max, int item, int meta) {
            this.minCount = min;
            this.maxCout = max;
            this.item = item;
            this.meta = meta;
        }

        @Override
        public Item getDrop(Entity e)   {
            return Item.get(item, meta, Utils.rand(minCount, maxCout));
        }
    }

    public static class BurningMobDrop implements MobDrop    {

        public MobDrop ON_FIRE;
        public MobDrop NOT_ON_FIRE;

        public BurningMobDrop(MobDrop on, MobDrop off)   {
            this.ON_FIRE = on;
            this.NOT_ON_FIRE = off;
        }

        @Override
        public Item getDrop(Entity e)   {
            if (e.isOnFire())   {
                return ON_FIRE.getDrop(e);
            } else {
                return NOT_ON_FIRE.getDrop(e);
            }
        }
    }

    public static class RareMobDrop extends NormalMobDrop {

        public RareMobDrop(int min, int max, int item, int meta) {
            super(min, max, item, meta);
        }

        @Override
        public Item getDrop(Entity e)   {
            if (Utils.rand(0, 50) == 1)  { //~2% chance
                return super.getDrop(e);
            }

            return Item.get(Item.AIR, 0, 0);
        }
    }
}