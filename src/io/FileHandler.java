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
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Unit;

public class FileHandler
{
	private static Gson gson = new GsonBuilder()
		.registerTypeAdapter(Unit.class, new UnitAdapter())
		.create();

	public static String getFileName()
	{
		return String.format("Spelare\\%s", getUniqueId());
	}

	public static void writeToFile(File file, List<Unit> units)
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
			e.printStackTrace();
		}
	}

	public static List<Unit> readFromFile(File file)
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
		}

		return units;
	}

	public static List<Unit> readFromDirectory()
	{
		List<Unit> units = new LinkedList<>();

		File folder = new File("Spelare");

		for (File file : folder.listFiles())
		{
			units.addAll(readFromFile(file));
		}

		return units;
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
