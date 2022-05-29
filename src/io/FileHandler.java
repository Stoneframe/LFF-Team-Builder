package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import logging.LoggerFactory;
import model.Team;
import model.Unit;

public class FileHandler
{
	private static Logger logger = LoggerFactory.createLogger(FileHandler.class.getName());

	private static Gson gson = new GsonBuilder()
		.registerTypeAdapter(Unit.class, new UnitAdapter())
		.create();

	public static String getFileName()
	{
		return getUniqueId() + ".txt";
	}

	public static void writeToFile(Path filePath, List<Unit> units) throws IOException
	{
		writeToFile(filePath.toFile(), units);
	}

	public static void writeToFile(File file, List<Unit> units) throws IOException
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
		{
			for (Unit unit : units)
			{
				String json = gson.toJson(unit, Unit.class);

				writer.write(json);
				writer.write(System.lineSeparator());
			}
		}
		catch (IOException e)
		{
			logger.severe(e.toString());
			throw e;
		}
	}

	public static List<Unit> readFromFile(Path filePath) throws IOException
	{
		return readFromFile(filePath.toFile());
	}

	public static List<Unit> readFromFile(File file) throws IOException
	{
		List<Unit> units = new LinkedList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				units.add(gson.fromJson(line, Unit.class));
			}
		}
		catch (IOException e)
		{
			logger.severe(e.toString());
			throw e;
		}

		return units;
	}

	public static List<Unit> readFromFolder(Path folderPath) throws IOException
	{
		return readFromFolder(folderPath.toFile());
	}

	public static List<Unit> readFromFolder(File folder) throws IOException
	{
		List<Unit> units = new LinkedList<>();

		for (File file : folder.listFiles())
		{
			units.addAll(readFromFile(file));
		}

		return units;
	}

	public static void printTeams(Path filePath, List<Team> teams)
	{
		printTeams(filePath.toFile(), teams);
	}

	public static void printTeams(File file, List<Team> teams)
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
		{
			for (Team team : teams)
			{
				writer.write(team.toString());
				writer.write(System.lineSeparator());
			}
		}
		catch (IOException e)
		{
			logger.severe(e.toString());
		}
	}

	public static boolean exists(Path path)
	{
		return Files.exists(path);
	}

	public static void delete(Path path)
	{
		try
		{
			Files.deleteIfExists(path);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static List<String> readLines(Path path)
	{
		try
		{
			return Files.readAllLines(path);
		}
		catch (IOException e)
		{
			logger.severe(e.toString());
		}

		return Collections.emptyList();
	}

	public static void appendLine(Path path, String line)
	{
		try
		{
			Files.write(
				path,
				line.getBytes("utf-8"),
				StandardOpenOption.CREATE,
				StandardOpenOption.APPEND);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static String getUniqueId()
	{
		try
		{
			Enumeration<NetworkInterface> networkInterfaces =
				NetworkInterface.getNetworkInterfaces();

			int hash = 0;

			while (networkInterfaces.hasMoreElements())
			{
				NetworkInterface networkInterface = networkInterfaces.nextElement();

				byte[] hardwareAddress = networkInterface.getHardwareAddress();

				if (hardwareAddress != null)
				{
					hash ^= ByteBuffer.wrap(hardwareAddress).getInt();
				}
			}

			return Integer.toHexString(hash);
		}
		catch (SocketException e)
		{
			return "";
		}
	}
}
