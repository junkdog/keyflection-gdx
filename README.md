# keyflection-gdx
__keyflection__ is a simple annotation-based API for managing shortcuts for software built with [libGDX](http://libgdx.badlogicgames.com/). It is primarily conceived for making UI-based applications - game editors and such.


## Features
- Arbitrary number of shortcuts per action.
- Annotated methods supply default key combinations.
- Parse shortcuts as strings, aka _"SHIFT C"_.


## TODO
- Devise a means of obtaining a list of all commands/shortcut combos (currently, there is the [KeyFormatter](https://github.com/junkdog/keyflection-gdx/blob/master/keyflection-gdx/src/net/onedaybeard/keyflection/KeyFormatter.java) class, but it is not hooked into much yet).
- Write key settings file.
- Override default key combinations in the presence of a key settings file.


## Prerequisites
libGDX [nightlies](http://libgdx.badlogicgames.com/nightlies/), until the next version comes out.

## Usage
1. Declare a CommandController:
```java
public class DemoUI
{
	// snipped code

	// class must be either protected or public
	protected class ShortcutActions implements CommandController
	{
		@Command(name = "toggle UI", description = "hides/shows the UI", bindings =
			@Shortcut(Keys.H))
		public void toggleHidingHud() // methods must be public
		{
			drawHud = !drawHud;
		}

		@Command(name = "exit", description = "exit the demo", bindings = {
			@Shortcut(Keys.ESCAPE),
			@Shortcut({Keys.CONTROL_LEFT, Keys.Q}),
			@Shortcut({Keys.SHIFT_LEFT, Keys.Q})})
		public void exit()
		{
			Gdx.app.exit();
		}
	}
}
```
2. Wrap it inside an KeyflectionInputListener:
```java
public class DemoUI
{
	public void create()
	{
		// snipped

		Stage ui = ...
		ui.addListener(new KeyflectionInputListener(new ShortcutActions()));
	}

	// snip-snip
}
```
or, as an [InputProcessor](http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/InputProcessor.html):
```java
Gdx.input.setInputProcessor(new KeyflectionInputProcessor(new ShortcutActions()));
```


## Demo
There is a [demo](https://github.com/junkdog/keyflection-gdx/blob/master/keyflection-demo/src/net/onedaybeard/keyflection/demo/DemoUI.java) project, derived from [one](https://github.com/libgdx/libgdx/blob/master/tests/gdx-tests/src/com/badlogic/gdx/tests/StageTest.java) of the GDXTests.

## Limitations
- Any shortcut combination must not exceed 7 simultaneous keys.
    - This is true for parsing key combinations as well; just in case we're talking about a hypothetical multiplayer game sharing one keyboard.
- Marked methods must not accept any parameters.

## Extending
When overriding _KeyflectionInputProcessor_ or _KeyflectionInputListener_, _keyDown_ and _keyUp_ must call super - unless the custom behavior consumes the event.