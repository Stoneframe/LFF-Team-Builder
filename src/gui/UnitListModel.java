package gui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;

import com.google.gson.GsonBuilder;

import io.FileHandler;
import io.UnitAdapter;
import model.Unit;

public class UnitListModel
	extends AbstractListModel<Unit>
{
	private static final long serialVersionUID = -635067882933776162L;

	private static final String FILE_NAME = "backup.txt";

	private final List<Unit> units = new LinkedList<>();

	@Override
	public Unit getElementAt(int index)
	{
		return units.get(index);
	}

	@Override
	public int getSize()
	{
		return units.size();
	}

	public int indexOf(Unit unit)
	{
		return units.indexOf(unit);
	}

	public void add(Unit unit)
	{
		write(new Action(ActionType.Insert, unit, null));

		insertInternal(units.size(), unit);
	}

	public void insert(int index, Unit unit)
	{
		write(new Action(ActionType.Insert, unit, null));

		insertInternal(index, unit);
	}

	public void remove(Unit unit)
	{
		write(new Action(ActionType.Remove, null, unit));

		removeInternal(unit);
	}

	public void replace(List<Unit> unitsToRemove, List<Unit> unitsToAdd)
	{
		write(new Action(ActionType.Replace, unitsToAdd, unitsToRemove));

		replaceInternal(unitsToRemove, unitsToAdd);
	}

	public void loadFromFile(String playerFolder)
	{
		readUnitsFromFile(playerFolder);

		loadBackup();
	}

	public void loadFromFolder(String playerFolder)
	{
		insertAllInternal(FileHandler.readFromDirectory(Paths.get(playerFolder)));
	}

	public void save(String playerFolder)
	{
		writeUnits(playerFolder);

		disposeBackup();
	}

	private void readUnitsFromFile(String playerFolder)
	{
		Path filePath = Paths.get(playerFolder, FileHandler.getFileName());

		insertAllInternal(FileHandler.readFromFile(filePath));
	}

	private void writeUnits(String playerFolder)
	{
		Path filePath = Paths.get(playerFolder, FileHandler.getFileName());

		FileHandler.writeToFile(filePath, units);
	}

	private void loadBackup()
	{
		if (!FileHandler.exists(Paths.get(FILE_NAME))) return;

		List<String> lines = FileHandler.readLines(Paths.get(FILE_NAME));

		for (String line : lines)
		{
			Action action = fromJson(line);

			switch (action.type)
			{
				case Insert:
					insertAllInternal(action.inserted);
					break;

				case Remove:
					removeAllInternal(action.removed);
					break;

				case Replace:
					replaceInternal(action.removed, action.inserted);
					break;
			}
		}
	}

	private void disposeBackup()
	{
		FileHandler.delete(Paths.get(FILE_NAME));
	}

	private void replaceInternal(List<Unit> unitsToRemove, List<Unit> unitsToAdd)
	{
		int index = unitsToRemove.stream()
			.mapToInt(unit -> indexOf(unit))
			.min()
			.getAsInt();

		for (int i = 0; i < unitsToAdd.size(); i++)
		{
			insertInternal(index + i, unitsToAdd.get(i));
		}

		for (int i = 0; i < unitsToRemove.size(); i++)
		{
			removeInternal(unitsToRemove.get(i));
		}
	}

	private void insertAllInternal(List<Unit> units)
	{
		for (Unit unit : units)
		{
			insertInternal(this.units.size(), unit);
		}
	}

	private void removeAllInternal(List<Unit> units)
	{
		for (Unit unit : units)
		{
			removeInternal(unit);
		}
	}

	private void insertInternal(int index, Unit unit)
	{
		units.add(index, unit);
		fireIntervalAdded(this, index, index);
	}

	private void removeInternal(Unit unit)
	{
		int index = units.indexOf(unit);

		units.remove(unit);
		fireIntervalRemoved(this, index, index);
	}

	private static void write(Action action)
	{
		FileHandler.appendLine(Paths.get(FILE_NAME), toJson(action) + System.lineSeparator());
	}

	private static Action fromJson(String json)
	{
		return new GsonBuilder()
			.registerTypeAdapter(Unit.class, new UnitAdapter())
			.create()
			.fromJson(json, Action.class);
	}

	private static String toJson(Action action)
	{
		return new GsonBuilder()
			.registerTypeAdapter(Unit.class, new UnitAdapter())
			.create()
			.toJson(action);
	}

	private enum ActionType
	{
		Insert,
		Remove,
		Replace,
	}

	private class Action
	{
		public ActionType type;

		public List<Unit> inserted;
		public List<Unit> removed;

		public Action(ActionType type, Unit inserted, Unit removed)
		{
			this(type, Arrays.asList(inserted), Arrays.asList(removed));
		}

		public Action(ActionType type, List<Unit> inserted, List<Unit> removed)
		{
			this.type = type;
			this.inserted = inserted;
			this.removed = removed;
		}

		@Override
		public String toString()
		{
			return new GsonBuilder()
				.registerTypeAdapter(Unit.class, new UnitAdapter())
				.setPrettyPrinting()
				.create()
				.toJson(this);
		}
	}
}
