/*
 * Please keep attribution, but otherwise do whatever you want with this class.
 * 
 * By Andre-John Mas - 2007
 */
package ajmas74.dateslider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * 
 * @author Andre-John Mas
 *
 */
public class SVGDateSlider {

	private static final String TEMPLATE_NAME = "svgtemplate.txt";
	private static final String MONTH_FILL_COLOUR = "rgb(0,200,255)";
	private static final String DEFAULT_FILL_COLOUR = "rgb(255,255,255)";

	private static final String[] CALENDAR_NAMES = new String[] { "Gregorian", "Buddhist", "Chinese", "Coptic", "Ethiopic",
			"Hebrew", "Indian", "Islamic", "Japanese", "Persian", "Roc", "Indian" };

	public static String generateSvgTimeSlider(int pageWidth, int dayWidth, long date) {
		StringBuilder strBuilder = new StringBuilder();

		ULocale locale = ULocale.UK;// new ULocale("en");

		if (locale == null) {
			System.err.println("undefined locale");
			System.exit(1);
		}

		InputStream in = null;
		try {
			in = SVGDateSlider.class.getResourceAsStream(TEMPLATE_NAME);

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuilder.append(line);
				strBuilder.append("\n");
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(-1);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
				}
			}
		}

		String template = strBuilder.toString();

		strBuilder.setLength(0);

		int yBase = 20;
		int sectionSize = 60;

		List<Marker> markers = (new DateSlider()).calculatePositions(dayWidth, pageWidth, date, null, locale);

		for (Marker marker : markers) {
			if (marker.getType() == Marker.CURRENT_DAY) {
				int offset = marker.getOffset();
				strBuilder.append("  <line x1=\"" + offset + "\" y1=\"0\" x2=\"" + offset
						+ "\" y2=\"800\" style=\"stroke:rgb(200,0,0);stroke-width:2\"/>\n");
				break;
			}
		}

		// List<Marker> markers = (new
		// TimeSlider()).calculatePositions(dayWidth, pageWidth, date, null);
		//
		// for ( Marker marker : markers ) {
		// if ( marker.getType() == Marker.CURRENT_DAY ) {
		// int offset = marker.getOffset();
		// strBuilder.append("  <line x1=\""+offset+"\" y1=\"0\" x2=\""+offset+"\" y2=\"800\" style=\"stroke:rgb(200,0,0);stroke-width:2\"/>\n");
		// break;
		// }
		// }

		for (String calendarNames : CALENDAR_NAMES) {
			strBuilder.append(generateSvgTimeSlider(yBase, pageWidth, dayWidth, date, "@calendar=" + calendarNames,
					calendarNames, locale));
			strBuilder.append("\n\n\n");

			yBase += sectionSize;
		}

		String text = strBuilder.toString();

		return template.replace("@SUBST@", text);
	}

	public static String generateSvgTimeSlider(int yBase, int pageWidth, int dayWidth, long date, String calendarType,
			String calendarName, ULocale locale) {
		StringBuilder strBuilder = new StringBuilder();

		List<Marker> markers = (new DateSlider()).calculatePositions(dayWidth, pageWidth, date, calendarType, locale);

		// INFO Calendar name and current date

		String label = calendarName;

		for (int i = 0; i < markers.size(); i++) {
			Marker marker = markers.get(i);
			if (marker.getType() == Marker.CURRENT_DAY) {
				label += " (" + marker.getMonthName() + ")";
				break;
			}
		}

		strBuilder.append("  <!-- - - - - - - - - -->\n");
		strBuilder
				.append("  <text fill=\"#000000\" class=\"title\" font-size=\"12\"  style=\"font-family:  Arial Unicode MS, Verdana, sans-serif; font-weight: bold;\" x=\"40\" y=\""
						+ (yBase) + "\">" + label + "</text>\n");

		// INFO Years

		int prevOffset = -1;
		Marker marker = null;
		for (int i = 0; i < markers.size(); i++) {
			marker = markers.get(i);
			int offset = marker.getOffset();

			if (marker.getType() == Marker.NEW_YEAR) {
				int width = offset - prevOffset;
				strBuilder.append("  <rect x=\"" + prevOffset + "\" y=\"" + (yBase + 10) + "\" width=\"" + width
						+ "\" height=\"20\" style=\"fill:" + MONTH_FILL_COLOUR + ";stroke-width:1; stroke:rgb(0,0,0)\"/>");

				if (width > 200) {
					strBuilder.append("  <text fill=\"#000000\" font-size=\"12\" font-family=\"Verdana\" x=\""
							+ (prevOffset + 10) + "\" y=\"" + (yBase + 25) + "\">" + (marker.getYear() - 1) + "</text>\n\n");
				}

				prevOffset = offset;
			}
		}

		if (prevOffset < pageWidth) {
			int width = pageWidth - prevOffset;
			strBuilder.append("  <rect x=\"" + prevOffset + "\" y=\"" + (yBase + 10) + "\" width=\"" + width
					+ "\" height=\"20\" style=\"fill:" + MONTH_FILL_COLOUR + ";stroke-width:1; stroke:rgb(0,0,0)\"/>");
			if (width > 100) {
				strBuilder.append("  <text fill=\"#000000\" font-size=\"12\" font-family=\"Verdana\" x=\"" + (prevOffset + 10)
						+ "\" y=\"" + (yBase + 25) + "\">" + marker.getYear() + "</text>\n\n");
			}
		}

		// INFO Months

		yBase += 20;

		prevOffset = -1;
		marker = null;
		for (int i = 0; i < markers.size(); i++) {
			marker = markers.get(i);
			int offset = marker.getOffset();

			if (marker.getType() == Marker.NEW_MONTH) {
				int width = offset - prevOffset;
				strBuilder.append("  <rect x=\"" + prevOffset + "\" y=\"" + (yBase + 10) + "\" width=\"" + width
						+ "\" height=\"10\" style=\"fill:" + DEFAULT_FILL_COLOUR + ";stroke-width:1; stroke:rgb(0,0,0)\">");
				strBuilder.append("  <title>");
				strBuilder.append(marker.getMonthName());
				strBuilder.append("</title>");
				strBuilder.append("  </rect>");

				if (width > 200) {
					strBuilder.append("  <text fill=\"#000000\" font-size=\"12\" font-family=\"Verdana\" x=\""
							+ (prevOffset + 10) + "\" y=\"" + (yBase + 25) + "\">" + (marker.getYear() - 1) + "</text>\n\n");
				}

				prevOffset = offset;
			}

		}

		if (prevOffset < pageWidth) {
			int width = pageWidth - prevOffset;
			strBuilder.append("  <rect x=\"" + prevOffset + "\" y=\"" + (yBase + 10) + "\" width=\"" + width
					+ "\" height=\"10\" style=\"fill:" + DEFAULT_FILL_COLOUR + ";stroke-width:1; stroke:rgb(0,0,0)\">");

			strBuilder.append("  </rect>");
		}

		// INFO Current day marker
		
		prevOffset = -1;
		marker = null;
		for (int i = 0; i < markers.size(); i++) {
			marker = markers.get(i);
			if (marker.getType() == Marker.CURRENT_DAY) {
				int offsetx = marker.getOffset();
				strBuilder.append("  <line opacity=\"0.8\" x1=\"" + offsetx + "\" y1=\"" + (yBase - 10) + "\" x2=\"" + offsetx
						+ "\" y2=\"" + ((yBase - 10) + 30)
						+ "\" style=\"stroke:rgb(200,0,0);stroke-width:2\" tooltip=\"enable\">\n");

				Calendar cal = Calendar.getInstance(new ULocale(calendarType));
				DateFormat dateFormat = cal.getDateTimeFormat(DateFormat.LONG, DateFormat.NONE, locale);

				strBuilder.append("     <title>" + dateFormat.format(cal) + "</title>");
				strBuilder.append("  </line>\n");
				break;
			}
		}

		return strBuilder.toString();
	}

	/**
	 * Usage: java ajmas74.dateslider.SVGDateSlider output_path
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -35);

		if (args.length == 1) {
			FileOutputStream fout = null;
			BufferedWriter writer = null;
			try {
				fout = new FileOutputStream(args[0]);
				writer = new BufferedWriter(new OutputStreamWriter(fout, "UTF-8"));
				writer.append(generateSvgTimeSlider(800, 1, cal.getTimeInMillis()));
				writer.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (fout != null) {
					fout.close();
				}
				if (writer != null) {
					fout.close();
				}
			}
		} else {
			System.out.println(generateSvgTimeSlider(800, 1, cal.getTimeInMillis()));
		}
	}

}
