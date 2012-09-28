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

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.LongMap;

/**
 * InputListener that intercepts public methods declared in the {@link CommandController}.
 * The CommandController is typically implemented as an inner class to the UI class.
 * <p/>
 * When overriding KeyflectionInputProcessor, keyDown and keyUp must call
 * super - unless the custom behavior consumes the event.
 */
public class KeyflectionInputListener extends InputListener
{
	private final IntArray pressedKeys;
	private final LongMap<Method> shortcuts;
	private final CommandController controller;

	public KeyflectionInputListener(CommandController controller)
	{
		pressedKeys = new IntArray(false, 7);
		shortcuts = ShortcutConfigurator.create(controller);
		this.controller = controller;
	}
	
	/**
	 * If overriding, must call super{@link #keyDown(InputEvent, int)}.
	 * <p/>
	 * @see com.badlogic.gdx.scenes.scene2d.InputListener#keyDown(com.badlogic.gdx.scenes.scene2d.InputEvent, int)
	 */
	@Override
	public boolean keyDown(InputEvent event, int keycode)
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
	
	/**
	 * If overriding, must call super{@link #keyUp(InputEvent, int)}.
	 * 
	 * <p/>
	 * @see com.badlogic.gdx.scenes.scene2d.InputListener#keyUp(com.badlogic.gdx.scenes.scene2d.InputEvent, int)
	 */
	@Override
	public boolean keyUp(InputEvent event, int keycode)
	{
		pressedKeys.removeValue(keycode);
		return false;
	}
}