package tk.daporkchop;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ListIterator;
import java.util.Timer;
import java.util.concurrent.Executors;

import fi.iki.elonen.NanoHTTPD;
import tk.daporkchop.task.StatusUpdateTask;
import tk.daporkchop.task.StatusUpdateTaskDay;
import tk.daporkchop.task.StatusUpdateTaskHour;
import tk.daporkchop.util.DataTag;
import tk.daporkchop.util.ImageProvider;

public class WebServer extends NanoHTTPD {

	public static ArrayList<String> topPlayers;

	public static DataTag commonData;
	public static final ArrayList<Short> defaultMonthPlayData = new ArrayList<Short>();
	public static final File playersFolder = new File(DataTag.USER_FOLDER + "/tracker/players/");

	static {
		playersFolder.mkdirs();
		for (int i = 0; i < 30; i++) {
			defaultMonthPlayData.add((short) 0);
		}
	}

	@SuppressWarnings("unchecked")
	public WebServer(Timer timer) throws IOException {
		super(8123);
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		commonData = new DataTag(new File(DataTag.USER_FOLDER + "/tracker/data.dat"));
		if (commonData.getSerializable("playerCounts") == null) {
			ArrayList<Integer> toAdd = new ArrayList<Integer>();
			for (int i = 0; i < 720; i++) {
				toAdd.add(0);
			}
			commonData.setSerializable("playerCounts", toAdd);
			commonData.save();
		}
		ArrayList<String> def = new ArrayList<String>();
		def.add("DaPorkchop_");
		def.add("DaPorkchop_");
		def.add("DaPorkchop_");
		def.add("DaPorkchop_");
		def.add("DaPorkchop_");
		def.add("DaPorkchop_");
		def.add("DaPorkchop_");
		def.add("DaPorkchop_");
		def.add("DaPorkchop_");
		def.add("DaPorkchop_");
		topPlayers = (ArrayList<String>) commonData.getSerializable("topPlayers", def);
		commonData.setSerializable("topPlayers", topPlayers);
		StatusUpdateTaskHour.refreshTopPlayers();
		Calendar calendar = Calendar.getInstance();

		Executors.newScheduledThreadPool(1);
		timer.schedule(new StatusUpdateTaskHour(), millisToNextHour(calendar), 60 * 60 * 1000);

		Long midnight = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MILLIS);
		timer.schedule(new StatusUpdateTaskDay(), midnight, 1440 * 60 * 60 * 1000);

		timer.schedule(new StatusUpdateTask(), 0, 60 * 1000);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response serve(IHTTPSession session) {
		switch (session.getUri()) {
		case "/":
		case "/index.html":
		case "/index.html/":
			String msg = "";
			return newFixedLengthResponse(msg);
		default:
			String key = session.getUri();
			if (key.startsWith("/player_")) {
				if (key.startsWith("/player_playtime_")) {
					DataTag tag = new DataTag(new File(DataTag.USER_FOLDER + "/tracker/players/" + key.substring(17) + ".dat"));
					return newFixedLengthResponse(String.valueOf(tag.getInteger("playTime", 0)));
				} else if (key.startsWith("/player_graph_")) {
					DataTag tag = new DataTag(new File(DataTag.USER_FOLDER + "/tracker/players/" + key.substring(14) + ".dat"));
					return newFixedLengthResponse("<html><body><img src=\"data:image/png;base64," + ImageProvider.getPlayerMonthChart(key.substring(14), (ArrayList<Short>) tag.getSerializable("lastMonthPlayTime", defaultMonthPlayData)) + "\"></body></html>");
				}
			} else if (key.startsWith("/server_")) {
				if (key.startsWith("/server_playerchart")) {
					return newFixedLengthResponse("<html><body><img src=\"data:image/png;base64," + ImageProvider.getPlayerCountGraph((ArrayList<Integer>) commonData.getSerializable("playerCounts")) + "\"></body></html>");
				} else if (key.startsWith("/server_topplayers")) {
					String result = "<html><head><link type=\"text/css\" rel=\"stylesheet\" href=\"https://2pocket2edition.github.io/tracker-stylesheet.css\"/></head><body><table cellspacing=\"0\" cellpadding=\"0\"><tr><td><strong>Name</strong></td><td><strong>Play time (minutes)</strong></td></tr>";
					ArrayList<String> list = (ArrayList<String>) commonData.getSerializable("topPlayers");
					ListIterator<String> li = list.listIterator(list.size());

					while (li.hasPrevious()) {
						String name = li.previous();
						result += ("<tr><td>" + name + "</td><td>" + new DataTag(new File(playersFolder + "/" + name + ".dat")).getInteger("playTime") + "</td></tr>");
					}
					result += "</table></body></html>";
					return newFixedLengthResponse(result);
				}
			}
		}
		return null;
	}

	public static long millisToNextHour(Calendar calendar) {
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		int millis = calendar.get(Calendar.MILLISECOND);
		int minutesToNextHour = 60 - minutes;
		int secondsToNextHour = 60 - seconds;
		int millisToNextHour = 1000 - millis;
		return minutesToNextHour * 60 * 1000 + secondsToNextHour * 1000 + millisToNextHour;
	}
}
