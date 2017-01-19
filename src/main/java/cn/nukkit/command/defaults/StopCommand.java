package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class StopCommand extends VanillaCommand {

    public StopCommand(String name) {
        super(name, "%nukkit.command.stop.description", "%commands.stop.usage");
        this.setPermission("nukkit.command.stop");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        sender.getServer().broadcastMessage(new TranslationContainer("commands.stop.start"));

        for (Level lvl : sender.getServer().getLevels().values())	{
        	sender.getServer().broadcastMessage(TextFormat.colorize("" + TextFormat.ITALIC + TextFormat.GRAY + "[Saving level: " + lvl.getName() + "]"));
        	lvl.save();
        	sender.getServer().broadcastMessage(TextFormat.colorize("" + TextFormat.ITALIC + TextFormat.GRAY + "[Finished saving level: " + lvl.getName() + "]"));
        }
        
        sender.getServer().broadcastMessage(TextFormat.colorize("" + TextFormat.ITALIC + TextFormat.GRAY + "[Saving players]"));
        for (Player p : sender.getServer().getOnlinePlayers().values())	{
        	p.save();
        }
        sender.getServer().broadcastMessage(TextFormat.colorize("" + TextFormat.ITALIC + TextFormat.GRAY + "[Done saving players]"));
        
        sender.getServer().shutdown();

        return true;
    }
}
