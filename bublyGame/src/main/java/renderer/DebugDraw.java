package renderer;

import rose.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw {
	private static int MAX_LINES = 500;
	
	private static List<Line2D> lines = new ArrayList<>();
	// 6 floats per vertex, 2 vertices per line
	private static float[] vertex_array = new float[MAX_LINES * 6 * 2];
	private static Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");
	
	private static int vao_id;
	private static int vbo_id;
	
	private static boolean started = false;
	
	public static void start() {
		// Generate the vao
		vao_id = glGenVertexArrays();
		glBindVertexArray(vao_id);
		
		// create the vbo and buffer some memory
		vbo_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
		glBufferData(GL_ARRAY_BUFFER, vertex_array.length * Float.BYTES, GL_DYNAMIC_DRAW);
		
		// Enable the vertex array attributes
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
		glEnableVertexAttribArray(1);
		
		glLineWidth(2.0f);
	}

	public static void beginFrame() {
		if (!started) {
			start();
			started = true;
		}
		
		// remove dead lines
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).beginFrame() < 0) {
				lines.remove(i);
				i--;
			}
		}
	}
	
	public static void draw() {
		if (lines.size() <= 0) return;
		
		int index = 0;
		for (Line2D line : lines) {
			for (int i = 0; i < 2; i++) {
				Vector2f position = i == 0 ? line.getFrom() : line.getTo();
				Vector3f color = line.getColor();
				
				// Load position
				vertex_array[index] = position.x;
				vertex_array[index + 1] = position.y;
				vertex_array[index + 2] = -10.0f;
				
				// Load the color
				vertex_array[index + 3] = color.x;
				vertex_array[index + 4] = color.y;
				vertex_array[index + 5] = color.z;
				index += 6;
			}
		}
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
		glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertex_array, 0, lines.size() * 6 * 2));
	
		// Use the shader
		shader.use();
		shader.uploadMat4f("u_proj_matrix", Window.getScene().camera().getProjectionMatrix());
		shader.uploadMat4f("u_view_matrix", Window.getScene().camera().getViewMatrix());
		
		// Bind the vao
		glBindVertexArray(vao_id);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		// Draw the batch
		glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2);
		
		// Disable location
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		
		// Unbind shader
		shader.detach();
	}
	
	// ===========================================================
	// Add line2D methods
	// ===========================================================	
	public static void addLine2D(Vector2f from, Vector2f to) {
		// TODO: ADD CONSTANTS FOR COMMON COLORS
		addLine2D(from, to, new Vector3f(0, 1, 0), 1);
	}
	
	public static void addLine2D(Vector2f from, Vector2f to, Vector3f color) {
		addLine2D(from, to, color, 1);
	}
	
	public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
		if (lines.size() >= MAX_LINES) return;
		DebugDraw.lines.add(new Line2D(from, to, color, lifetime));
	}
}
