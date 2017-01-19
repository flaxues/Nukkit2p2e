package tk.daporkchop.task;

import java.io.File;
import java.util.ArrayList;
import java.util.TimerTask;

import cn.nukkit.Player;
import cn.nukkit.Server;
import tk.daporkchop.WebServer;
import tk.daporkchop.util.DataTag;

public class StatusUpdateTask extends TimerTask {

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			for (Player p : Server.getInstance().players.values()) {
				File f = new File(DataTag.USER_FOLDER + "/tracker/players/" + p.getName() + ".dat");
				DataTag tag;
				if (!f.exists()) {
					tag = new DataTag(f);
					tag.setLong("firstJoinDate", System.currentTimeMillis());
					tag.setSerializable("lastMonthPlayTime", (ArrayList<Short>) WebServer.defaultMonthPlayData.clone());
				} else {
					tag = new DataTag(f);
				}
				tag.setInteger("playTime", tag.getInteger("playTime", 0) + 1);
				ArrayList<Short> list = (ArrayList<Short>) tag.getSerializable("lastMonthPlayTime", (ArrayList<Short>) WebServer.defaultMonthPlayData.clone());
				list.set(list.size() - 1, (short) (list.get(list.size() - 1) + 1));
				tag.setSerializable("lastMonthPlayTime", list);
				tag.save();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
