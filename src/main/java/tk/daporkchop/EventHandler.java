package tk.daporkchop;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;

public class EventHandler implements Listener {
	
	public Server server;
	
	public EventHandler()	{
		server = Server.getInstance();
	}
	
	@cn.nukkit.event.EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (String s : PorkUtils.welcomeMessage)	{
        	player.sendMessage(s);
        }
    }
}
