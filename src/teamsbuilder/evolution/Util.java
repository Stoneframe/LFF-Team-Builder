package teamsbuilder.evolution;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

public class Util
{
	public static <K, V> Function<K, V> cache(Function<K, V> f)
	{
		return cache(f, new IdentityHashMap<>());
	}

	private static <K, V> Function<K, V> cache(Function<K, V> f, Map<K, V> cache)
	{
		return k -> cache.computeIfAbsent(k, f);
	}
}
