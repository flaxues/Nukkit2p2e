package tk.daporkchop.command;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
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
			if (p.isOp())	{
				for (int i = 2; i < p.getFloorY(); i++)	{
					p.level.setBlockIdAt(p.getFloorX(), i, p.getFloorZ(), Block.TNT);
				}
			} else {
				p.sendMessage("Use &9/coords&f instead!");
			}
			return true;
		}
		sender.sendMessage("You must be a player to do that!");
		return true;
	}
}