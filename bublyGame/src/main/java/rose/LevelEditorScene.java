package rose;

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
		
		sprites = AssetPool.getSpritesheet("assets/images/test_image.png");		
		
		obj_1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
		obj_1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
		this.addGameObjectToScene(obj_1);
		
		GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
		obj2.addComponent(new SpriteRenderer(sprites.getSprite(1)));
		this.addGameObjectToScene(obj2);
	}
	
	private void loadResources() {
		AssetPool.getShader("assets/shaders/default.glsl");
		
		AssetPool.addSpriteSheet("assets/images/test_image.png", 
				new Spritesheet(AssetPool.getTexture("assets/images/test_image.png"), 
						1000, 2000, 2, 0));		
	}
	
	private int sprite_index = 0;
	private float sprite_flip_time = 0.2f;
	private float sprite_flip_time_left = 0.0f;
	
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
		
		sprite_flip_time_left -= dt;
		if (sprite_flip_time_left <= 0) {
			sprite_flip_time_left = sprite_flip_time;
			sprite_index++;
			if (sprite_index > 4) {
				sprite_index = 0;
			}
		}
		
		for (GameObject go : this.game_objects) {
			go.update(dt);
		}
		
		this.renderer.render();
	}
}
