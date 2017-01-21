package tk.daporkchop.command;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import tk.daporkchop.PorkUtils;

public class AnnounceCommand extends VanillaCommand {

	public AnnounceCommand(String name) {
		super(name, "Broadcasts a message", "/say");
		this.setPermission("nukkit.command.say"); // everyone should be able to
													// use this
		this.commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }

        String msg = "";
        for (String arg : args) {
            msg += arg + " ";
        }
        if (msg.length() > 0) {
            msg = msg.substring(0, msg.length() - 1);
        }


        sender.getServer().broadcastMessage(TextFormat.AQUA + msg);
        PorkUtils.queueMessageForDiscord("***" + msg + "***");
        return true;
	}

}
