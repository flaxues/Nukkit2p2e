package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.item.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class SimpleTransactionGroup implements TransactionGroup {

    private final long creationTime;
    protected boolean hasExecuted = false;

    protected Player source = null;

    protected final Set<Inventory> inventories = new HashSet<>();

    protected final Set<Transaction> transactions = new HashSet<>();

    public SimpleTransactionGroup() {
        this(null);
    }

    public SimpleTransactionGroup(Player source) {
        this.creationTime = System.currentTimeMillis();
        this.source = source;
    }

    public Player getSource() {
        return source;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public Set<Inventory> getInventories() {
        return inventories;
    }

    @Override
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        if (this.transactions.contains(transaction)) {
            return;
        }

        for (Transaction tx : new HashSet<>(this.transactions)) {
            if (tx.getInventory().equals(transaction.getInventory()) && tx.getSlot() == transaction.getSlot()) {
                if (transaction.getCreationTime() >= tx.getCreationTime()) {
                    this.transactions.remove(tx);
                } else {
                    return;
                }
            }
        }

        this.transactions.add(transaction);
        this.inventories.add(transaction.getInventory());
    }

    protected boolean matchItems(List<Item> needItems, List<Item> haveItems) {
        for (Transaction ts : this.transactions) {
        	
            Item checkSourceItem = ts.getInventory().getItem(ts.getSlot());
            Item sourceItem = ts.getSourceItem();
        	
        	if (ts.isEnch())	{
        		break;
        	}
        	if (checkSourceItem.hasEnchantments() || sourceItem.hasEnchantments() || ts.getTargetItem().hasEnchantments())	{
        		return true;
        	}
        	
            if (ts.getTargetItem().getId() != Item.AIR) {
                needItems.add(ts.getTargetItem());
            }
            
            if (!checkSourceItem.deepEquals(sourceItem) || sourceItem.getCount() != checkSourceItem.getCount()) {
                return false;
            }
            
            if (sourceItem.getId() != Item.AIR) {
                haveItems.add(sourceItem);
            }
        }

        Iterator<Item> iter1 = needItems.iterator();
        Iterator<Item> iter2 = haveItems.iterator();
        
        while (iter1.hasNext())	{
        	Item needItem = iter1.next();
        	if (needItem.hasEnchantments())	{
        		iter1.remove();
        		continue;
        	}
        	while (iter2.hasNext())	{
        		Item haveItem = iter2.next();
        		if (haveItem.hasEnchantments())	{
        			iter2.remove();
        			continue;
        		}
        		if (needItem.deepEquals(haveItem)) {
                    int amount = Math.min(haveItem.getCount(), needItem.getCount());
                    needItem.setCount(needItem.getCount() - amount);
                    haveItem.setCount(haveItem.getCount() - amount);
                    if (haveItem.getCount() == 0) {
                        iter2.remove();
                    }
                    if (needItem.getCount() == 0) {
                        iter1.remove();
                        break;
                    }
                }
        	}
        }
        return true;
    }

    @Override
    public boolean canExecute() {
        List<Item> haveItems = new ArrayList<>();
        List<Item> needItems = new ArrayList<>();
        
        return this.matchItems(needItems, haveItems) && haveItems.isEmpty() && needItems.isEmpty() && !this.transactions.isEmpty();
    }

    @Override
    public boolean execute() {
        return execute(false);
    }

    @Override
    public boolean execute(boolean force) {
        if (this.hasExecuted || (!force && !this.canExecute())) {
            return false;
        }

        InventoryTransactionEvent ev = new InventoryTransactionEvent(this);
        Server.getInstance().getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            for (Inventory inventory : this.inventories) {
                if (inventory instanceof PlayerInventory) {
                    ((PlayerInventory) inventory).sendArmorContents(this.getSource());
                }
                inventory.sendContents(this.getSource());
            }
            return false;
        }

        for (Transaction transaction : this.transactions) {
            transaction.getInventory().setItem(transaction.getSlot(), transaction.getTargetItem());
        }

        this.hasExecuted = true;

        return true;
    }

    @Override
    public boolean hasExecuted() {
        return this.hasExecuted;
    }
}
