package components;

import rose.GameObject;
import rose.MouseListener;
import rose.Window;
import util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
	
public class MouseControls extends Component {
	GameObject holding_object = null;
		
	public void pickupObject(GameObject go) {
		this.holding_object = go;
		Window.getScene().addGameObjectToScene(go);
	}
	
	public void place() {
		this.holding_object = null;
	}
	
	@Override
	public void update(float dt) {
		if (holding_object != null) {
			holding_object.transform.position.x = MouseListener.getOrthoX();
			holding_object.transform.position.y = MouseListener.getOrthoY();
			holding_object.transform.position.x = (int)(holding_object.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
			holding_object.transform.position.y = (int)(holding_object.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;
			
			if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
				place();
			}
		}
	}
}

