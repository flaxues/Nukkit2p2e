package mobs.milk.pureentities.entity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityMotionEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.potion.Effect;
import mobs.milk.pureentities.entity.monster.Monster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseEntity extends EntityCreature{

    protected int stayTime = 0;
    protected int moveTime = 0;

    protected Vector3 target = null;
    protected Entity followTarget = null;

    protected List<Block> blocksAround = new ArrayList<>();

    private boolean movement = true;
    private boolean friendly = false;
    private boolean wallcheck = true;

    public BaseEntity(FullChunk chunk, CompoundTag nbt){
        super(chunk, nbt);
    }

    public abstract Vector3 updateMove(int tickDiff);

    public boolean isFriendly(){
        return this.friendly;
    }

    public boolean isMovement(){
        return this.movement;
    }

    public boolean isKnockback(){
        return this.attackTime > 0;
    }

    public boolean isWallCheck(){
        return this.wallcheck;
    }

    public void setFriendly(boolean bool){
        this.friendly = bool;
    }

    public void setMovement(boolean value){
        this.movement = value;
    }

    public void setWallCheck(boolean value){
        this.wallcheck = value;
    }

    public double getSpeed(){
        return 1;
    }

    public Entity getTarget(){
        return this.followTarget != null ? this.followTarget : (this.target instanceof Entity ? (Entity) this.target : null);
    }

    public void setTarget(Entity target){
        this.followTarget = target;

        this.moveTime = 0;
        this.stayTime = 0;
        this.target = null;
    }

    @Override
    protected void initEntity(){
        super.initEntity();

        if(this.namedTag.contains("Movement")){
            this.setMovement(this.namedTag.getBoolean("Movement"));
        }

        if(this.namedTag.contains("WallCheck")){
            this.setWallCheck(this.namedTag.getBoolean("WallCheck"));
        }
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_IMMOBILE);
    }

    public void saveNBT(){
        super.saveNBT();
        this.namedTag.putBoolean("Movement", this.isMovement());
        this.namedTag.putBoolean("WallCheck", this.isWallCheck());
    }

    @Override
    public void spawnTo(Player player){
        if(!this.hasSpawned.containsKey(player.getLoaderId()) && player.usedChunks.containsKey(Level.chunkHash(this.chunk.getX(), this.chunk.getZ()))){
            AddEntityPacket pk = new AddEntityPacket();
            pk.entityUniqueId = this.getId();
            pk.entityRuntimeId = this.getId();
            pk.type = this.getNetworkId();
            pk.x = (float) this.x;
            pk.y = (float) this.y;
            pk.z = (float) this.z;
            pk.speedX = pk.speedY = pk.speedZ = 0;
            pk.yaw = (float) this.yaw;
            pk.pitch = (float) this.pitch;
            pk.metadata = this.dataProperties;
            player.dataPacket(pk);

            this.hasSpawned.put(player.getLoaderId(), player);
        }
    }

    @Override
    protected void updateMovement(){
        if(this.lastX != this.x || this.lastY != this.y || this.lastZ != this.z || this.lastYaw != this.yaw || this.lastPitch != this.pitch){
            this.lastX = this.x;
            this.lastY = this.y;
            this.lastZ = this.z;
            this.lastYaw = this.yaw;
            this.lastPitch = this.pitch;

            this.addMovement(this.x, this.y, this.z, this.yaw, this.pitch, this.yaw);
        }
    }

    public boolean targetOption(EntityCreature creature, double distance){
        if(this instanceof Monster){
            if(creature instanceof Player){
                Player player = (Player) creature;
                return !player.closed && player.spawned && player.isAlive() && player.isSurvival() && distance <= 100;
            }
            return creature.isAlive() && !creature.closed && distance <= 81;
        }
        return false;
    }

    @Override
    public List<Block> getBlocksAround(){
        if(this.blocksAround == null){
            int minX = NukkitMath.floorDouble(this.boundingBox.minX);
            int minY = NukkitMath.floorDouble(this.boundingBox.minY);
            int minZ = NukkitMath.floorDouble(this.boundingBox.minZ);
            int maxX = NukkitMath.ceilDouble(this.boundingBox.maxX);
            int maxY = NukkitMath.ceilDouble(this.boundingBox.maxY);
            int maxZ = NukkitMath.ceilDouble(this.boundingBox.maxZ);

            this.blocksAround = new ArrayList<>();

            for(int z = minZ; z <= maxZ; ++z){
                for(int x = minX; x <= maxX; ++x){
                    for(int y = minY; y <= maxY; ++y){
                        Block block = this.level.getBlock(this.temporalVector.setComponents(x, y, z).x, this.temporalVector.y, this.temporalVector.z);
                        if(block.hasEntityCollision()){
                            this.blocksAround.add(block);
                        }
                    }
                }
            }
        }

        return this.blocksAround;
    }

    @Override
    public boolean entityBaseTick(int tickDiff){
        this.blocksAround = null;
        this.justCreated = false;

        if(!this.effects.isEmpty()){
            for(Effect effect : this.effects.values()){
                if(effect.canTick()){
                    effect.applyEffect(this);
                }
                effect.setDuration(effect.getDuration() - tickDiff);

                if(effect.getDuration() <= 0){
                    this.removeEffect(effect.getId());
                }
            }
        }

        boolean hasUpdate = false;

        this.checkBlockCollision();

        if(this.isInsideOfSolid()){
            hasUpdate = true;
            this.attack(new EntityDamageEvent(this, EntityDamageEvent.CAUSE_SUFFOCATION, 1));
        }

        if(this.y <= -16 && this.isAlive()){
            hasUpdate = true;
            this.attack(new EntityDamageEvent(this, EntityDamageEvent.CAUSE_VOID, 10));
        }

        if(this.fireTicks > 0){
            if(this.fireProof){
                this.fireTicks -= 4 * tickDiff;
            }else{
                if(!this.hasEffect(Effect.FIRE_RESISTANCE) && (this.fireTicks % 20) == 0 || tickDiff > 20){
                    EntityDamageEvent ev = new EntityDamageEvent(this, EntityDamageEvent.CAUSE_FIRE_TICK, 1);
                    this.attack(ev);
                }
                this.fireTicks -= tickDiff;
            }

            if(this.fireTicks <= 0){
                this.extinguish();
            }else{
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_ONFIRE, true);
                hasUpdate = true;
            }
        }

        if(this.moveTime > 0){
            this.moveTime -= tickDiff;
        }

        if(this.attackTime > 0){
            this.attackTime -= tickDiff;
        }

        if(this.noDamageTicks > 0){
            this.noDamageTicks -= tickDiff;
            if(this.noDamageTicks < 0){
                this.noDamageTicks = 0;
            }
        }

        this.age += tickDiff;
        this.ticksLived += tickDiff;

        return hasUpdate;
    }

    @Override
    public boolean isInsideOfSolid(){
        Block block = this.level.getBlock(this.temporalVector.setComponents(NukkitMath.floorDouble(this.x), NukkitMath.floorDouble(this.y + this.getHeight() - 0.18f), NukkitMath.floorDouble(this.z)).x, this.temporalVector.y, this.temporalVector.z);
        AxisAlignedBB bb = block.getBoundingBox();
        return bb != null && block.isSolid() && !block.isTransparent() && bb.intersectsWith(this.getBoundingBox());
    }

    @Override
    public void attack(EntityDamageEvent source){
        if(
            this.isKnockback()
            && source instanceof EntityDamageByEntityEvent
            && ((EntityDamageByEntityEvent) source).getDamager() instanceof Player
        ){
            return;
        }

        super.attack(source);

        this.target = null;
        this.attackTime = 7;
    }

    @Override
    public boolean setMotion(Vector3 motion){
        if(!this.justCreated){
            EntityMotionEvent ev = new EntityMotionEvent(this, motion);
            this.server.getPluginManager().callEvent(ev);
            if(ev.isCancelled()){
                return false;
            }
        }

        this.motionX = motion.x;
        this.motionY = motion.y;
        this.motionZ = motion.z;
        return true;
    }

    @Override
    public boolean move(double dx, double dy, double dz){
        //Timings.entityMoveTimer.startTiming();

        double movX = dx;
        double movY = dy;
        double movZ = dz;

        AxisAlignedBB[] list = this.level.getCollisionCubes(this, this.level.getTickRate() > 1 ? this.boundingBox.getOffsetBoundingBox(dx, dy, dz) : this.boundingBox.addCoord(dx, dy, dz));
        if(this.isWallCheck()){
            for(AxisAlignedBB bb : list){
                dx = bb.calculateXOffset(this.boundingBox, dx);
            }
            this.boundingBox.offset(dx, 0, 0);

            for(AxisAlignedBB bb : list){
                dz = bb.calculateZOffset(this.boundingBox, dz);
            }
            this.boundingBox.offset(0, 0, dz);
        }
        for(AxisAlignedBB bb : list){
            dy = bb.calculateYOffset(this.boundingBox, dy);
        }
        this.boundingBox.offset(0, dy, 0);

        this.setComponents(this.x + dx, this.y + dy, this.z + dz);
        this.checkChunks();

        this.checkGroundState(movX, movY, movZ, dx, dy, dz);
        this.updateFallState(this.onGround);

        //Timings.entityMoveTimer.stopTiming();
        return true;
    }
    
    @SuppressWarnings("serial")
	public static final HashMap<Integer, Float> armorValues = new HashMap<Integer, Float>(){{
        put(Item.LEATHER_CAP, 1f);
        put(Item.LEATHER_TUNIC, 3f);
        put(Item.LEATHER_PANTS, 2f);
        put(Item.LEATHER_BOOTS, 1f);
        put(Item.CHAIN_HELMET, 1f);
        put(Item.CHAIN_CHESTPLATE, 5f);
        put(Item.CHAIN_LEGGINGS, 4f);
        put(Item.CHAIN_BOOTS, 1f);
        put(Item.GOLD_HELMET, 1f);
        put(Item.GOLD_CHESTPLATE, 5f);
        put(Item.GOLD_LEGGINGS, 3f);
        put(Item.GOLD_BOOTS, 1f);
        put(Item.IRON_HELMET, 2f);
        put(Item.IRON_CHESTPLATE, 6f);
        put(Item.IRON_LEGGINGS, 5f);
        put(Item.IRON_BOOTS, 2f);
        put(Item.DIAMOND_HELMET, 3f);
        put(Item.DIAMOND_CHESTPLATE, 8f);
        put(Item.DIAMOND_LEGGINGS, 6f);
        put(Item.DIAMOND_BOOTS, 3f);
    }};
}
