package tk.daporkchop.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;

public class GetPosCommand extends VanillaCommand {

	public GetPosCommand(String name) {
		super(name, "Shows you your coordinates", "/getpos");
		this.setPermission("nukkit.command.help"); // everyone should be able to
													// use this
		this.commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Position pos = p.getPosition();
			p.sendMessage(TextFormat.AQUA + "x: " + Math.floor(pos.x) + " y: " + Math.floor(pos.y) + " z: " + Math.floor(pos.z));
			return true;
		}
		sender.sendMessage("You must be a player to do that!");
		return true;
	}
}