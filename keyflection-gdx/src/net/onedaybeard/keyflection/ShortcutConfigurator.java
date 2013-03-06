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
import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.Input.Keys;
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
		
		sort(methods, controller);
		
		for (Method method : methods)
		{
			assignKeyToMethod(shortcutToMethodMap, method);
				CommandManager.instance.addCommand(method, controller);
		}
		
		return shortcutToMethodMap;
	}
	
	private static void sort(Array<Method> methods, CommandController controller)
	{
		switch (controller.commandOrder())
		{
			case COMMAND_NAME:
				methods.sort(new CommandNameComparator());
				break;
			case METHOD_NAME:
				methods.sort(new MethodNameComparator());
				break;
			case SHORTCUTS:
				methods.sort(new ShortcutComparator());
				break;
			default:
				break;
		}
	}

	static LongMap<Method> createOld(CommandController controller)
	{
		LongMap<Method> shortcutToMethodMap = new LongMap<Method>();
		
		Method[] methods = controller.getClass().getDeclaredMethods();
		Arrays.sort(methods, new Comparator<Method>()
			{
			@Override
			public int compare(Method o1, Method o2)
			{
				return o1.getName().compareTo(o2.getName());
			}
			});
		
		for (Method method : methods)
		{
			if (!method.isAnnotationPresent(Command.class))
				continue;
			
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
	
	private static final class MethodNameComparator implements Comparator<Method>
	{
		@Override
		public int compare(Method o1, Method o2)
		{
			return o1.getName().compareTo(o2.getName());
		}
	}
	
	private static final class CommandNameComparator implements Comparator<Method>
	{
		@Override
		public int compare(Method o1, Method o2)
		{
			return getName(o1).compareTo(getName(o2));
		}
		
		private static String getName(Method m)
		{
			return m.getAnnotation(Command.class).name();
		}
	}
	
	private static final class ShortcutComparator implements Comparator<Method>
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
}
