# keyflection-gdx
__keyflection__ is a simple annotation-based API for managing shortcuts for software built with [libGDX](http://libgdx.badlogicgames.com/). It is primarily conceived for making UI-based applications - game editors and such.


## Features
- Arbitrary number of shortcuts per action.
- Annotated methods supply default key combinations.
- Parse shortcuts as strings, aka _"SHIFT C"_.

## TODO
- Write a key settings file.
- Default key combinations are overridden in the presence of a key settings file.

## Usage


## Demo


## Limitations
- Any shortcut combination must not exceed 7 simultaneous keys.
    - This is true for parsing key combinations as well; just in case we're talking about a hypothetical multiplayer game sharing one keyboard.
- Marked methods must not accept any parameters.

## Extending
When overriding _KeyflectionInputProcessor_ or _KeyflectionInputListener_, _keyDown_ and _keyUp_ must call super - unless the custom behavior consumes the event.
