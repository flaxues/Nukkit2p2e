package tk.daporkchop.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class ImageProvider {
	public static String getPlayerMonthChart(String name, ArrayList<Short> list)	{
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		for (int i = 0; i < list.size(); i++)	{
			short s = list.get(i);
			data.addValue(s, "Play time", String.valueOf(i));
		}
		JFreeChart lineChartObject = ChartFactory.createBarChart(name + "'s play time for the last month", "Day (higher values mean more recent)", "Play time (minutes)", data, PlotOrientation.VERTICAL, false, true, false);
		int width = 1200;
	    int height = 480;
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    try {
			ChartUtilities.writeChartAsPNG(stream, lineChartObject, width, height);
		} catch (IOException e) {}
	    return Base64.encodeBase64String(stream.toByteArray());
	}
	
	public static String getPlayerCountGraph(ArrayList<Integer> list)	{
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		for (int i = 0; i < list.size(); i++)	{
			Integer s = list.get(i);
			data.addValue(s, "Players", String.valueOf(i));
		}
		JFreeChart lineChartObject = ChartFactory.createLineChart("Player count over the last week", "Day (higher values mean more recent)", "Players", data, PlotOrientation.VERTICAL, false, true, false);
		int width = 1200;
	    int height = 480;
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    try {
			ChartUtilities.writeChartAsPNG(stream, lineChartObject, width, height);
		} catch (IOException e) {}
	    return Base64.encodeBase64String(stream.toByteArray());
	}
}
