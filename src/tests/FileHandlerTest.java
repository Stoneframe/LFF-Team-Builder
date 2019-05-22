package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.FileHandler;
import model.Unit;

public class FileHandlerTest
{
	@Test
	public void writeToFile()
	{
		File file = new File("units");

		List<Unit> writtenUnits = UnitsUtil.createRandomUnitList();
		
		FileHandler.writeToFile(file, writtenUnits);
		
		List<Unit> readUnits = FileHandler.readFromFile(file);
		
		assertEquals(writtenUnits.size(), readUnits.size());
	}
}
