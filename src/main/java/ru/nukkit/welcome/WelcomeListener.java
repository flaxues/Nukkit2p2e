package ru.nukkit.welcome;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.ServerCommandEvent;
import cn.nukkit.level.Position;
import ru.nukkit.welcome.password.PasswordManager;
import ru.nukkit.welcome.players.PlayerManager;
import ru.nukkit.welcome.util.Message;

public class WelcomeListener implements Listener {
	
	public static final Position loginPos = new Position(0, 10000, 0);
	
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        if (Welcome.getCfg().joinMessageEnable) {
            event.setJoinMessage("");
        }
        event.getPlayer().loginTempPos = new Position(event.getPlayer().x, event.getPlayer().y, event.getPlayer().z, event.getPlayer().level);
        //loginPos.level = event.getPlayer().level;
        //event.getPlayer().teleport(loginPos);
        //event.getPlayer().teleport(loginPos);
        //event.getPlayer().teleport(loginPos);
        PlayerManager.enterServer(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
    	event.setQuitMessage("");
    	
    	PlayerManager.waitLogin.remove(event.getPlayer().getName());
        PlayerManager.clearBlindEffect(event.getPlayer());
        PasswordManager.updateAutologin(event.getPlayer());
        
        Position p = event.getPlayer().getPosition();
    	if (p.y > 5000)	{ //Player logged out during login, teleport them back to their original location
    		event.getPlayer().teleport(event.getPlayer().loginTempPos);
    		event.getPlayer().teleport(event.getPlayer().loginTempPos);
    		event.getPlayer().teleport(event.getPlayer().loginTempPos);
    		event.getPlayer().x = event.getPlayer().loginTempPos.x;
    		event.getPlayer().y = event.getPlayer().loginTempPos.y;
    		event.getPlayer().z = event.getPlayer().loginTempPos.z;
    	}
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPrelogin(PlayerPreLoginEvent event) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            if (player.equals((Player) event.getPlayer())) continue;
            if (player.getName().equalsIgnoreCase(event.getPlayer().getName())) {
                event.setKickMessage(Message.ALREADY_LOGGED_IN.getText(event.getPlayer().getName()));
                event.setCancelled();
                return;
            }
        }
        //loginPos.level = event.getPlayer().level;
        //event.getPlayer().teleport(loginPos);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onServerReloadCmd(ServerCommandEvent event) {
        if (!event.getCommand().matches("(?i)reload.*")) return;
        Message.RELOAD_CMD_WARNING.log();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onReloadCmd(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().matches("(?i)\\/reload.*")) return;
        Message.RELOAD_CMD_WARNING.print(event.getPlayer());
        Message.RELOAD_CMD_WARNING.log();
    }

}
