package io;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import model.Unit;

public class UnitAdapter
	implements
		JsonSerializer<Unit>,
		JsonDeserializer<Unit>
{
	private static final String CLASSNAME = "CLASSNAME";
	private static final String INSTANCE = "INSTANCE";

	@Override
	public JsonElement serialize(
		Unit unit,
		Type type,
		JsonSerializationContext context)
	{
		JsonObject retValue = new JsonObject();

		retValue.addProperty(CLASSNAME, unit.getClass().getName());
		retValue.add(INSTANCE, context.serialize(unit));

		return retValue;
	}

	@Override
	public Unit deserialize(
		JsonElement json,
		Type type,
		JsonDeserializationContext context)
			throws JsonParseException
	{
		JsonObject jsonObject = json.getAsJsonObject();

		return context.deserialize(jsonObject.get(INSTANCE), getClass(jsonObject));
	}

	private Class<?> getClass(JsonObject jsonObject)
	{
		try
		{
			return Class.forName(getClassName(jsonObject));
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			throw new JsonParseException(e.getMessage());
		}
	}

	private String getClassName(JsonObject jsonObject)
	{
		JsonPrimitive prim = (JsonPrimitive)jsonObject.get(CLASSNAME);

		return prim.getAsString();
	}
}
