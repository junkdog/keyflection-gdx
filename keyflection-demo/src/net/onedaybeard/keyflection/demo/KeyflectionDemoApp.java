package net.onedaybeard.keyflection.demo;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class KeyflectionDemoApp
{
	public static void main(String[] args)
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "keyflection-demo";
		cfg.useGL20 = false;
		cfg.width = 1024;
		cfg.height = 768;

		new LwjglApplication(new DemoUI(), cfg);
	}
}
