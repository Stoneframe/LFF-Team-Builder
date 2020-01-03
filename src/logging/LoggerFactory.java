package logging;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerFactory
{
	public static Logger createLogger(String name)
	{
		Logger logger = Logger.getLogger(name);

		try
		{
			logger.addHandler(createFileHandler());
		}
		catch (SecurityException | IOException e)
		{
			e.printStackTrace();
		}

		return logger;
	}

	private static FileHandler createFileHandler() throws SecurityException, IOException
	{
		FileHandler fileHandler = new FileHandler(
			Paths.get("Loggar", "log").toString(),
			10 * 1024 * 1024,
			5,
			true);

		fileHandler.setFormatter(new Formatter()
		{
			@Override
			public String format(LogRecord record)
			{
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

				StringBuilder builder = new StringBuilder(1000);

				builder.append(dateFormat.format(new Date(record.getMillis())));
				builder.append(" - ");
				builder.append("[").append(record.getLevel()).append("]");
				builder.append(" - ");
				builder.append("[").append(record.getSourceClassName()).append("]");
				builder.append(" - ");
				builder.append(formatMessage(record));

				builder.append(System.lineSeparator());

				return builder.toString();
			}
		});

		return fileHandler;
	}
}
