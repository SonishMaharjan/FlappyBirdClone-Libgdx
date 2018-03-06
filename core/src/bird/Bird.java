package bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import helpers.GameInfo;
import helpers.GameManager;

public class Bird extends Sprite {
    private World world;
    private Body body;

    private boolean isAlive;

    private Texture deadBird;

    private TextureAtlas birdAtlas;
    private Animation<TextureRegion> birdAnimation;
    private float elaspedTime;

    Sprite currentFrame;//for getting present animation sprite
    Sprite previousFrame;
    TextureRegion currentFrameRegion;

    float angleOfRotation;

    public Bird(World world,float x,float y)
    {
       // super(new Texture("Birds/"+ GameManager.getInstance().getBird()+"/Idle.png"));
        super(new Texture("Spr/alive.png"));


        deadBird = new Texture("Spr/dead.png");

        this.world = world;
        setPosition(x,y);
        createBody();
        createAnimation();

        previousFrame = new Sprite();
        currentFrame =new Sprite();
        currentFrameRegion = new TextureRegion();


    }

    void createBody()
    {
        BodyDef bdef= new BodyDef();
        bdef.type =BodyDef.BodyType.DynamicBody;
        bdef.position.set(getX()/ GameInfo.PPM,getY()/GameInfo.PPM);

        body = world.createBody(bdef);

        CircleShape shape= new CircleShape();
        shape.setRadius((getHeight()/2)/GameInfo.PPM);

        FixtureDef fdef= new FixtureDef();
        fdef.shape = shape;
        fdef.density= 1;

        Fixture fixture= body.createFixture(fdef);
        fixture.setUserData("Bird");
        shape.dispose();

        body.setActive(false);

    }

    void createAnimation()
    {

        birdAtlas = new TextureAtlas("Spr/scissor.atlas");
        birdAnimation = new Animation(1/3f,birdAtlas.getRegions());
    }

    public void drawAnimateBird(SpriteBatch batch)
    {

        if(isAlive)
        {
            elaspedTime += Gdx.graphics.getDeltaTime();
            currentFrameRegion.setRegion(birdAnimation.getKeyFrame(elaspedTime,true));
            currentFrame.setRegion(currentFrameRegion);
            currentFrame.setSize(currentFrameRegion.getRegionWidth(), currentFrameRegion.getRegionHeight());
            currentFrame.setOriginCenter();
            if(body.getLinearVelocity().y<0){
                currentFrame.setRotation(currentFrame.getRotation() - 1);
                if(currentFrame.getRotation()<-10)
                {
                    currentFrame.setRotation(-10);
                }
            }
            if(Gdx.input.justTouched())
            {
                currentFrame.setRotation(currentFrame.getRotation() + 7);
                if(Math.abs(currentFrame.getRotation())>45)
                {
                    currentFrame.setRotation(45);
                }

            }
            currentFrame.setPosition(body.getPosition().x*GameInfo.PPM-currentFrame.getWidth()/2,body.getPosition().y*GameInfo.PPM-currentFrame.getHeight()/2);
           // s.setOrigin(s.getWidth()/2,s.getHeight()/2);


            currentFrame.draw(batch);

        }

    }

    public void birdFlap()
    {
        if(body.getLinearVelocity().y>=0) {
            body.setLinearVelocity(0, body.getLinearVelocity().y+4f);
        }
        else
        {
            body.setLinearVelocity(0, 4.5f);
        }
        //  body.applyLinearImpulse(new Vector2(0,1.5f),body.getWorldCenter(),true);

    }


    public void drawIdle(SpriteBatch batch)
    {   if(!isAlive)
        {
            draw(batch);
        }

      //  draw(batch);
    }

    public void updateBird()
    {
        setPosition((body.getPosition().x *GameInfo.PPM)-getWidth()/2,(body.getPosition().y *GameInfo.PPM)-getHeight()/2);
    }


    public void setIsAlive(boolean isAlive)
    {
        this.isAlive = isAlive;
    }

    public boolean getIsAlive()
    {
        return isAlive;
    }

    public void activateBird()
    {
        isAlive = true;
        body.setActive(true);
    }



    public void birdDied()
    {

        setTexture(deadBird);
        //setOriginCenter();
        //setRotation(180f);
    }

    public void dispose()
    {
        deadBird.dispose();
        birdAtlas.dispose();

    }

}
