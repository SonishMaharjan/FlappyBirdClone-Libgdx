package pipes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.sun.org.apache.xpath.internal.operations.Or;
import helpers.GameInfo;

import java.util.Random;

public class Pipes  {
//controls the pipes

    World world ;
    Body body1,body2,score;

    private Sprite pipe1,pipe2,paper,torn;
    private final float DISTN_BETN_PIPE = 400;
    private Random random = new Random();

    Texture tornPaper;

    private OrthographicCamera mainCamera= new OrthographicCamera();







    public Pipes(World world, float x)
    {
        this.world = world;
        createPipes(x,getRandomY());

        tornPaper = new Texture("Spr/torn.png");
    }

    void createPipes(float x, float y)
    {
     //   pipe1 = new Sprite(new Texture("Pipes/Pipe 1.png"));
     //  pipe2 = new Sprite(new Texture("Pipes/Pipe 2.png"));

     //   pipe1 = new Sprite(new Texture("spr/pipe.png"));
       // pipe2 = new Sprite(new Texture("pipe.png"));

        pipe1 = new Sprite(new Texture("Spr/rock.png"));
        pipe2 = new Sprite(new Texture("Spr/rock.png"));
        paper = new Sprite(new Texture("Spr/paper.png"));
       // torn = new Sprite(new Texture("spr/torn.png"));

        paper.scale(1);



        pipe1.setPosition(x,y+DISTN_BETN_PIPE);
        pipe2.setPosition(x,y-DISTN_BETN_PIPE);
        paper.setPosition(x,y);

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.KinematicBody;

        bdef.position.set(pipe1.getX()/GameInfo.PPM,pipe1.getY()/GameInfo.PPM);
        body1 = world.createBody(bdef);

        bdef.position.set(pipe2.getX()/GameInfo.PPM,pipe2.getY()/GameInfo.PPM);
        body2 = world.createBody(bdef);

        bdef.position.set((pipe2.getX())/GameInfo.PPM,(pipe2.getY()+400)/GameInfo.PPM);
        score = world.createBody(bdef);

        //CircleShape c = new CircleShape();
        //c.setRadius(10/GameInfo.PPM);


        FixtureDef fdef = new FixtureDef();


        //fdef.shape = c;
        //score.createFixture(fdef);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox((pipe1.getWidth()/2)/GameInfo.PPM,(pipe1.getHeight()/2)/GameInfo.PPM);

        fdef.shape = shape;
        fdef.filter.categoryBits = GameInfo.PIPE_BIT;


        Fixture fixture1= body1.createFixture(fdef);
        fixture1.setUserData("Pipe");

        Fixture fixture2= body2.createFixture(fdef);
        fixture2.setUserData("Pipe");

       shape.setAsBox(3/GameInfo.PPM,(100)/GameInfo.PPM);
        fdef.shape = shape;
        fdef.isSensor = true;


        Fixture scoreFix =score.createFixture(fdef);
        scoreFix.setUserData("Score");

        shape.dispose();
    }
    //update pipe sprite to pipe body position
    public void updatePipes()
    {
        pipe1.setPosition((body1.getPosition().x*GameInfo.PPM),body1.getPosition().y *GameInfo.PPM);
        pipe2.setPosition((body2.getPosition().x*GameInfo.PPM),body2.getPosition().y *GameInfo.PPM);
        paper.setPosition(score.getPosition().x*GameInfo.PPM,score.getPosition().y * GameInfo.PPM-paper.getHeight()/2);
    }

    public void movePipes()
    {
        body1.setLinearVelocity(-2,0);
        body2.setLinearVelocity(-2,0);
        score.setLinearVelocity(-2,0);

        if(pipe1.getX()+ GameInfo.WIDTH<mainCamera.position.x)
        {
            body1.setActive(false);
            body2.setActive(false);
            score.setActive(false);
        }
    }

    public void stopPipes()
    {
        body1.setLinearVelocity(0,0);
        body2.setLinearVelocity(0,0);
        score.setLinearVelocity(0,0);
    }

    float getRandomY()
    {
        float max = GameInfo.HEIGHT/2f+ 200;
        float min = GameInfo.HEIGHT/2f -100;

        return random.nextFloat() *(max-min)+min;

    }

    public void drawPipes(SpriteBatch batch)
    {
        batch.draw(pipe1, pipe1.getX()-pipe1.getWidth()/2,pipe1.getY()-pipe1.getHeight()/2);
        batch.draw(pipe2, pipe2.getX()-pipe2.getWidth()/2,pipe2.getY()-pipe2.getHeight()/2);
        paper.draw(batch);
    }

    public void setMainCamera(OrthographicCamera mainCamera)
    {
        this.mainCamera = mainCamera;
    }

    public void dispose()
    {
        pipe2.getTexture().dispose();
        pipe1.getTexture().dispose();
        paper.getTexture().dispose();
    }


    public void cutPaper()
    {
        paper.setRegion(tornPaper);
    }

}
