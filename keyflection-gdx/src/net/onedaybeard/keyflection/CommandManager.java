/*
 * Copyright 2012 Adrian Papari
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.onedaybeard.keyflection;

import java.lang.reflect.Method;
import java.util.Iterator;

import com.badlogic.gdx.utils.Array;

/**
 * Will at a later point facilitate overriding default shortcuts.
 */
public enum CommandManager
{
	instance;
	
	private final Array<BoundCommand> commands;
	
	public boolean debug = false;
	
	private CommandManager()
	{
		commands = new Array<BoundCommand>();
	}
	
	@SuppressWarnings("static-method")
	public void setSingleModifierKeys(boolean lrModifierEquality)
	{
		KeyData.modfierKeyEquality = lrModifierEquality;
	}
	
	@SuppressWarnings("static-method")
	public boolean isSingleModifierKeys()
	{
		return KeyData.modfierKeyEquality;
	}
	
	void addCommand(Method method, CommandController instance)
	{
		commands.add(new BoundCommand(method, instance));
	}
	
	public void remove(CommandController instance)
	{
		Iterator<BoundCommand> iterator = commands.iterator();
		while (iterator.hasNext())
		{
			BoundCommand command = iterator.next();
			if (command.getCommandInstance() == instance)
				iterator.remove();
		}
	}
	
	public void remove(Class<? extends CommandController> klazz)
	{
		Iterator<BoundCommand> iterator = commands.iterator();
		while (iterator.hasNext())
		{
			BoundCommand command = iterator.next();
			if (command.getCommandInstance().getClass() == klazz)
				iterator.remove();
		}
	}
	
	public void removeAll()
	{
		commands.clear();
	}
	
	public Array<BoundCommand> getCommands()
	{
		return commands;
	}
}
