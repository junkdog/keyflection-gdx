package net.onedaybeard.keyflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.LongMap;

final class KeyData
{
	private final IntArray pressedKeys;
	private final LongMap<Method> shortcuts;
	private final CommandController controller;

	KeyData(CommandController controller)
	{
		pressedKeys = new IntArray(false, 7);
		shortcuts = ShortcutConfigurator.create(controller);
		this.controller = controller;
	}
	
	boolean keyDown(int keycode)
	{
		pressedKeys.add(keycode);
		long keyCombination = KeyPacker.pack(pressedKeys.toArray());
		boolean consumed = shortcuts.containsKey(keyCombination);
		
		if (consumed)
		{
			try
			{
				shortcuts.get(keyCombination).invoke(controller);
			}
			catch (IllegalArgumentException e)
			{
				throw new RuntimeException(e);
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(
					"Is the CommandController's visibility too restrictive?", e);
			}
			catch (InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return consumed;
	}
	
	boolean keyUp(int keycode)
	{
		pressedKeys.removeValue(keycode);
		return false;
	}
}