package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import imgui.ImGui;
import imgui.ImVec2;
import rose.Camera;
import rose.GameObject;
import rose.GameObjectDeserializer;
import rose.Prefabs;
import rose.Transform;
import components.Component;
import components.ComponentDeserializer;
import components.MouseControls;
import components.Rigidbody;
import org.joml.Vector2f;

import util.AssetPool;

public class LevelEditorScene extends Scene {
	
	private GameObject obj_1;
	private Spritesheet sprites;
	SpriteRenderer obj_1_sprite;
	
	MouseControls mouse_controls = new MouseControls();

	public LevelEditorScene() {
		
	}
	
	@Override
	public void init() {	
		loadResources();
		this.camera = new Camera(new Vector2f(-250, 0));
		sprites = AssetPool.getSpritesheet("assets/images/moonguy_rotate(2).png");
		if (level_loaded) {
			this.active_game_object = game_objects.get(0);
			return;
		}			
		
		obj_1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 2);
		obj_1_sprite = new SpriteRenderer();
		obj_1_sprite.setSprite(sprites.getSprite(0));
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
		//System.out.println("FPS: " + (1.0f / dt));
		mouse_controls.update(dt);
		
		for (GameObject go : this.game_objects) {
			go.update(dt);
		}
		
		this.renderer.render();
	}
	
	@Override
	public void imGui() {
		ImGui.begin("Test window");
		
		ImVec2 window_pos = new ImVec2();
		ImGui.getWindowPos(window_pos);
		ImVec2 window_size = new ImVec2();
		ImGui.getWindowSize(window_size);
		ImVec2 item_spacing = new ImVec2();
		ImGui.getStyle().getItemSpacing(item_spacing);
		
		float window_x2 = window_pos.x + window_size.x;
		for (int i = 0; i < sprites.size(); i++) {
			Sprite sprite = sprites.getSprite(i);
			float sprite_width = sprite.getWidth() * 4;
			float sprite_height = sprite.getHeight() * 4;
			int id = sprite.getTexId();
			Vector2f[] tex_coords= sprite.getTexCoords();
			
			ImGui.pushID(i);
			if (ImGui.imageButton(id, sprite_width, sprite_height, tex_coords[2].x, tex_coords[0].y, tex_coords[0].x, tex_coords[2].y)) {
				GameObject object = Prefabs.generateSpriteObject(sprite, sprite_width, sprite_height);
				mouse_controls.pickupObject(object);
			}
			ImGui.popID();
			
			ImVec2 last_button_pos = new ImVec2();
			ImGui.getItemRectMax(last_button_pos);
			float last_button_x2 = last_button_pos.x;
			float next_button_x2 = last_button_x2 + item_spacing.x + sprite_width;
			if (i + 1 < sprites.size() && next_button_x2 < window_x2) {
				ImGui.sameLine();
			}
		}
		
		ImGui.end();
	}
}
