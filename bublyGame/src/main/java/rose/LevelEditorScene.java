package rose;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import imgui.ImGui;
import components.Rigidbody;
import org.joml.Vector2f;

import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {
	
	private GameObject obj_1;
	private Spritesheet sprites;
	SpriteRenderer obj_1_sprite;

	public LevelEditorScene() {
		
	}
	
	@Override
	public void init() {	
		loadResources();
		this.camera = new Camera(new Vector2f(-250, 0));
		if (level_loaded) {
			this.active_game_object = game_objects.get(0);
			return;
		}
		
		sprites = AssetPool.getSpritesheet("assets/images/moonguy_rotate(2).png");		
		
		obj_1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 2);
		obj_1_sprite = new SpriteRenderer();
		obj_1_sprite.setSprite(sprites.getSprite(0));
		//obj_1_sprite.setColor(new Vector4f(1, 0, 0, 1));
		obj_1.addComponent(obj_1_sprite);
		obj_1.addComponent(new Rigidbody());
		this.addGameObjectToScene(obj_1);
		this.active_game_object = obj_1;
		
		GameObject obj_2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 3);
		SpriteRenderer obj_2_sprite_renderer = new SpriteRenderer();
		Sprite obj_2_sprite = new Sprite();
		obj_2_sprite.setTexture(AssetPool.getTexture("assets/images/test_image.png"));
		obj_2_sprite_renderer.setSprite(obj_2_sprite);
		obj_2.addComponent(obj_2_sprite_renderer);
		this.addGameObjectToScene(obj_2);	
		
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Component.class, new ComponentDeserializer())
				.registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
				.create();
		String serialized = gson.toJson(obj_1);
		System.out.println(serialized);
		GameObject obj = gson.fromJson(serialized, GameObject.class);
		System.out.println(obj);
	}
	
	private void loadResources() {
		AssetPool.getShader("assets/shaders/default.glsl");
		
		AssetPool.addSpriteSheet("assets/images/moonguy_rotate(2).png", 
				new Spritesheet(AssetPool.getTexture("assets/images/moonguy_rotate(2).png"), 
						44, 64, 8, 0));
		AssetPool.getTexture("assets/images/test_image.png");
	}
	
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
		
		for (GameObject go : this.game_objects) {
			go.update(dt);
		}
		
		this.renderer.render();
	}
	
	@Override
	public void imGui() {
		ImGui.begin("Test Window");
		ImGui.text("Some random text");
		ImGui.end();
	}
}
