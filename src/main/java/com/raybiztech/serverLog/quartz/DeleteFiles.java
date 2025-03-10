package com.raybiztech.serverLog.quartz;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component("deleteFiles")
public class DeleteFiles {
	public void deleteFile() {
		File directory = new File("/opt/wildfly-8.0.0.Final/standalone/log");

		if (directory.exists()) {
			File[] listFiles = directory.listFiles();

			for (File listFile : listFiles) {
				String fileName = listFile.getName();
				String fileNameRequired = "";

				if (!listFile.getName().equalsIgnoreCase("server.log")) {
					fileNameRequired = fileName.substring(
							fileName.lastIndexOf(".") + 1, fileName.length());
				}
				if (!fileNameRequired.isEmpty()) {

					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd");

					java.util.Date date2 = null;

					try {
						date2 = formatter.parse(fileNameRequired);

					} catch (ParseException e) {
						e.printStackTrace();
					}
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, -11);
					Date date3 = cal.getTime();
					if (date2.before(date3)) {
						listFile.delete();
					}

				}

			}

		}

	}

}
