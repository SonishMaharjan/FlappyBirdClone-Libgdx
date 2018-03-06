package Scenes;

import bird.Bird;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.website.scissor.GameMain;
import ground.GroundBody;
import helpers.GameInfo;
import hud.UIHud;
import pipes.Pipes;

public class Gameplay implements Screen,ContactListener{

    private GameMain game;
    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private Array<Sprite> bgs = new Array<Sprite>();
    private Array<Sprite> grounds = new Array<Sprite>();

    private Bird bird;
    private World world;

    private OrthographicCamera debugCamera;
    private Box2DDebugRenderer debugRender;

    private GroundBody groundBody;

    //private Pipes pipes;

    private Array<Pipes> pipesArray= new Array<Pipes>();
    private final int DISTN_BETN_PIPES = 800;

    private UIHud hud;

    private boolean firstTouch;

    private Sound scoreSound,birdDiedSound,birdFlapSound;

    int i= 0;

    public Gameplay(GameMain game)
    {
        this.game = game;
        mainCamera = new OrthographicCamera(GameInfo.WIDTH,GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH/2f,GameInfo.HEIGHT/2f,0);

        gameViewport = new StretchViewport(GameInfo.WIDTH,GameInfo.HEIGHT,mainCamera);

        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false,GameInfo.WIDTH/100f,GameInfo.HEIGHT/100);
        debugCamera.position.set(GameInfo.WIDTH/2,GameInfo.HEIGHT/2,0);
        debugRender =new Box2DDebugRenderer();


        world = new World(new Vector2(0,-14f),true);
        world.setContactListener(this);
        bird = new Bird(world,GameInfo.WIDTH/2,GameInfo.HEIGHT/2);

        createBackgrounds();
        createGrounds();
        groundBody = new GroundBody(world,grounds.get(0));
        //Gdx.input.setInputProcessor(this);

       // pipes = new Pipes(world,GameInfo.WIDTH/2+50);
       // pipes.setMainCamera(mainCamera);



        hud = new UIHud(game,this);

        scoreSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Score.mp3"));
        birdDiedSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Dead.mp3"));
        birdFlapSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Fly.mp3"));

