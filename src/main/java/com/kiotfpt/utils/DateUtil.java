package com.kiotfpt.utils;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;

import com.kiotfpt.request.DateRequest;

public class DateUtil {
	public static Date calculateStartDate(DateRequest filterRequest) {
		Calendar calendar = Calendar.getInstance();
		int inputType = getInputType(filterRequest);

		switch (inputType) {
		case 1: // Only day
			calendar.set(Year.MIN_VALUE, Calendar.JANUARY, filterRequest.getDay(), 0, 0, 0);
			break;

		case 2: // Only month
			calendar.set(Year.MIN_VALUE, filterRequest.getMonth() - 1, 1, 0, 0, 0);
			break;

		case 3: // Day and month
			calendar.set(Year.MIN_VALUE, filterRequest.getMonth() - 1, filterRequest.getDay(), 0, 0, 0);
			break;

		case 4: // Only year
			calendar.set(filterRequest.getYear(), Calendar.JANUARY, 1, 0, 0, 0);
			break;

		case 5: // Day and year
			calendar.set(filterRequest.getYear(), Calendar.JANUARY, filterRequest.getDay(), 0, 0, 0);
			break;

		case 6: // Month and year
			calendar.set(filterRequest.getYear(), filterRequest.getMonth() - 1, 1, 0, 0, 0);
			break;

		case 7: // Day, month, and year
			calendar.set(filterRequest.getYear(), filterRequest.getMonth() - 1, filterRequest.getDay(), 0, 0, 0);
			break;

		default:
			// Handle invalid input
			throw new IllegalArgumentException("Invalid filter request");
		}

		return calendar.getTime();
	}

	public static Date calculateEndDate(DateRequest filterRequest) {
		Calendar calendar = Calendar.getInstance();
		int inputType = getInputType(filterRequest);

		switch (inputType) {
		case 1: // Only day
			calendar.set(Year.now().getValue(), Calendar.DECEMBER, filterRequest.getDay(), 23, 59, 59);
			break;

		case 2: // Only month
			calendar.set(Year.now().getValue(), filterRequest.getMonth() - 1,
					calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			break;

		case 3: // Day and month
			calendar.set(Year.now().getValue(), filterRequest.getMonth() - 1, filterRequest.getDay(), 23, 59, 59);
			break;

		case 4: // Only year
			calendar.set(filterRequest.getYear(), Calendar.DECEMBER, calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
					23, 59, 59);
			break;

		case 5: // Day and year
			calendar.set(filterRequest.getYear(), Calendar.DECEMBER, filterRequest.getDay(), 23, 59, 59);
			break;

		case 6: // Month and year
			calendar.set(filterRequest.getYear(), filterRequest.getMonth() - 1,
					calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			break;

		case 7: // Day, month, and year
			calendar.set(filterRequest.getYear(), filterRequest.getMonth() - 1, filterRequest.getDay(), 23, 59, 59);
			break;

		default:
			// Handle invalid input
			throw new IllegalArgumentException("Invalid filter request");
		}

		return calendar.getTime();
	}

	private static int getInputType(DateRequest filterRequest) {
		int inputType = 0;
		if (filterRequest.getDay() != null && filterRequest.getDay() > 0) {
			inputType += 1;
		}
		if (filterRequest.getMonth() != null && filterRequest.getMonth() > 0) {
			inputType += 2;
		}
		if (filterRequest.getYear() != null && filterRequest.getYear() >= 0) {
			inputType += 4;
		}
		return inputType;
	}
}
