package Ai.Attri.Utils;

import java.time.Month;
import java.util.Locale;
import java.util.TimeZone;

import Ai.Attri.Library.Constants;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DateOperations {
	
	Constants constants = new Constants();
	public dateFormat LSMV_dateFormat = dateFormat.ddMMMyyyy;
	public String timeZone = "";
	
	public enum dateFormat {

		yyyyMMdd("yyyy-MM-dd"), MMddyyyy("MM-dd-yyyy"), ddMMyyyy("dd-MM-yyyy"), ddMMyyyyhhmmss(
				"dd-MM-yyyy hh:mm:ss"), ddMMMyyyy(
						"dd-MMM-yyyy"), ddMMyyyyhhmmaa("dd-MM-yyyy hh:mm aa"), ddMMMyyyyhhmmaa("dd-MMM-yyyy hh:mm aa"),ddMMMMyyyy(
								"dd-MMMM-yyyy");

		private String value;

		dateFormat(String evalue) {
			this.value = evalue;
		}

		public String getDateFormat() {
			return value;
		}
	}

	/**********************************************************************************************************
	 * @Objective: To get current date
	 * @InputParameters:date format as enum dateFormat
	 * @OutputParameters:current date as String
	 * @author:Adarsh
	 * @Date : 08-30-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/

	public String getCurrentDate(dateFormat format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format.getDateFormat());
			Calendar c1 = Calendar.getInstance();
			return sdf.format(c1.getTime());
		} catch (Exception e) {
			throw e;
		}
	}

	/**********************************************************************************************************
	 * @Objective: To get date as specified by Input data
	 * @InputParameters:Input data in format(d:m:y)----To increment day(d+1:m:y)
	 * @OutputParameters:specified date as String
	 * @author:Adarsh
	 * @Date : 08-30-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/

	public String getDateByInputData(dateFormat format, String data,String timezone) {
		try {
			if (!data.trim().equalsIgnoreCase("#skip#")) {
				SimpleDateFormat sdf = new SimpleDateFormat(format.getDateFormat());
				Calendar c1 = Calendar.getInstance();
				if(!timezone.equalsIgnoreCase("")) {
					c1.setTimeZone(TimeZone.getTimeZone(timezone));
					System.out.println("Japan Time"+c1.get(Calendar.HOUR_OF_DAY)+c1.get(Calendar.MINUTE));
				}
				String[] data1 = data.split(":");

				if (data1[0].indexOf("+") > 0) {
					String[] day = data1[0].split("\\+");
					c1.add(Calendar.DAY_OF_MONTH, Integer.valueOf(day[1]));
				} else if (data1[0].indexOf("-") > 0) {
					String[] day = data1[0].split("\\-");
					c1.add(Calendar.DAY_OF_MONTH, -+Integer.valueOf(day[1]));
				}

				if (data1[1].indexOf("+") > 0) {
					String[] month = data1[1].split("\\+");
					c1.add(Calendar.MONTH, Integer.valueOf(month[1]));
				} else if (data1[1].indexOf("-") > 0) {
					String[] month = data1[1].split("\\-");
					c1.add(Calendar.MONTH, -+Integer.valueOf(month[1]));
				}

				if (data1[2].indexOf("+") > 0) {
					String[] year = data1[2].split("\\+");
					c1.add(Calendar.YEAR, Integer.valueOf(year[1]));
				} else if (data1[2].indexOf("-") > 0) {
					String[] year = data1[2].split("\\-");
					c1.add(Calendar.YEAR, -+Integer.valueOf(year[1]));
				}
				return sdf.format(c1.getTime());
			} else {

				return "#skip#";
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**********************************************************************************************************
	 * @Objective: To get future date as specified
	 * @InputParameters:date format as enum
	 *                       dateFormat,increaseYearBy,increaseMonthBy,increaseDayBy
	 *                       as integer
	 * @OutputParameters:specified date as String
	 * @author:Adarsh
	 * @Date : 08-30-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/

	public String getFutureDate(dateFormat format, int increaseYearBy, int increaseMonthBy, int increaseDayBy) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format.getDateFormat());
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.YEAR, increaseYearBy);
			c1.add(Calendar.MONTH, increaseMonthBy);
			c1.add(Calendar.DAY_OF_MONTH, increaseDayBy);
			return sdf.format(c1.getTime());
		} catch (Exception e) {
			throw e;
		}
	}

	/**********************************************************************************************************
	 * @Objective: To get past date as specified
	 * @InputParameters:date format as enum
	 *                       dateFormat,decreaseYearBy,decreaseMonthBy,decreaseDayBy
	 *                       as integer
	 * @OutputParameters:specified date as String
	 * @author:Adarsh
	 * @Date : 08-30-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public String getPastDate(dateFormat format, int decreaseYearBy, int decreaseMonthBy, int decreaseDayBy) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format.getDateFormat());
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.YEAR, -+decreaseYearBy);
			c1.add(Calendar.MONTH, -+decreaseMonthBy);
			c1.add(Calendar.DAY_OF_MONTH, -+decreaseDayBy);
			return sdf.format(c1.getTime());
		} catch (Exception e) {
			throw e;
		}
	}

	/**********************************************************************************************************
	 * @Objective: To get last working day
	 * @InputParameters:date format as enum dateFormat
	 * @OutputParameters:last working date as String
	 * @author:Adarsh
	 * @Date : 08-30-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public String getLastWorkingDay(dateFormat format) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format.getDateFormat());
			Calendar c1 = Calendar.getInstance();
			// c1.add(Calendar.DAY_OF_MONTH, 4);
			switch (new SimpleDateFormat("EEEE", Locale.ENGLISH).format(c1.getTime())) {
			case "Monday":
				c1.add(Calendar.DAY_OF_MONTH, -3);
				break;
			case "Sunday":
				c1.add(Calendar.DAY_OF_MONTH, -2);
				break;
			default:
				c1.add(Calendar.DAY_OF_MONTH, -1);
				break;
			}
			return sdf.format(c1.getTime());
		} catch (Exception e) {
			throw e;
		}
	}

	/**********************************************************************************************************
	 * @Objective: To get provided date in specific format
	 * @InputParameters:date as String in format-yyyymmmdd
	 * @OutputParameters:Provided date in specified format(dd/mm/yyyy) as String
	 * @author:Navya
	 * @Date : 22-10-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/

	public String getSpecificFormatDate(String date) {
		try {
			if (!date.trim().equalsIgnoreCase("#skip#")) {

				String yyyy = date.substring(0, 4);
				int mmm = Integer.parseInt(date.substring(4, 6));
				String dd = date.substring(6, 8);

				String Month1 = (String) Month.of(mmm).toString().subSequence(0, 3);
				Month1 = Month1.substring(0, 1).toUpperCase() + Month1.substring(1, 3).toLowerCase();
				// System.out.println("Month : " + Month1);
				date = dd + "/" + Month1 + "/" + yyyy;
			}
			return date;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**********************************************************************************************************
	 * @Objective: The below method is created to return Date as per defined Date
	 *             Format
	 * @InputParameters: testData (Ex: d:m:y)
	 * @OutputParameters: converted date as string
	 * @author:Naresh
	 * @Date : 24-Oct-2019
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public String returnDateTime(String testData) {
		if (!testData.trim().equalsIgnoreCase("#skip#") || !testData.trim().equalsIgnoreCase("Private")) {
			String data = testData.trim();
			String resultString = null;
			if (data.contains(" ")) {
				String[] result = data.split(" ");
				if (result[1].equals("")) {
					return getDateByInputData(LSMV_dateFormat, result[0], timeZone);
				} else {
					String finResult = getDateByInputData(LSMV_dateFormat, result[0], timeZone);
					resultString = finResult + " " + result[1];
				}
				return resultString;
			}
			return getDateByInputData(LSMV_dateFormat, data, timeZone);
		}
		return testData;
	}

}
