package tk.daporkchop.command;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;

public class VoteCommand extends VanillaCommand {

	public VoteCommand(String name) {
		super(name, "Sends you the vote link", "/vote");
		this.setPermission("nukkit.command.help"); // everyone should be able to
													// use this
		this.commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(TextFormat.BLUE + "http://www.2p2e.tk/vote.html");
		return true;
	}

}
