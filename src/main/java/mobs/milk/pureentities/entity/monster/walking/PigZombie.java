package mobs.milk.pureentities.entity.monster.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.ItemSwordGold;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import mobs.milk.pureentities.entity.monster.WalkingMonster;
import mobs.milk.pureentities.util.Utils;

import java.util.HashMap;

public class PigZombie extends WalkingMonster{
    public static final int NETWORK_ID = 36;

    int angry = 0;

    public PigZombie(FullChunk chunk, CompoundTag nbt){
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId(){
        return NETWORK_ID;
    }

    @Override
    public float getWidth(){
        return 0.72f;
    }

    @Override
    public float getHeight(){
        return 1.8f;
    }

    @Override
    public float getEyeHeight(){
        return 1.62f;
    }

    @Override
    public double getSpeed(){
        return 1.15;
    }

    @Override
    protected void initEntity(){
        super.initEntity();

        if(this.namedTag.contains("Angry")){
            this.angry = this.namedTag.getInt("Angry");
        }

        this.fireProof = true;
        this.setDamage(new int[]{0, 5, 9, 13});
    }

    @Override
    public void saveNBT(){
        super.saveNBT();
        this.namedTag.putInt("Angry", this.angry);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance){
        if(
            distance <= 100 && this.isAngry()
            && creature instanceof PigZombie && !((PigZombie) creature).isAngry()
        ){
            ((PigZombie) creature).setAngry(1000);
        }
        return this.isAngry() && super.targetOption(creature, distance);
    }

    @Override
    public void attackEntity(Entity player){
        if(this.attackDelay > 10 && this.distanceSquared(player) < 1.44){
            this.attackDelay = 0;
            HashMap<Integer, Float> damage = new HashMap<>();
            damage.put(EntityDamageEvent.MODIFIER_BASE, (float) this.getDamage());

            if(player instanceof Player){
                float points = 0;
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += armorValues.getOrDefault(i.getId(), 0f);
                }

                damage.put(EntityDamageEvent.MODIFIER_ARMOR, (float) (damage.getOrDefault(EntityDamageEvent.MODIFIER_ARMOR, 0f) - Math.floor(damage.getOrDefault(EntityDamageEvent.MODIFIER_BASE, 1f) * points * 0.04)));
            }
            player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.CAUSE_ENTITY_ATTACK, damage));
        }
    }

    public boolean isAngry(){
        return this.angry > 0;
    }

    public void setAngry(int val){
        this.angry = val;
    }

    @Override
    public void attack(EntityDamageEvent ev){
        super.attack(ev);

        if(!ev.isCancelled()){
            this.setAngry(1000);
        }
    }

    @Override
    public void spawnTo(Player player){
        super.spawnTo(player);

        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = this.getId();
        pk.item = new ItemSwordGold();
        pk.slot = 10;
        pk.selectedSlot = 10;
        player.dataPacket(pk);
    }

    @Override
    public Item[] getDrops(){
        if(this.lastDamageCause instanceof EntityDamageByEntityEvent){
            switch(Utils.rand(0, 2)){
                case 0:
                    return new Item[]{Item.get(Item.FLINT, 0, 1)};
                case 1:
                    return new Item[]{Item.get(Item.GUNPOWDER, 0, 1)};
                case 2:
                    return new Item[]{Item.get(Item.REDSTONE_DUST, 0, 1)};
            }
        }
        return new Item[0];
    }

}
