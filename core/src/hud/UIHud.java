package hud;

import Scenes.Gameplay;
import Scenes.MainMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.website.scissor.GameMain;
import helpers.GameInfo;

public class UIHud {

    private GameMain game;
    private Gameplay gameplay;
    private Stage stage;
    private Viewport gameViewport;

    Label scoreLabel;
    int score;

    ImageButton retryBtn,quitBtn;

    public UIHud(GameMain game,Gameplay gameplay)
    {
        this.game= game;
        this.gameplay = gameplay;
        gameViewport = new FitViewport(GameInfo.WIDTH,GameInfo.HEIGHT,new OrthographicCamera());
        stage = new Stage(gameViewport,game.getBatch());
        createLabel();


    }

    void createLabel()
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 100;

        BitmapFont font = generator.generateFont(parameter);
        //upto here font generating code
        
        scoreLabel = new Label(String.valueOf(score),new Label.LabelStyle(font, Color.WHITE));
        scoreLabel.setPosition(GameInfo.WIDTH/2-scoreLabel.getWidth()/2,GameInfo.HEIGHT/2+200);

        stage.addActor(scoreLabel);

    }

    public void createButton()
    {
        retryBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Retry.png"))));
        quitBtn  = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Quit.png"))));

        retryBtn.setPosition(GameInfo.WIDTH/2-100-retryBtn.getWidth()/2,GameInfo.HEIGHT/2-50);
        quitBtn.setPosition(GameInfo.WIDTH/2+100-quitBtn.getWidth()/2,GameInfo.HEIGHT/2-55);

        retryBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Gameplay(game));
                stage.dispose(); //gamePlay will create stage again so dispose
                gameplay.dispose();
            }
        });

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
                stage.dispose();
                gameplay.dispose();

            }
        });

        stage.addActor(retryBtn);
        stage.addActor(quitBtn);
    }

    public void incrementScore()
    {
        score++;
        scoreLabel.setText(String.valueOf(score));//update score in Labels
    }

    public void showScore()
    {
        scoreLabel.setText(String .valueOf(score));
        stage.addActor(scoreLabel);
    }
    public Stage getStage()
    {
        return this.stage;//for calling getStage.draw()
    }

    public int getScore()
    {
        return score;
    }


}
