package net.onedaybeard.keyflection.sort;

import java.lang.reflect.Method;
import java.util.Comparator;

public final class MethodNameComparator implements Comparator<Method>
{
	@Override
	public int compare(Method o1, Method o2)
	{
		return o1.getName().compareTo(o2.getName());
	}
}