package com.website.scissor;

import Scenes.Gameplay;
import Scenes.MainMenu;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMain extends Game {
	SpriteBatch batch;

	Sound sound;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MainMenu(this));
		sound = Gdx.audio.newSound(Gdx.files.internal("Spr/bgmusic.mp3"));


	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		sound.dispose();

	}

	public void playMusic()
	{
		///sound.play();
		//sound.setLooping(0,true);
	}
	public SpriteBatch getBatch()
	{
		return this.batch;
	}
}
