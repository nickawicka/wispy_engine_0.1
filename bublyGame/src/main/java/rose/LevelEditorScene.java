package rose;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {
	
	private GameObject obj_1;
	private Spritesheet sprites;

	public LevelEditorScene() {
		
	}
	
	@Override
	public void init() {	
		loadResources();
		this.camera = new Camera(new Vector2f(-250, 0));
		
		sprites = AssetPool.getSpritesheet("assets/images/moonguy_rotate(2).png");		
		
		obj_1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 2);
		obj_1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
		this.addGameObjectToScene(obj_1);
		
		GameObject obj_2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 3);
		obj_2.addComponent(new SpriteRenderer(sprites.getSprite(5)));
		//this.addGameObjectToScene(obj_2);
	}
	
	private void loadResources() {
		AssetPool.getShader("assets/shaders/default.glsl");
		
		AssetPool.addSpriteSheet("assets/images/moonguy_rotate(2).png", 
				new Spritesheet(AssetPool.getTexture("assets/images/moonguy_rotate(2).png"), 
						44, 64, 8, 0));		
	}
	
	private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;
	
	@Override
	public void update(float dt) {
		System.out.println("FPS: " + (1.0f / dt));
		
		if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
			camera.position.x += 100f * dt;
		} else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
			camera.position.x -= 100f * dt;
		}
		if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
			camera.position.y += 100f * dt;
		} else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
			camera.position.y -= 100f * dt;
		}
		
		spriteFlipTimeLeft -= dt;
        if (spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 7) {
                spriteIndex = 0;
            }
            obj_1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }
		
		for (GameObject go : this.game_objects) {
			go.update(dt);
		}
		
		this.renderer.render();
	}
}
