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

import java.lang.reflect.Field;
import java.util.Arrays;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;

/**
 * Format key combinations into human-readable strings.
 */
public class KeyFormatter
{
	private static final int[] MODIFIER_KEYS = {
		Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT, 
		Keys.ALT_LEFT, Keys.ALT_RIGHT,
		Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT};
	
	private Field[] keyFields;

	public KeyFormatter()
	{
		keyFields = Keys.class.getFields();
		Arrays.sort(MODIFIER_KEYS);
	}

	public String parse(int[] keycodes)
	{
		return parse(KeyPacker.pack(keycodes));
	}
	
	public String parse(long keycodes)
	{
		Array<String> modifierKeys = new Array<String>(8);
		Array<String> keys = new Array<String>();
		
		int[] leKeys = KeyPacker.unpack(keycodes);
		for (int key : leKeys)
		{
			formatKey(key, (Arrays.binarySearch(MODIFIER_KEYS, key) > - 1 ? modifierKeys : keys));
		}
		return format(modifierKeys, keys);
	}
	
	private void formatKey(int keycode, Array<String> into)
	{
		for (int i = 0; keyFields.length > i; i++)
		{
			try
			{
				if (keyFields[i].getInt(null) == keycode)
				{
					into.add(keyFields[i].getName());
					break;
				}
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private static String format(Array<String> modifier, Array<String> keys)
	{
		StringBuilder sb = new StringBuilder();
		for (String s : modifier)
		{
			if (KeyData.modfierKeyEquality)
				s = s.replace("_LEFT", "").replace("_RIGHT", "");

			sb.append(toSentenceCase(s)).append('+');
		}
		
		for (String s : keys)
		{
			sb.append(toSentenceCase(sanitizeKeys(s))).append('+');
		}
		
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	
	private static String sanitizeKeys(String s)
	{
		return s.replace("NUM_", "").replace("DPAD_", "");
	}

	private static String toSentenceCase(String input)
	{
		if (input.length() == 1)
			return input;
		
		String s = input.replace('_', ' ');
		s = s.substring(0, 1) + s.substring(1).toLowerCase();
		return s;
	}
}
