package tk.daporkchop.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;

public class MuteCommand extends VanillaCommand {

	public MuteCommand(String name) {
		super(name, "toggles your chat", "/mute");
		this.setPermission("nukkit.command.help"); // everyone should be able to
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
		
		p.sendMessage("&l&aChat has been" + ((p.isChatMuted = !p.isChatMuted) ? "dis" : "en") + "abled!", true);
		
		return true;
	}

}
