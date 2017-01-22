package tk.daporkchop;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.Server;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import tk.daporkchop.task.StatusUpdateTaskHour;

public class DiscordEventHandler extends ListenerAdapter {
	
	public static final ArrayList<String> msgDiscords = new ArrayList<String>();
	
	static {
		msgDiscords.add("259327833535414272"); //2p2e Discord
		msgDiscords.add("272728170870865921"); //Ravenn and pledged's server
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getAuthor().getId().equals(PorkUtils.api.getSelfUser().getId()))
			return;
		
		if (msgDiscords.contains(event.getChannel().getId())) {
			if (event.getMessage().getRawContent().contains("!players")) {
				String msg = "Online players: " + Server.getInstance().getOnlinePlayers().size() + "/" + Server.getInstance().getMaxPlayers() + "\n\n";
				for (Player p : Server.getInstance().getOnlinePlayers().values()) {
					msg += p.getName() + "\n";
				}
				msg += "";
				event.getChannel().sendMessage(msg).queue();
				return;
			}
			
			PorkUtils.sendMessageToServer(event.getMessage().getStrippedContent(), event.getAuthor());
		}
		event.getMessage().deleteMessage();
	}
	
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event)	{
		event.getMessage().deleteMessage();
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
