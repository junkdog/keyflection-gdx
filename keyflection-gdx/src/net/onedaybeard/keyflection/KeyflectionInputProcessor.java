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

import com.badlogic.gdx.InputProcessor;

/**
 * InputProcessor that intercepts public methods declared in the {@link CommandController}.
 * The CommandController is typically implemented as an inner class to the UI class.
 * <p/>
 * When overriding KeyflectionInputProcessor, keyDown and keyUp must call
 * super - unless the custom behavior consumes the event.
 */
public class KeyflectionInputProcessor implements InputProcessor
{
	private final KeyData data;

	public KeyflectionInputProcessor(CommandController controller)
	{
		data = new KeyData(controller);
	}
	
	/**
	 * If overriding, must call super{@link #keyDown(int)}.
	 * <p/>
	 * @see com.badlogic.gdx.InputProcessor#keyDown(int)
	 */
	@Override
	public boolean keyDown(int keycode)
	{
		return data.keyDown(keycode);
	}

	/**
	 * If overriding, must call super{@link #keyUp(int)}.
	 * 
	 * <p/>
	 * @see com.badlogic.gdx.InputProcessor#keyUp(int)
	 **/
	@Override
	public boolean keyUp(int keycode)
	{
		return data.keyUp(keycode);
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
}