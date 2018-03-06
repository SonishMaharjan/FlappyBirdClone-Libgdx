package hud;

import Scenes.Gameplay;
import Scenes.MainMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.website.scissor.GameMain;
import helpers.GameInfo;
import helpers.GameManager;

public class MainMenuButtons {

    GameMain game;
    MainMenu mainMenu;//this var is created to call dispose funct of mainmenu thro button

    private Stage stage;
    private Viewport gameViewport;

    private ImageButton playBtn, scoreBtn,changeBirdBtn;

    private Label scoreLabel;


    public MainMenuButtons(GameMain game,MainMenu mainMenu)
    {
        this.game = game;
        this.mainMenu = mainMenu;
        gameViewport = new FitViewport(GameInfo.WIDTH,GameInfo.HEIGHT,new OrthographicCamera());

        stage = new Stage(gameViewport,game.getBatch());
        createButtons();
       // changeBird();
        Gdx.input.setInputProcessor(stage);
    }

    void createButtons()
    {
        playBtn = new ImageButton(new SpriteDrawable( new Sprite(new Texture("Buttons/Play.png"))));
        scoreBtn = new ImageButton(new SpriteDrawable( new Sprite(new Texture("Buttons/Score.png"))));

        playBtn.setPosition(GameInfo.WIDTH/2-playBtn.getWidth()/2-100,GameInfo.HEIGHT/2);
       // playBtn.setPosition(GameInfo.WIDTH/2-100,GameInfo.HEIGHT/2, Align.center);

        scoreBtn.setPosition(GameInfo.WIDTH/2-scoreBtn.getWidth()/2+100,GameInfo.HEIGHT/2);

        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Gameplay(game));
                stage.dispose();//this dispose eliminate the the effect of menu btn in play screen
                mainMenu.dispose();
            }
        });

        scoreBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showScore();
            }
        });

        stage.addActor(playBtn);
        stage.addActor(scoreBtn);
    }

    void changeBird()
    {
        if(changeBirdBtn !=null)
        {
            changeBirdBtn.remove();
        }
        changeBirdBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Birds/"+ GameManager.getInstance().getBird()+ "/Idle.png"))));
        changeBirdBtn.setPosition(GameInfo.WIDTH/2,GameInfo.HEIGHT/2+200,Align.center);
        changeBirdBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.getInstance().incrementIndex();
                changeBird();
            }
        });

        stage.addActor(changeBirdBtn);

    }

    void showScore()
    {
        if(scoreLabel!=null)
        {
            return;
        }
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 100;

        BitmapFont font  = generator.generateFont(parameter);

        Preferences  prefs = Gdx.app.getPreferences("Data");


        scoreLabel = new Label(String.valueOf(prefs.getInteger("Score")),new Label.LabelStyle(font, Color.BLACK));
        scoreLabel.setPosition(GameInfo.WIDTH/2,GameInfo.HEIGHT/2-200,Align.center);

        stage.addActor(scoreLabel);



    }



    public Stage getStage()
    {
        return this.stage;
    }

}
