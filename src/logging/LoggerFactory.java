package logging;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerFactory
{
	public static Logger createLogger(Object object)
	{
		Logger logger = Logger.getLogger(object.getClass().getName());

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

		fileHandler.setFormatter(new SimpleFormatter());

		return fileHandler;
	}
}
