package rose;

import java.util.ArrayList;
import java.util.List;

import components.Component;

public class GameObject {
	
	private static int ID_COUNTER = 0;
	private int uid = -1;
	
	private String name;
	private List<Component> components;
	public Transform transform;
	private int z_index;
	
	public GameObject(String name, Transform transform, int z_index) {
		this.name = name;
		this.z_index = z_index;
		this.components = new ArrayList<>();
		this.transform = transform;
		
		this.uid = ID_COUNTER++;
	}

	public <T extends Component> T getComponent(Class<T> component_class) {
		for (Component c : components) {
			if (component_class.isAssignableFrom(c.getClass())) {
				try {
					return component_class.cast(c);
				} catch (ClassCastException e) {
					e.printStackTrace();
					assert false : "Error: Casting component.";
				}
			}
		}
		return null;
	}
	
	public <T extends Component> void removeComponent(Class<T> component_class) {
		for (int i = 0; i < components.size(); i++) {
			Component c = components.get(i);
			if (component_class.isAssignableFrom(c.getClass())) {
				components.remove(i);
				return;
			}
		}
	}
	
	public void addComponent(Component c) {
		c.generateID();
		this.components.add(c);
		c.game_object = this;
	}
	
	public void update(float dt) {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).update(dt);
		}
	}
	
	public void start() {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).start();
		}
	}
	
	public void imGui() {
		for (Component c : components) {
			c.imGui();
		}
	}
	
	public int getZIndex() {
		return this.z_index;
	}
	
	public int getUid() {
		return this.uid;
	}
	
	public static void init(int max_id) {
		ID_COUNTER = max_id;
	}
	
	public List<Component> getAllComponents() {
		return this.components;
	}

	public String getName() {
		return name;
	}
}
