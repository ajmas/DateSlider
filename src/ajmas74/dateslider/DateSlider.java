/*
 * Please keep attribution, but otherwise do whatever you want with this class.
 * 
 * By Andre-John Mas - November 2007
 */
package ajmas74.dateslider;

import java.util.ArrayList;
import java.util.List;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.DateFormatSymbols;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * 
 * This class is used to create a list of 'markers' indicating month start, year
 * start and current date. The results are then available for a class wishing to
 * render a linear calendar.
 * 
 * @author Andre-John Mas
 * 
 */
public class DateSlider {

	/**
	 * This works out where the months and years starts, along with the current
	 * day. These are then provided as a set of markers that can be used by a
	 * renderer.
	 * 
	 * @param dayWidth
	 *            the day width in pixels
	 * @param containerWidth
	 *            the width of the container
	 * @param baseDate
	 *            the date from which the displayed calendar starts, in
	 *            milliseconds since 1970
	 * @param calendarType
	 * @param ULocale
	 *            locale
	 * @return
	 */
	public List<Marker> calculatePositions(float dayWidth, float containerWidth, long baseDate, String calendarType,
			ULocale locale) {

		List<Marker> markers = new ArrayList<Marker>();

		int divisions = (int) (containerWidth / dayWidth);

		Calendar calendar = null;
		Calendar currentDayCalendar = null;
		if (calendarType != null) {
			calendar = Calendar.getInstance(new ULocale(calendarType));
			currentDayCalendar = Calendar.getInstance(new ULocale(calendarType));

		} else {
			calendar = Calendar.getInstance();
			currentDayCalendar = Calendar.getInstance();
		}

		calendar.setTimeInMillis(baseDate);
		currentDayCalendar.setTimeInMillis(System.currentTimeMillis());

		calendar.add(Calendar.DATE, -1);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		for (int i = 0; i < divisions; i++) {

			calendar.add(Calendar.DATE, 1);

			int year2 = calendar.get(Calendar.YEAR);
			int month2 = calendar.get(Calendar.MONTH);
			int day2 = calendar.get(Calendar.DAY_OF_MONTH);

			int offset = (int) (dayWidth * i);

			// info: see if this is a new year
			if (year2 != year) {
				markers.add(new Marker(offset, Marker.NEW_YEAR, year2, month2, ""));
			}

			// info: see if this is a new month
			if (month2 != month || day2 < day) {

				DateFormat dateFormat = calendar.getDateTimeFormat(DateFormat.MEDIUM, DateFormat.NONE, locale);

				String monthName = "";
				if (dateFormat instanceof SimpleDateFormat) {

					DateFormatSymbols symbols = ((SimpleDateFormat) dateFormat).getDateFormatSymbols();
					monthName = symbols.getMonths()[month];
				}

				markers.add(new Marker(offset, Marker.NEW_MONTH, year2, month2, monthName));// +
																							// );
			}

			if (calendar.get(Calendar.YEAR) == currentDayCalendar.get(Calendar.YEAR)
					&& calendar.get(Calendar.MONTH) == currentDayCalendar.get(Calendar.MONTH)
					&& calendar.get(Calendar.DAY_OF_MONTH) == currentDayCalendar.get(Calendar.DAY_OF_MONTH)) {
				// TODO use the locale from the UI
				DateFormat dateFormat = calendar.getDateTimeFormat(DateFormat.LONG, DateFormat.NONE, locale);

				markers.add(new Marker(offset, Marker.CURRENT_DAY, year2, month2, dateFormat.format(currentDayCalendar)));
			}

			year = year2;
			month = month2;
			day = day2;
		}

		return markers;
	}

}
