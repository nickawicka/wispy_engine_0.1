package rose;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
	
	private String name;
	private List<Component> components;
	
	public GameObject(String name) {
		this.name = name;
		this.components = new ArrayList<>();
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

	public String getName() {
		return name;
	}
}
