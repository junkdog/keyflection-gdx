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
import com.badlogic.gdx.utils.LongMap;

import net.onedaybeard.keyflection.annotation.Command;
import net.onedaybeard.keyflection.annotation.Shortcut;

final class ShortcutConfigurator
{
	private ShortcutConfigurator()
	{
		
	}

	static LongMap<Method> create(CommandController controller)
	{
		LongMap<Method> shortcutToMethodMap = new LongMap<Method>();
		
		Array<Method> methods = new Array<Method>();
		for (Method method : controller.getClass().getDeclaredMethods())
		{
			if (method.isAnnotationPresent(Command.class))
				methods.add(method);
		}
		
		if (controller.commandComparator() != null)
			methods.sort(controller.commandComparator());
		
		for (Method method : methods)
		{
			assignKeyToMethod(shortcutToMethodMap, method);
				CommandManager.instance.addCommand(method, controller);
		}
		
		return shortcutToMethodMap;
	}
	
	private static void assignKeyToMethod(LongMap<Method> shortcutToMethodMap, Method method)
	{
		KeyFormatter formatter = new KeyFormatter();
		
		Command command = method.getAnnotation(Command.class);
		for (Shortcut shortcut : command.bindings())
		{
			if (CommandManager.instance.debug)
				System.out.printf("Adding shortcut for command '%s': '%s'\n", command.name(), formatter.parse(shortcut.value()));
			
			shortcutToMethodMap.put(KeyPacker.pack(shortcut.value()), method);
		}
	}
}
