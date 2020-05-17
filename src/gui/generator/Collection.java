package gui.generator;

import java.util.LinkedList;

import model.Unit;

public class Collection
	extends LinkedList<Unit>
{
	private static final long serialVersionUID = -6559677279332538692L;

	private final String name;

	public Collection(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}
