package tk.daporkchop.command;

import java.util.ArrayList;
import java.util.Random;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import mobs.milk.pureentities.util.Utils;
import tk.daporkchop.PorkUtils;

public class VoterewardCommand extends VanillaCommand {

	public Random r = new Random();

	public static final ArrayList<Item> item = new ArrayList<Item>();
	static {
		item.add(Item.get(Item.STRING, 0, 5));
		item.add(Item.get(Item.POTATO, 0, 1));
		item.add(Item.get(Item.CARROT, 0, 1));
		item.add(Item.get(Item.ARROW, 0, 8));
		item.add(Item.get(Item.RAW_BEEF, 0, 2));
		item.add(Item.get(Item.RAW_MUTTON, 0, 2));
		item.add(Item.get(Item.RAW_PORKCHOP, 0, 2));
		item.add(Item.get(Item.RAW_CHICKEN, 0, 2));
		item.add(Item.get(Item.ENDER_PEARL, 0, 2));
		item.add(Item.get(Item.TNT, 0, 16));
		item.add(Item.get(Item.LEATHER, 0, 3));
	}

	public VoterewardCommand(String name) {
		super(name, "gives you vote rewards", "/votereward");
		this.setPermission("nukkit.command.say"); // everyone should be able to
													// use this
		this.commandParameters.clear();
		this.commandParameters.put("default",
                new CommandParameter[]{
                        new CommandParameter("player", CommandParameter.ARG_TYPE_TARGET, false)
                });
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player p = sender.getServer().getPlayer(args[0]);

		if (p == null) {
			Server.getInstance().getLogger().alert("/votereward had no match, aborting");
			return true;
		}
		Item[][] leftOvers = new Item[2][0];
		if (r.nextBoolean()) {
			leftOvers[0] = p.getInventory().addItem(item.get(r.nextInt(item.size())), item.get(r.nextInt(item.size())));
		} else {
			if (r.nextBoolean()) {
				leftOvers[0] = p.getInventory().addItem(item.get(r.nextInt(item.size())), item.get(r.nextInt(item.size())), item.get(r.nextInt(item.size())));
			} else {
				leftOvers[0] = p.getInventory().addItem(item.get(r.nextInt(item.size())), item.get(r.nextInt(item.size())), item.get(r.nextInt(item.size())), item.get(r.nextInt(item.size())));
			}
		}
		if (Utils.rand() && Utils.rand() && Utils.rand() && Utils.rand() && Utils.rand() && Utils.rand() && Utils.rand()) {
			sender.getServer().broadcastMessage("" + TextFormat.BOLD + TextFormat.AQUA + p.getName() + TextFormat.GREEN + "WAS SUPER LUCKY AND GOT AN ELYTRA FROM VOTING!");
			PorkUtils.queueMessageForDiscord("***" + p.getName() + " WAS SUPER LUCKY AND GOT AN ELYTRA FROM VOTING!***");
			leftOvers[1] = p.getInventory().addItem(Item.get(Item.ELYTRA, 0, 1));
		}
		if (leftOvers[0].length > 0) {
			dropLeftOvers(p, leftOvers);
		}
		p.sendMessage("" + TextFormat.GOLD + TextFormat.BOLD + "Your vote items have been added!");

		return true;
	}

	private static void dropLeftOvers(Player p, Item[]... toDrop) {
		for (Item[] ia : toDrop) {
			for (Item i : ia) {
				p.getLevel().dropItem(p.temporalVector, i);
			}
		}
	}
}
