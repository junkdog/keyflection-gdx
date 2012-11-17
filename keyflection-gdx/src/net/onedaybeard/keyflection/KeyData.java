/*
 * Copyright 2012 Adrian Papari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		boolean consumed = false;
		try
		{
			pressedKeys.add(keycode);
			long keyCombination = KeyPacker.pack(pressedKeys.toArray());
			consumed = shortcuts.containsKey(keyCombination);
		
			if (consumed)
				shortcuts.get(keyCombination).invoke(controller);
		}
		catch (IllegalArgumentException e)
		{
			System.err.println("Clearing keys - might help");
			pressedKeys.clear();
			
			e.printStackTrace();
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
		return consumed;
	}
	
	boolean keyUp(int keycode)
	{
		pressedKeys.removeValue(keycode);
		return false;
	}
}