package rose;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import renderer.DebugDraw;
import scenes.LevelEditorScene;
import scenes.LevelScene;
import scenes.Scene;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWVidMode;

public class Window {
	//public static final int WINDOW_WIDTH = 1000, WINDOW_HEIGHT = 500;
	private int width, height;
	private static final boolean START_FULLSCREEN = false;	
	
	private static Window window = null;
	private static Scene current_scene;

	private boolean remain_open = true;
	
	private long glfwWindow;
	private ImGuiLayer imgui_layer;
	
	public float r, g, b, a;
	
	private Window() {
		this.width = 1600;
		this.height = 900;
		r = 1;
		b = 1;
		g = 0;
		a = 1;
	}
	
	public static void changeScene(int new_scene) {
		switch(new_scene) {
			case 0:
				current_scene = new LevelEditorScene();
				break;
			case 1:
				current_scene = new LevelScene();
				break;
			default:
				assert false : "Unknown scene '" + new_scene + "'";
				break;
		}
		
		current_scene.load();
		current_scene.init();
		current_scene.start();
	}
	
	public static Window getWindow() {
		if (Window.window == null) {
			Window.window = new Window();
		}		
		return Window.window;
	}
	
	public static Scene getScene() {
		getWindow();
		return Window.current_scene;
	}
	
	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");		
		init();
		loop();
		deinit();
	}
	
	/*
	 * Initialize the program and glfw components
	 */	
    public void init() {    	
    	// Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();
        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }
        
        // Set re-sizable
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        // Request an OpenGL 3.3 Core context.
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); 
        
        int window_width = this.width;
        int window_height = this.height;
        long monitor = 0;   
        
        monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vid_mode = glfwGetVideoMode(monitor);
        
        if(START_FULLSCREEN) {
            // Retrieve the desktop resolution        	
            window_width = vid_mode.width();
            window_height = vid_mode.height();
        } else {
        	monitor = 0;
        }
        
        glfwWindow = glfwCreateWindow(window_width, window_height, "Game - LWJGL3", monitor, 0);       
        if(glfwWindow == 0) {
            throw new RuntimeException("Failed to create window");
        }
        
        if (!START_FULLSCREEN) {
			glfwSetWindowPos(
					glfwWindow,
    				(vid_mode.width() - window_width) / 2,
    				(vid_mode.height() - window_height) / 2
    			);        	
        } 
        
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, new_width, new_height) -> {
        	Window.setWidth(new_width);
        	Window.setHeight(new_height);
        });
        
        // Make this window's context the current on this thread.
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);
        // Let LWJGL know to use this current context.
        GL.createCapabilities();
        // Make this window visible.
        glfwShowWindow(glfwWindow);
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        this.imgui_layer = new ImGuiLayer(glfwWindow);
        this.imgui_layer.initImGui();
        
        Window.changeScene(0);
    }
    
    /**
     * Releases game resources and window.
     */
    public void deinit() {
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);   
        glfwTerminate();
        //this.imgui_layer.
        System.out.println("Closed Succesfully!");
    }
    
    public void loop() {    	
    	float begin_time = (float)glfwGetTime();
    	float end_time;
    	float delta = -1.0f;
   	
        // Continue whilst no close request from internal nor external.
        while(!glfwWindowShouldClose(glfwWindow) && remain_open) {            
            // Polls input and swap frame buffers.
            glfwPollEvents();
            
            DebugDraw.beginFrame();
            
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);
            
            if (delta >= 0) {
            	DebugDraw.draw();
            	current_scene.update(delta);
            }
            
            this.imgui_layer.update(delta, current_scene);
            glfwSwapBuffers(glfwWindow);
            
            end_time = (float)glfwGetTime();
            delta = end_time - begin_time;
            begin_time = end_time;            
        }
        current_scene.saveExit();
    }
    
    public static int getWidth() {
        return getWindow().width;
    }

    public static int getHeight() {
        return getWindow().height;
    }

    public static void setWidth(int newWidth) {
    	getWindow().width = newWidth;
    }

    public static void setHeight(int newHeight) {
    	getWindow().height = newHeight;
    }
}
