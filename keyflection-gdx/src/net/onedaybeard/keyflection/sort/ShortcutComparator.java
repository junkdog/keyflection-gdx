package net.onedaybeard.keyflection.sort;

import java.lang.reflect.Method;
import java.util.Comparator;

import com.badlogic.gdx.Input.Keys;

import net.onedaybeard.keyflection.KeyPacker;
import net.onedaybeard.keyflection.annotation.Command;
import net.onedaybeard.keyflection.annotation.Shortcut;

public final class ShortcutComparator implements Comparator<Method>
{
	// TODO: refactor
	private static final int[] MODIFIER_KEYS = {
		Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT, 
		Keys.ALT_LEFT, Keys.ALT_RIGHT,
		Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT};
	
	@Override
	public int compare(Method o1, Method o2)
	{
		return getShortcutValue(o1) - getShortcutValue(o2);
	}
	
	public static int getShortcutValue(Method m)
	{
		Shortcut shortcut = m.getAnnotation(Command.class).bindings()[0];
		
		int keys[] = KeyPacker.unpack(KeyPacker.pack(shortcut.value()));
		int value = 0;
		for (int key : keys)
		{
			if (isModifier(key))
				continue;
			
			value = key;
			break;
		}
		return value;
	}
	
	private static boolean isModifier(int keyValue)
	{
		for (int key : MODIFIER_KEYS)
		{
			if (key == keyValue)
				return true;
		}
		
		return false;
	}
}