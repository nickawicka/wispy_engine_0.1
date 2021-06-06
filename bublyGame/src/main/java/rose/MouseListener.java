package rose;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
	private static MouseListener instance;
	private double scroll_x, scroll_y;
	private double x_pos, y_pos, last_y, last_x;
	private boolean mouse_button_press[] = new boolean[3]; // stores which button was last pressed
	private boolean is_dragging;
	
	private MouseListener() {
		this.scroll_x = 0.0;
		this.scroll_y = 0.0;
		this.x_pos = 0.0;
		this.y_pos = 0.0;
		this.last_x = 0.0;
		this.last_y = 0.0;
	}
	
	public static MouseListener getMouseListener() {
		if (MouseListener.instance == null) {
			MouseListener.instance = new MouseListener();
		}
		return MouseListener.instance;
	}
	
	public static void mousePosCallback(long window, double xpos, double ypos) {
		getMouseListener().last_x = getMouseListener().x_pos;
		getMouseListener().last_y = getMouseListener().y_pos;
		getMouseListener().x_pos = xpos;
		getMouseListener().y_pos = ypos;
		getMouseListener().is_dragging = getMouseListener().mouse_button_press[0] || 
										getMouseListener().mouse_button_press[1] || 
										getMouseListener().mouse_button_press[1];
	}
	
	public static void mouseButtonCallback(long window, int button, int action, int mods) {
		if (action == GLFW_PRESS) {
			if (button < getMouseListener().mouse_button_press.length) {
				getMouseListener().mouse_button_press[button] = true;
			}
		} else if (action == GLFW_RELEASE) {
			if (button < getMouseListener().mouse_button_press.length) {
				getMouseListener().mouse_button_press[button] = false;
				getMouseListener().is_dragging = false;
			}
		}
	}
	
	public static void mouseScrollCallback(long window, double x_offset, double y_offset) {
		getMouseListener().scroll_x = x_offset;
		getMouseListener().scroll_y = y_offset;
	}
	
	public static void endFrame() {
		getMouseListener().scroll_x = 0;
		getMouseListener().scroll_y = 0;
		getMouseListener().last_x = getMouseListener().x_pos;
		getMouseListener().last_y = getMouseListener().y_pos;
	}
	
	public static float getX() {
		return (float)getMouseListener().x_pos;
	}
	
	public static float getY() {
		return (float)getMouseListener().y_pos;
	}
	
	public static float getDx() {
		return (float)(getMouseListener().last_x - getMouseListener().x_pos);
	}
	
	public static float getDy() {
		return (float)(getMouseListener().last_y - getMouseListener().y_pos);
	}
	
	public static float getScrollX() {
		return (float)getMouseListener().scroll_x;
	}
	
	public static float getScrollY() {
		return (float)getMouseListener().scroll_y;
	}
	
	public static boolean isDragging() {
		return getMouseListener().is_dragging;
	}
	
	public static boolean mouseButtonDown(int button) {
		if (button < getMouseListener().mouse_button_press.length) {
			return getMouseListener().mouse_button_press[button];			
		} else {
			return false;
		}
	}
}
