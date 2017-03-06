package tk.daporkchop;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;
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
			EmbedBuilder builder = new EmbedBuilder();

			builder.addField("Online players:", Server.getInstance().getOnlinePlayers().size() + "/" + Server.getInstance().getMaxPlayers(), false);

            String players = "";

            ArrayList<Player> arr = new ArrayList<>(Server.getInstance().getOnlinePlayers().values());
            for (int i = 0; i < arr.size(); i++)    {
                players += i + 1 == arr.size() ? arr.get(i).getName() : arr.get(i).getName() + ", ";
            }
            builder.addField("", players, false);
            builder.setColor(Color.RED);

			event.getChannel().sendMessage(builder.build()).queue();
			return;
		}
		
		if (event.getChannel().getId().equals("259327833535414272"))	{
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
