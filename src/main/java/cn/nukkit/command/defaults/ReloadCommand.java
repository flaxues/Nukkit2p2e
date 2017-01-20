package cn.nukkit.command.defaults;

import cn.nukkit.command.CommandSender;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ReloadCommand extends VanillaCommand {

    public ReloadCommand(String name) {
        super(name, "%nukkit.command.reload.description", "%commands.reload.usage");
        this.setPermission("nukkit.command.reload");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        sender.getServer().reload();

        return true;
    }
}
