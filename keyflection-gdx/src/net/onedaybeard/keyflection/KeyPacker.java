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

import java.util.Arrays;

import com.badlogic.gdx.utils.IntArray;

final class KeyPacker
{
	private static final int DATA_BYTES = Long.SIZE / 8;

	private static IntArray unpacked = new IntArray(DATA_BYTES);
	
	private KeyPacker()
	{
		
	}

	static long pack(int[] keys)
	{
		if (keys.length > (DATA_BYTES - 1)) // 8th byte reserved for mouse/touch
			throw new IllegalArgumentException("Maximum length of key array is 7, found " + keys.length);
		
		Arrays.sort(keys);
		
		long packed = 0;
		for (int i = 0; keys.length > i; i++)
		{
			assert keys[i] == (keys[i] & 0xff) : "unexpected value " + keys[i];
			packed |= keys[i] << (8 * i);
		}
		
		return packed;
	}
	
	static int[] unpack(long keys)
	{
		unpacked.clear();
		for (int i = 0; DATA_BYTES > i; i++)
		{
			int keycode = (int)(keys >> (8 * i)) & 0xff;
			if (keycode > 0)
				unpacked.add(keycode);
			else
				break;
		}
		
		return unpacked.toArray();
	}
}
