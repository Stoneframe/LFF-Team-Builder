package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PlayerTest
{
	@Test
	public void counterNumberOfScoreAble()
	{
		Player player = new Player("name", 10);

		assertEquals(1, player.count(NumberOf.SCORE_ABLE));
	}
}
