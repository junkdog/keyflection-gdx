package net.onedaybeard.keyflection.sort;

import java.lang.reflect.Method;
import java.util.Comparator;

import net.onedaybeard.keyflection.annotation.Command;

public final class CommandNameComparator implements Comparator<Method>
{
	@Override
	public int compare(Method o1, Method o2)
	{
		return getName(o1).compareTo(getName(o2));
	}
	
	private static String getName(Method m)
	{
		return m.getAnnotation(Command.class).name();
	}
}