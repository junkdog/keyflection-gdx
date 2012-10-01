package net.onedaybeard.keyflection.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;

import net.onedaybeard.keyflection.BoundCommand;
import net.onedaybeard.keyflection.CommandManager;
import net.onedaybeard.keyflection.KeyFormatter;

public class CommandOverlay
{
	private final Window overlay;
	
	private Skin skin;

	public CommandOverlay()
	{
		this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		overlay = new Window("command shortcuts", skin);
	}
	
	public Window createOverlay()
	{
		overlay.clear();
		populateOverlay(overlay);
		overlay.pack();
		
		return overlay;
	}

	private void populateOverlay(Table overlay)
	{
		KeyFormatter formatter = new KeyFormatter();
		for (BoundCommand command : CommandManager.instance.getCommands())
		{
			overlay.add(newLabel(command.getName())).pad(2).padLeft(8).align(Align.left | Align.top);
			populateCommands(overlay, formatter, command);
		}
	}

	private void populateCommands(Table overlay, KeyFormatter formatter, BoundCommand command)
	{
		Array<String> shortcuts = command.formatShortcuts(formatter);
		for (int i = 0; shortcuts.size > i; i++)
		{
			if (i > 0)
				overlay.add();
			
			overlay.add(newLabel(shortcuts.get(i))).pad(2).padLeft(16).padRight(8).align(Align.right);
			overlay.row();
		}
		overlay.add().padBottom(18);
		overlay.row();
	}
	
	private Label newLabel(String s)
	{
		Label label =  new Label(s, skin);
		label.setColor(Color.GREEN);
		return label;
	}

}


