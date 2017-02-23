package tk.daporkchop;

import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.lang.TextContainer;
import net.dv8tion.jda.core.entities.User;

public class DiscordCommandSender extends ConsoleCommandSender {

	public User user;

	public DiscordCommandSender(User u) {
		user = u;
	}

	@Override
	public void sendMessage(String message) {
		message = this.getServer().getLanguage().translateString(message);
		try {
			user.getPrivateChannel().sendMessage(TextFormat.clean(message.trim())).queue();
		} catch (UnsupportedOperationException e) {
		}
	}

	@Override
	public void sendMessage(TextContainer message) {
		this.sendMessage(this.getServer().getLanguage().translate(message));
	}
}
