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

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * InputListener that intercepts public methods declared in the {@link CommandController}.
 * The CommandController is typically implemented as an inner class to the UI class.
 * <p/>
 * When overriding KeyflectionInputProcessor, keyDown and keyUp must call
 * super - unless the custom behavior consumes the event.
 */
public class KeyflectionInputListener extends InputListener
{
	private final KeyData data;

	public KeyflectionInputListener(CommandController controller)
	{
		data = new KeyData(controller);
	}
	
	/**
	 * If overriding, must call super{@link #keyDown(InputEvent, int)}.
	 * <p/>
	 * @see com.badlogic.gdx.scenes.scene2d.InputListener#keyDown(com.badlogic.gdx.scenes.scene2d.InputEvent, int)
	 */
	@Override
	public boolean keyDown(InputEvent event, int keycode)
	{
		return data.keyDown(keycode);
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
		return data.keyUp(keycode);
	}
}