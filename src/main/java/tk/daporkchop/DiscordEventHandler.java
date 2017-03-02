package tk.daporkchop;

import cn.nukkit.Player;
import cn.nukkit.Server;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import tk.daporkchop.task.StatusUpdateTaskHour;

public class DiscordEventHandler extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getAuthor().getId().equals(PorkUtils.api.getSelfUser().getId()))	{
			return;
		}

		if (event.getMessage().getRawContent().contains("!players")) {
			String msg = "Online players: " + Server.getInstance().getOnlinePlayers().size() + "/" + Server.getInstance().getMaxPlayers() + "\n\n";
			for (Player p : Server.getInstance().getOnlinePlayers().values()) {
				msg += p.getName() + "\n";
			}
			msg += "";
			event.getChannel().sendMessage(msg).queue();
			return;
		}
		
		if (event.getMessage().getStrippedContent().length() < 150) {
			//prevent super long messages from spamming PE players
			PorkUtils.sendMessageToServer(event.getMessage().getStrippedContent(), event.getAuthor());
			return;
		} else {
			try {
				event.getAuthor().getPrivateChannel().sendMessage("You tred to send a message in #minecraft-chat that was too long! The max message size is 150 characters!").queue();
			} catch (IllegalStateException e)	{
				event.getAuthor().openPrivateChannel();
				event.getAuthor().getPrivateChannel().sendMessage("You tred to send a message in #minecraft-chat that was too long! The max message size is 150 characters!").queue();
			}
			event.getMessage().deleteMessage().queue();
		}
	}
	
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event)	{
		if (event.getAuthor().getId().equals("226975061880471552"))	{
			if (event.getMessage().getRawContent().startsWith("$"))	{
				if (event.getMessage().getRawContent().equals("$regenTopPlayers"))	{
					StatusUpdateTaskHour.refreshTopPlayers();
				}
			} else if (event.getMessage().getRawContent().startsWith("/"))	{
				Server.getInstance().dispatchCommand(new DiscordCommandSender(event.getAuthor()), event.getMessage().getRawContent().substring(1));
			} else if (!event.getMessage().getRawContent().startsWith(" ")) {
				Server.getInstance().dispatchCommand(new DiscordCommandSender(event.getAuthor()), "say " + event.getMessage().getRawContent());
			}
		}
	}
}
