package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;

/**
 * Created on 2015/11/22 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockPodzol extends BlockDirt {

    public BlockPodzol() {
        this(0);
    }

    public BlockPodzol(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return PODZOL;
    }

    @Override
    public String getName() {
        return "Podzol";
    }
    
    @Override
    public int[][] getDrops(Item item)	{
    	if (item.getEnchantment(Enchantment.ID_SILK_TOUCH) != null)	{
    		return new int[][] {
    			{
    				this.getId(),
    				0,
    				1
    			}
    		};
    	}
    	return super.getDrops(item);
    }
    
    @Override
    public int onUpdate(int type) {
        //we don't want grass to do any updating, only dirt
        return 0;
    }
}
