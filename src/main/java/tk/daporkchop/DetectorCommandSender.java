package tk.daporkchop;

import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.lang.TextContainer;
import tk.daporkchop.task.DetectFrozenTask;

public class DetectorCommandSender extends ConsoleCommandSender {
	
	public DetectFrozenTask task;
	
	public DetectorCommandSender(DetectFrozenTask task)	{
		this.task = task;
	}
	
	@Override
	public void sendMessage(String message) {
		task.returnedOutput = true;
	}

	@Override
	public void sendMessage(TextContainer message) {
		this.sendMessage("");
	}
}
