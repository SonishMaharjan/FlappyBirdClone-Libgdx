package ground;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import helpers.GameInfo;

public class GroundBody {

    private World world;
    private Body body;

    public GroundBody(World world,Sprite ground)
    {
        this.world = world;
        createGroundBody(ground);

    }

    void createGroundBody(Sprite ground)
    {

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((ground.getX()+ground.getWidth()/2)/GameInfo.PPM,(ground.getY()+ground.getHeight()/2)/ GameInfo.PPM);

        body = world.createBody(bdef);

        FixtureDef fdef= new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((ground.getWidth()/2)/GameInfo.PPM,(ground.getHeight()/2)/GameInfo.PPM);

        fdef.shape = shape;
        fdef.filter.categoryBits = GameInfo.GROUND_BIT;

        Fixture fixture= body.createFixture(fdef);
        fixture.setUserData("Ground");

        shape.dispose();

    }

}
