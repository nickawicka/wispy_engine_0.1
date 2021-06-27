package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import components.Component;
import components.ComponentDeserializer;
import renderer.Renderer;
import rose.Camera;
import rose.GameObject;
import rose.GameObjectDeserializer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import imgui.ImGui;

public abstract class Scene {
	
	protected Renderer renderer = new Renderer();
	protected Camera camera;
	private boolean is_running = false;
	protected List<GameObject> game_objects = new ArrayList<>();
	protected GameObject active_game_object = null;
	protected boolean level_loaded = false;
	
	public Scene() {
		
	}
	
	public void init() {
		
	}
	
	public void start() {
		for (GameObject go: game_objects) {
			go.start();
			this.renderer.addGameObject(go);
		}
		is_running = true;
	}
	
	public void addGameObjectToScene(GameObject go) {
		if (!is_running) {
			game_objects.add(go);
		} else {
			game_objects.add(go);
			go.start();
			this.renderer.addGameObject(go);
		}
	}
	
	public abstract void update(float delta);
	
	public Camera camera() {
		return this.camera;
	}
	
	public void sceneImGui() {
		if (active_game_object != null) {
			ImGui.begin("Inspector");
			active_game_object.imGui();
			ImGui.end();
		}
		imGui();
	}
	
	public void imGui() {
		
	}
	
	public void saveExit() {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Component.class, new ComponentDeserializer())
				.registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
				.create();
		
		try {
			FileWriter writer = new FileWriter("level.txt");
			writer.write(gson.toJson(this.game_objects));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Component.class, new ComponentDeserializer())
				.registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
				.create();
		
		String in_file = "";
		try {
			in_file = new String(Files.readAllBytes(Paths.get("level.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (!in_file.equals("")) {			
			int max_go_id = -1;
			int max_comp_id = -1;
			GameObject[] objs = gson.fromJson(in_file, GameObject[].class);
			for (int i = 0; i < objs.length; i++) {
				addGameObjectToScene(objs[i]);
				
				for (Component c : objs[i].getAllComponents()) {
					if (c.getUid() > max_comp_id) {
						max_comp_id = c.getUid();//
					}
				}
				if (objs[i].getUid() > max_go_id) {
					max_go_id = objs[i].getUid();
				}
			}			
			
			max_go_id++;
			max_comp_id++;
			System.out.println(max_go_id);
			System.out.println(max_comp_id);			
			GameObject.init(max_go_id);
			Component.init(max_comp_id);
			this.level_loaded = true;
		}
	}
}
