package rose;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
	private static KeyListener instance;
	private boolean key_pressed[] = new boolean[350];
	
	private KeyListener() {
		
	}
	
	public static KeyListener getKeyListener() {
		if (KeyListener.instance == null)  {
			KeyListener.instance = new KeyListener();
		}
		return KeyListener.instance;
	}

	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS) {
			getKeyListener().key_pressed[key] = true;
		} else if (action == GLFW_RELEASE) {
			getKeyListener().key_pressed[key] = false;
		}
	}
	
	public static boolean isKeyPressed(int key_code) {
		return getKeyListener().key_pressed[key_code];
	}
}
