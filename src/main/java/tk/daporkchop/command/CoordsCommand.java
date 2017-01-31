package tk.daporkchop.command;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;

public class CoordsCommand extends VanillaCommand {
	
	public static final ArrayList<Player> onCoords = new ArrayList<Player>();

	public CoordsCommand(String name) {
		super(name, "shows your coords as a tooltip", "/coords");
		this.setPermission("nukkit.command.say"); // everyone should be able to
													// use this
		this.commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player p = sender.getServer().getPlayer(args[0]);

		if (p == null) {
			return true;
		}
		
		if (onCoords.contains(p))	 {
			onCoords.remove(p);
			p.sendMessage("Coords turned off");
			p.sendTip("");
		} else {
			onCoords.add(p);
			p.sendMessage("Coords turned on");
		}
		
		return true;
	}

}
