package gamergames.puzzle1.objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;


public abstract class Object {
    protected Vector2 position;
    protected Vector2 velocity;

    int forwardSpeed = 200;
    float rotationSpeed = 100f;
    int forwardState;
    int rotationState;
    private float rotation;
    static final float rotationOffset = -90f;
    private float textureRotation;

    private Texture texture;

    private int SizeX;
    private int SizeY;

    public Object(float px, float py, String textureName){
        position = new Vector2(px, py);
        velocity = new Vector2(0, 0);
        rotation = 90f;
        textureRotation = rotation + rotationOffset;
        forwardState = 0;
        rotationState = 0;
        texture = new Texture(textureName);
        SizeX = texture.getWidth();
        SizeY = texture.getHeight();
    }

    public void update(float dt){
        velocity = new Vector2(0, 0);
        rotation += rotationState * rotationSpeed * dt;
        if(rotation >= 360){ /// This might not be necessary
            rotation -= 360;
        }if(rotation < 0){
            rotation += 360;
        }
        textureRotation = rotation + rotationOffset;
        if(forwardState == 1){
            velocity.x = (float) (cos(toRadians(rotation)) * forwardSpeed * dt);
            velocity.y = (float) (sin(toRadians(rotation)) * forwardSpeed * dt);
        }
        position.add(velocity);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setForwardState(int forwardState) {
        this.forwardState = forwardState;
    }

    public void setRotationState(int rotationState) {
        this.rotationState = rotationState;
    }

    public float getTextureRotation() {
        return textureRotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }

    public void setForwardSpeed(int forwardSpeed) {
        this.forwardSpeed = forwardSpeed;
    }

    public int getSizeX() {
        return SizeX;
    }

    public void setSizeX(int sizeX) {
        SizeX = sizeX;
    }

    public int getSizeY() {
        return SizeY;
    }

    public void setSizeY(int sizeY) {
        SizeY = sizeY;
    }
}
