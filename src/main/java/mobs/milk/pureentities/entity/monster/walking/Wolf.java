package mobs.milk.pureentities.entity.monster.walking;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import mobs.milk.pureentities.entity.monster.WalkingMonster;

import java.util.HashMap;

public class Wolf extends WalkingMonster{
    public static final int NETWORK_ID = 14;

    int angry = 0;

    public Wolf(FullChunk chunk, CompoundTag nbt){
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
        return 0.9f;
    }

    @Override
    public double getSpeed(){
        return 1.2;
    }

    @Override
    protected void initEntity(){
        super.initEntity();

        if(this.namedTag.contains("Angry")){
            this.angry = this.namedTag.getInt("Angry");
        }

        this.setMaxHealth(8);
        this.fireProof = true;
        this.setDamage(new int[]{0, 3, 4, 6});
    }

    @Override
    public void saveNBT(){
        super.saveNBT();
        this.namedTag.putInt("Angry", this.angry);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance){
    	return this.isAngry() && super.targetOption(creature, distance);
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
    public void attackEntity(Entity player){
        if(this.attackDelay > 10 && this.distanceSquared(player) < 1.6){
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

    @Override
    public Item[] getDrops(){
    	return new Item[0];
    }

}
