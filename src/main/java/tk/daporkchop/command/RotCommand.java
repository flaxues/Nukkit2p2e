package tk.daporkchop.command;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;

public class RotCommand extends VanillaCommand {

	public static final ArrayList<Player> onRotation = new ArrayList<Player>();

	public RotCommand(String name) {
		super(name, "shows your rotation as a tooltip", "/rot");
		this.setPermission("nukkit.command.say"); // everyone should be able to
													// use this
		this.commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player p;
		if (sender instanceof Player)	{
			p = (Player) sender;
		} else {
			sender.sendMessage("You need to be a player to do that!");
			return true;
		}
		
		if (onRotation.contains(p))	 {
			onRotation.remove(p);
			p.sendMessage("Rotation turned off");
			p.sendTip("");
		} else {
			onRotation.add(p);
			p.sendMessage("Rotation turned on");
		}
		
		return true;
	}

}
