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

import java.lang.reflect.Method;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LongArray;

import net.onedaybeard.keyflection.annotation.Command;
import net.onedaybeard.keyflection.annotation.Shortcut;

/**
 * Object representation of the {@link Command} annotation. 
 */
public class BoundCommand
{
	private final Command command;
	private final Method method;
	
	private final LongArray shortcuts;
	
	BoundCommand(Method method)
	{
		if (!method.isAnnotationPresent(Command.class))
			throw new RuntimeException("No @Command on " + method.getName());
		
		command = method.getAnnotation(Command.class);
		this.method = method;
		
		shortcuts = new LongArray();
		for (Shortcut shortcut : command.bindings())
		{
			shortcuts.add(KeyPacker.pack(shortcut.value()));
		}
	}

	public String getName()
	{
		return command.name();
	}

	public String getDescription()
	{
		return command.description();
	}

	public LongArray getShortcuts()
	{
		return shortcuts;
	}
	
	public Array<String> formatShortcuts(KeyFormatter formatter)
	{
		Array<String> keys = new Array<String>();
		for (int i = 0; shortcuts.size > i; i++)
		{
			keys.add(formatter.parse(shortcuts.get(i)));
		}
		return keys;
	}
	
	public boolean acceptsShortcut(long packedKeys)
	{
		return shortcuts.contains(packedKeys);
	}

	public Method getMethod()
	{
		return method;
	}
}
