package net.onedaybeard.keyflection.demo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Scaling;

import net.onedaybeard.keyflection.Command;
import net.onedaybeard.keyflection.CommandController;
import net.onedaybeard.keyflection.KeyflectionInputListener;
import net.onedaybeard.keyflection.KeyflectionInputProcessor;
import net.onedaybeard.keyflection.Shortcut;

/*******************************************************************************
 * Copyright 2011 See AUTHORS.libgdx file.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

public class DemoUI implements ApplicationListener
{
	private static final int NUM_GROUPS = 5;
	private static final int NUM_SPRITES = (int)Math.sqrt(400 / NUM_GROUPS);
	private static final float SPACING = 5;
	
	ShapeRenderer renderer;
	Stage stage;
	Stage ui;
	Texture texture;
	Texture uiTexture;
	BitmapFont font;

	boolean rotateSprites = false;
	boolean scaleSprites = false;
	float angle;
	Array<Image> images = new Array<Image>();
	float scale = 1;
	float vScale = 1;
	Label fps;
	Label hint;

	private Color background;
	private boolean drawHud = true;
	private StageKeys stageActions;

	@Override
	public void create()
	{
		background = new Color(0.2f, 0.2f, 0.2f, 1f);
		
		texture = new Texture(Gdx.files.internal("data/badlogicsmall.jpg"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		
		stageActions = new StageKeys();

		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

		float loc = (NUM_SPRITES * (32 + SPACING) - SPACING) / 2;
		for (int i = 0; i < NUM_GROUPS; i++)
		{
			Group group = new Group();
			group.setX((float)Math.random() * (stage.getWidth() - NUM_SPRITES * (32 + SPACING)));
			group.setY((float)Math.random() * (stage.getHeight() - NUM_SPRITES * (32 + SPACING)));
			group.setOrigin(loc, loc);

			fillGroup(group, texture);
			stage.addActor(group);
		}

		uiTexture = new Texture(Gdx.files.internal("data/ui.png"));
		uiTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		ui.addListener(new KeyflectionInputListener(stageActions));

		Image blend = new Image(new TextureRegion(uiTexture, 0, 0, 64, 32));
		blend.setAlign(Align.center);
		blend.setScaling(Scaling.none);
		blend.addListener(new InputListener()
		{
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				stageActions.toggleBlending();
				return true;
			}
		});
		blend.setY(ui.getHeight() - 64);

		Image rotate = new Image(new TextureRegion(uiTexture, 64, 0, 64, 32));
		rotate.setAlign(Align.center);
		rotate.setScaling(Scaling.none);
		rotate.addListener(new InputListener()
		{
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				stageActions.toggleRotation();
				return true;
			}
		});
		rotate.setPosition(64, blend.getY());

		Image scale = new Image(new TextureRegion(uiTexture, 64, 32, 64, 32));
		scale.setAlign(Align.center);
		scale.setScaling(Scaling.none);
		scale.addListener(new InputListener()
		{
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				stageActions.toggleScaling();
				return true;
			}
		});
		scale.setPosition(128, blend.getY());

		ui.addActor(blend);
		ui.addActor(rotate);
		ui.addActor(scale);

		hint = new Label("See console output for shortcuts (hint: s, r, b, Shift-r, h, ESC).", new Label.LabelStyle(font, Color.WHITE));
		hint.setPosition(10, 45);
		hint.setColor(0, 1, 0, 1);
		ui.addActor(hint);
		
		fps = new Label("fps: 0", new Label.LabelStyle(font, Color.WHITE));
		fps.setPosition(10, 30);
		fps.setColor(0, 1, 0, 1);
		ui.addActor(fps);

		renderer = new ShapeRenderer();
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new KeyflectionInputProcessor(new GlobalKeys()));
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(ui);
		
		Gdx.input.setInputProcessor(multiplexer);
	}

	private void fillGroup(Group group, Texture texture)
	{
		float advance = 32 + SPACING;
		for (int y = 0; y < NUM_SPRITES * advance; y += advance)
			for (int x = 0; x < NUM_SPRITES * advance; x += advance)
			{
				Image img = new Image(new TextureRegion(texture));
				img.setAlign(Align.center);
				img.setScaling(Scaling.none);
				img.setBounds(x, y, 32, 32);
				img.setOrigin(16, 16);
				group.addActor(img);
				images.add(img);
			}
	}

	@Override
	public void render()
	{
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(background.r, background.g, background.b, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isTouched())
		{
			Vector2 stageCoords = Vector2.tmp;
			stage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
			Actor actor = stage.hit(stageCoords.x, stageCoords.y, true);
			if (actor instanceof Image)
				((Image)actor).setColor((float)Math.random(), (float)Math.random(), (float)Math.random(),
					0.5f + 0.5f * (float)Math.random());
		}

		Array<Actor> actors = stage.getActors();
		int len = actors.size;
		if (rotateSprites)
		{
			for (int i = 0; i < len; i++)
				actors.get(i).rotate(Gdx.graphics.getDeltaTime() * 10);
		}

		scale += vScale * Gdx.graphics.getDeltaTime();
		if (scale > 1)
		{
			scale = 1;
			vScale = -vScale;
		}
		if (scale < 0.5f)
		{
			scale = 0.5f;
			vScale = -vScale;
		}

		len = images.size;
		for (int i = 0; i < len; i++)
		{
			Image img = images.get(i);
			if (rotateSprites)
				img.rotate(-40 * Gdx.graphics.getDeltaTime());
			else
				img.setRotation(0);

			if (scaleSprites)
			{
				img.setScale(scale);
			}
			else
			{
				img.setScale(1);
			}
			img.invalidate();
		}

		stage.draw();

		renderer.begin(ShapeType.Point);
		renderer.setColor(1, 0, 0, 1);
		len = actors.size;
		for (int i = 0; i < len; i++)
		{
			Group group = (Group)actors.get(i);
			renderer.point(group.getX() + group.getOriginX(), group.getY() + group.getOriginY(), 0);
		}
		renderer.end();

		if (drawHud)
		{
			fps.setText("fps: " + Gdx.graphics.getFramesPerSecond() + ", actors " + images.size + ", groups " +
				actors.size);
			ui.draw();
		}
	}

	@Override
	public void dispose()
	{
//		ui.dispose(); // FIXME: crashes, somehow - maybe an OSX or nightlies issue?
		renderer.dispose();
		texture.dispose();
		uiTexture.dispose();
		font.dispose();
	}

	@Override
	public void pause()
	{
		
	}

	@Override
	public void resize(int arg0, int arg1)
	{
		
	}

	@Override
	public void resume()
	{
		
	}
	
	protected class StageKeys implements CommandController
	{
		@Command(name = "toggle UI", description = "hides/shows the UI", bindings =
			@Shortcut(Keys.H))
		public void toggleHidingHud()
		{
			drawHud = !drawHud;
		}
		
		@Command(name = "randomize background", description = "colors...", bindings = {
			@Shortcut({Keys.SHIFT_LEFT, Keys.R}),
			@Shortcut({Keys.SHIFT_RIGHT, Keys.R})})
		public void randomizeBackground()
		{
			background.r = MathUtils.random(1f);
			background.g = MathUtils.random(1f);
			background.b = MathUtils.random(1f);
		}
		
		@Command(name = "toggle scaling", description = "toggles sprite scaling", bindings = 
			@Shortcut(Keys.S))
		public void toggleScaling()
		{
			scaleSprites = !scaleSprites;
		}
		
		@Command(name = "toggle rotation", description = "toggles sprite rotation", bindings = 
			@Shortcut(Keys.R))
		public void toggleRotation()
		{
			rotateSprites = !rotateSprites;
		}
		
		@Command(name = "toggle blend", description = "toggles blending", bindings = 
			@Shortcut(Keys.B))
		public void toggleBlending()
		{
			if (stage.getSpriteBatch().isBlendingEnabled())
				stage.getSpriteBatch().disableBlending();
			else
				stage.getSpriteBatch().enableBlending();
		}
	}

	protected class GlobalKeys implements CommandController
	{
		@Command(name = "exit", description = "exit the demo", bindings = {
			@Shortcut(Keys.ESCAPE),
			@Shortcut({Keys.CONTROL_LEFT, Keys.Q}),
			@Shortcut({Keys.SHIFT_LEFT, Keys.Q})})
		public void exit()
		{
			try
			{
				dispose();
			}
			catch (GdxRuntimeException e)
			{
				e.printStackTrace();
			}
			finally
			{
				Gdx.app.exit();
			}
		}
	}
}
