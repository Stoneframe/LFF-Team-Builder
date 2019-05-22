package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
			e.printStackTrace();
		}

		return units;
	}
}
