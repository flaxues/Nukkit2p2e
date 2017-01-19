package tk.daporkchop.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TimerTask;

import cn.nukkit.Server;
import tk.daporkchop.WebServer;
import tk.daporkchop.util.DataTag;

public class StatusUpdateTaskHour extends TimerTask {

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			ArrayList<Integer> list = (ArrayList<Integer>) WebServer.commonData.getSerializable("playerCounts", (ArrayList<Short>) WebServer.defaultMonthPlayData.clone());
			if (list.size() == 720) {
				list.remove(0);
			}
			list.add(Server.getInstance().players.size());
			WebServer.commonData.save();

			refreshTopPlayers();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void refreshTopPlayers() {
		try {
			ArrayList<Integer> list = new ArrayList<Integer>();
			HashMap<Integer, String> map = new HashMap<Integer, String>();
			ArrayList<String> topPlayers = (ArrayList<String>) WebServer.commonData.getSerializable("topPlayers");
			for (File f : WebServer.playersFolder.listFiles()) {
				DataTag tag = new DataTag(f);
				int time = tag.getInteger("playTime");
				list.add(time);
				map.put(time, f.getName().substring(0, f.getName().length() - 4));
			}
			Collections.sort(list);
			ArrayList<Integer> top10 = new ArrayList<Integer>(list.subList((list.size() < 10 ? (list.size() - 1) * -1 : list.size() - 10), list.size()));
			topPlayers.clear();
			for (Integer i : top10) {
				topPlayers.add(map.get(i));
			}
			WebServer.commonData.setSerializable("topPlayers", topPlayers);
			WebServer.commonData.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
