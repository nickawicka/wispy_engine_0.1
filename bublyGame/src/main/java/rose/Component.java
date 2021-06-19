package rose;

import imgui.ImGui;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {
	
	public transient GameObject game_object = null;
		
	public void start() {
		
	}
	
	public void update(float dt) {
		
	}
	
	public void imGui() {
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for(Field field : fields) {
				boolean is_transient = Modifier.isTransient(field.getModifiers());
				if (is_transient) {
					continue;
				}
				
				boolean is_private = Modifier.isPrivate(field.getModifiers());
				if (is_private) {
					field.setAccessible(true);
				}
				
				Class<?> type = field.getType();
				Object value = field.get(this);
				String name = field.getName();
				
				if (type == int.class) {
					int val = (int)value;
					int[] im_int = {val};
					if (ImGui.dragInt(name + ": ", im_int)) {
						field.set(this, im_int[0]);
					}
				} else if (type == float.class) {
					float val = (float)value;
					float[] im_float = {val};
					if (ImGui.dragFloat(name + ": ", im_float)) {
						field.set(this, im_float[0]);
					}
				} else if (type == boolean.class) {
					boolean val = (boolean)value;
					if (ImGui.checkbox(name + ": ", val)) {
						field.set(this, !val);
					}
				} else if (type == Vector3f.class) {
					Vector3f val = (Vector3f)value;
					float[] im_vec = {val.x, val.y, val.z};
					if (ImGui.dragFloat3(name + ": ", im_vec)) {
						val.set(im_vec[0], im_vec[1], im_vec[2]);
					}
				} else if (type == Vector4f.class) {
					Vector4f val = (Vector4f)value;
					float[] im_vec = {val.x, val.y, val.z, val.w};
					if (ImGui.dragFloat3(name + ": ", im_vec)) {
						val.set(im_vec[0], im_vec[1], im_vec[2], im_vec[3]);
					}
				}
				
				if (is_private) {
					field.setAccessible(false);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
