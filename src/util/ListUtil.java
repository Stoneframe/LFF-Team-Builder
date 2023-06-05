package util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ListUtil
{
	public static <T> T getRandom(List<T> list)
	{
		if (list.isEmpty())
		{
			return null;
		}

		return list.get(new Random().nextInt(list.size()));
	}

	public static <T> List<T> getShuffledCopy(List<T> list)
	{
		List<T> copy = new LinkedList<>(list);

		Collections.shuffle(copy);

		return copy;
	}
}