    game.playMusic();

    }

    void createBackgrounds(){
        for(int i=0;i<3;i++)
        {
            Sprite bg = new Sprite(new Texture("Spr/jpt.png"));
            bg.setPosition(i*bg.getWidth(),0);
            bgs.add(bg);
        }
    }

    void drawBackgrounds(SpriteBatch batch)
    {
        for(Sprite s:bgs)
        {
            batch.draw(s,s.getX(),s.getY());
        }
    }

    void moveBackgrounds()
    {
        for(Sprite bg: bgs)
        {
            float x1 = bg.getX()-0.5f;
            bg.setPosition(x1,bg.getY());

            if(bg.getX()+GameInfo.WIDTH + (bg.getWidth()/2f)<mainCamera.position.x)
            {
                float x2 = bg.getX() +3*bg.getWidth();
                bg.setPosition(x2,bg.getY());
            }

        }


    }


    void createGrounds()
    {
        for(int i=0;i<3;i++)
        {
            Sprite s = new Sprite(new Texture("Spr/rockground.png"));
           // Sprite s = new Sprite(new Texture("Spr/ground.png"));
            s.setPosition(i*s.getWidth(),-100);
            grounds.add(s);

        }
    }

    void drawGrounds(SpriteBatch batch)
    {
        for(Sprite grnd:grounds)
        {
            batch.draw(grnd,grnd.getX(),grnd.getY());
        }
    }


    void moveGrounds()
    {
        for(Sprite grnd:grounds)
        {
            float x1 = grnd.getX()-3.3f;
            grnd.setPosition(x1,grnd.getY());
            if(grnd.getX()+grnd.getWidth()+(GameInfo.WIDTH)/2<mainCamera.position.x)
            {
                float x2= grnd.getX();
                grnd.setPosition(x2+3f*grnd.getWidth(),grnd.getY());

            }
        }

    }


    void createPipes()
    {

        Pipes p = new Pipes(world, DISTN_BETN_PIPES);



        p.setMainCamera(mainCamera);
        pipesArray.add(p);
    }

    void drawPipes(SpriteBatch batch)
    {
        for(Pipes pipe: pipesArray)
        {
            pipe.drawPipes(batch);
        }
    }

    void updatePipes()
    {
        for(Pipes pipe: pipesArray)
        {
            pipe.updatePipes();
        }
    }

    void movePipes()
    {
        for(Pipes pipe:pipesArray)
        {
            pipe.movePipes();
        }
    }

    void stopPipes()
    {
        for(Pipes p: pipesArray)
        {
            p.stopPipes();
        }
    }
    void birdFlap()
    {
        if(Gdx.input.justTouched())
        {
            birdFlapSound.play();
            bird.birdFlap();
        }
    }


    void birdDied()
    {
        if(bird.getIsAlive())
        {
            birdDiedSound.play();
            bird.birdDied();
            bird.setIsAlive(false);
            stopPipes();

            hud.getStage().clear();//other wise actiton is called forever

            //this is wirrte because sprite doewnot synch when bird is died;
            bird.updateBird();

            hud.showScore();
            hud.createButton();
            Gdx.input.setInputProcessor(hud.getStage());

            Preferences prefs = Gdx.app.getPreferences("Data");
            int highScore = prefs.getInteger("Score");

            if(highScore< hud.getScore())
            {
                prefs.putInteger("Score",hud.getScore());
                prefs.flush();
            }


        }
    }

    void createAllPipes()
    {
        RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {
                createPipes();
            }
        });

        SequenceAction sa = new SequenceAction();
        sa.addAction(Actions.delay(1.5f));
        sa.addAction(run);

        hud.getStage().addAction(Actions.forever(sa));

    }

    void update(float dt)
    {
        if(bird.getIsAlive()) {
            bird.updateBird();
            moveBackgrounds();
            moveGrounds();
            birdFlap();
            // pipes.updatePipes();
            // pipes.movePipes();
            movePipes();
            updatePipes();
        }


    }

    void checkForFirstTouch()
    {
        if(!firstTouch)
        {
            if(Gdx.input.justTouched()) {
                firstTouch = true;
                bird.activateBird();
                createAllPipes();

            }
            bird.updateBird();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        checkForFirstTouch();

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

       // game.getBatch().setProjectionMatrix(mainCamera.combined);//add this line to avoid sync between body and spriet


        game.getBatch().begin();
        drawBackgrounds(game.getBatch());
               //pipes.drawPipes(game.getBatch());
        drawPipes(game.getBatch());
        drawGrounds(game.getBatch());
        bird.drawIdle(game.getBatch());
        bird.drawAnimateBird(game.getBatch());

        game.getBatch().end();
       // debugRender.render(world,debugCamera.combined);

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.getStage().act();

        world.step(Gdx.graphics.getDeltaTime(),6,2);

    }

    @Override
    public void resize(int width, int height) {

        gameViewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("Disposed called");
        world.dispose();
        for(Sprite s:bgs)
        {
            s.getTexture().dispose();
        }

       for(Sprite s:grounds)
       {
           s.getTexture().dispose();
       }

       for(Pipes p:pipesArray)
       {
           p.dispose();
       }

       bird.dispose();


        scoreSound.dispose();
        birdFlapSound.dispose();
        birdDiedSound.dispose();

    }

    @Override
    public void beginContact(Contact contact) {

        Fixture body1, body2;

        if(contact.getFixtureA().getUserData() =="Bird")
        {
            body1 = contact.getFixtureA();
            body2 = contact.getFixtureB();
        }else
        {
            body1 = contact.getFixtureB();
            body2 = contact.getFixtureA();
        }

        if(body1.getUserData() == "Bird"&& body2.getUserData()=="Pipe")
        {
            if(bird.getIsAlive()) {
                System.out.println("Bird Dead");

                birdDied();
            }
        }
        if(body1.getUserData() == "Bird"&& body2.getUserData()=="Ground")
        {

            if(bird.getIsAlive()) {
                System.out.println("Bird Dead");
                birdDied();
            }
        }
        if(body1.getUserData() == "Bird"&& body2.getUserData()=="Score")
        {
            if(bird.getIsAlive()) {
                hud.incrementScore();
                scoreSound.play();

                pipesArray.get(i).cutPaper();
                i++;




            }
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
