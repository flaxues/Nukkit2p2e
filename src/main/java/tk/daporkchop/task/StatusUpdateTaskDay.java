package tk.daporkchop.task;

import java.io.File;
import java.util.ArrayList;
import java.util.TimerTask;

import tk.daporkchop.WebServer;
import tk.daporkchop.util.DataTag;

public class StatusUpdateTaskDay extends TimerTask {
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			final File saveDir = new File(DataTag.USER_FOLDER + "/tracker/players");
			ArrayList<File> toDelete = new ArrayList<File>();
			for (final File f : saveDir.listFiles()) {
				DataTag tag = new DataTag(f);
				ArrayList<Short> list = (ArrayList<Short>) tag.getSerializable("lastMonthPlayTime", (ArrayList<Short>) WebServer.defaultMonthPlayData.clone());
				boolean isEmpty = true;
				for (short s : list) {
					if (s != 0) {
						isEmpty = false;
					}
				}
				if (isEmpty) {
					toDelete.add(f);
					continue;
				}
				list.remove(0);
				list.add((short) 0);
				tag.setSerializable("lastMonthPlayTime", list);
				tag.save();
			}
			for (File f : toDelete) {
				f.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
