package cn.nukkit.command.defaults;

import java.util.Timer;

import cn.nukkit.command.CommandSender;
import tk.daporkchop.task.AutoRestartTask;

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
        
        Timer timer = new Timer();
        
        timer.schedule(new AutoRestartTask().setLoopCount(6 * 60 * 60 - 11), 0, 1000);

        return true;
    }
}
