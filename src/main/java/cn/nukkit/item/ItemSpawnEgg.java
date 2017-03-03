package cn.nukkit.item;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.inventory.SimpleTransactionGroup;
import cn.nukkit.level.Level;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemSpawnEgg extends Item {

    public ItemSpawnEgg() {
        this(0, 1);
    }

    public ItemSpawnEgg(Integer meta) {
        this(meta, 1);
    }

    public ItemSpawnEgg(Integer meta, int count) {
        super(SPAWN_EGG, meta, count, "Spawn EntityEgg");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block target, int face, double fx, double fy, double fz) {
        for (Map.Entry<Integer, Item> entry : player.getInventory().slots.entrySet())	{
        	if (SimpleTransactionGroup.isBannedItem(entry.getValue().getId(), player.isOp()))	{
        		player.getInventory().setItem(entry.getKey(), new ItemBlock(new BlockAir()));
        	}
        }
        //check for hacked in items
        return false;
    }
}
