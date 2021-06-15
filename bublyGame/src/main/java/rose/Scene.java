package rose;

import renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
	
	protected Renderer renderer = new Renderer();
	protected Camera camera;
	private boolean is_running = false;
	protected List<GameObject> game_objects = new ArrayList<>();

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
}
