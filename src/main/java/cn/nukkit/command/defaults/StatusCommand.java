package cn.nukkit.command.defaults;

import java.util.Objects;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.utils.TextFormat;

/**
 * Created on 2015/11/11 by xtypr. Package cn.nukkit.command.defaults in project Nukkit .
 */
public class StatusCommand extends VanillaCommand {

	public StatusCommand(String name) {
		super(name, "%nukkit.command.status.description", "%nukkit.command.status.usage");
		this.setPermission("nukkit.command.status");
		this.commandParameters.clear();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!this.testPermission(sender)) {
			return true;
		}

		Server server = sender.getServer();

		long time = (System.currentTimeMillis() - Nukkit.START_TIME) / 1000;
		int seconds = NukkitMath.floorDouble(time % 60);
		int minutes = NukkitMath.floorDouble((time % 3600) / 60);
		int hours = NukkitMath.floorDouble(time % (3600 * 24) / 3600);
		int days = NukkitMath.floorDouble(time / (3600 * 24));
		String upTimeString = days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds";

		String message = "---- Server status ----\nUptime: " + upTimeString + "\n";
		
		float tps = server.getTicksPerSecond();

		message += "Current TPS: " + NukkitMath.round(tps, 2) + "\n";

		message += "Load: " + server.getTickUsage() + "%" + "\n";

		message += "Network upload: " + TextFormat.GREEN + NukkitMath.round((server.getNetwork().getUpload() / 1024 * 1000), 2) + " kB/s" + "\n";

		message += "Network download: " + TextFormat.GREEN + NukkitMath.round((server.getNetwork().getDownload() / 1024 * 1000), 2) + " kB/s" + "\n";

		message += "Thread count: " + TextFormat.GREEN + Thread.getAllStackTraces().size() + "\n";

		Runtime runtime = Runtime.getRuntime();
		double totalMB = NukkitMath.round(((double) runtime.totalMemory()) / 1024 / 1024, 2);
		double usedMB = NukkitMath.round((double) (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024, 2);
		double maxMB = NukkitMath.round(((double) runtime.maxMemory()) / 1024 / 1024, 2);
		double usage = usedMB / maxMB * 100;

		message += "Used memory: " + usedMB + " MB. (" + NukkitMath.round(usage, 2) + "%)" + "\n";

		message += "Total memory: " + totalMB + " MB." + "\n";

		message += "Maximum VM memory: " + maxMB + " MB." + "\n";

		message += "Available processors: " + runtime.availableProcessors() + "\n";

		message += "Players: " + server.getOnlinePlayers().size() + " online, " + server.getMaxPlayers() + " max. " + "\n";

		for (Level level : server.getLevels().values()) {
			message += "World \"" + level.getFolderName() + "\"" + (!Objects.equals(level.getFolderName(), level.getName()) ? " (" + level.getName() + ")" : "") + ": " + level.getChunks().size() + " chunks, " + level.getEntities().length + " entities, " + level.getBlockEntities().size() + " blockEntities." + " Time " + NukkitMath.round(level.getTickRateTime(), 2) + "ms" + (level.getTickRate() > 1 ? " (tick rate " + level.getTickRate() + ")" : "") + "\n";
		}
		
		sender.sendMessage(message);

		return true;
	}
}
