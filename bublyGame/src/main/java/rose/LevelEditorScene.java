package rose;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {
	
	private float[] vertex_array = {
			// Position				// Color
			50.5f, -50.5f, 0.0f,		1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
			-50.5f, 50.5f, 0.0f,		0.0f, 1.0f, 0.0f, 1.0f, // Top left		1
			50.5f, 50.5f, 0.0f,		0.0f, 0.0f, 1.0f, 1.0f, // Top right	2
			-50.5f, -50.5f, 0.0f,		1.0f, 1.0f, 0.0f, 1.0f, // Bottom left	3
	};
	
	private int[] element_array = {
			/*
			 * 		x		x
			 * 
			 * 
			 * 
			 * 		x		x
			 * 
			 */
			2, 1, 0, // Top right triangle
			0, 1, 3 // Bottom right triangle
	};
	
	private int vao_id, vbo_id, ebo_id;
	private Shader default_shader;
	
	public LevelEditorScene() {
		
	}
	
	@Override
	public void init() {	
		this.camera = new Camera(new Vector2f());
		default_shader = new Shader("assets/shaders/default.glsl");
		default_shader.compile();
		
		/*
		 *  Generate VAO, VBO, and EBO buffer objects, and send to GPU
		 */		
		vao_id = glGenVertexArrays();
		glBindVertexArray(vao_id);
		
		// Create a float buffer of vertices
		FloatBuffer vertex_buffer = BufferUtils.createFloatBuffer(vertex_array.length);
		vertex_buffer.put(vertex_array).flip();
		
		// Create VBO upload and vertex buffer		
		vbo_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
		glBufferData(GL_ARRAY_BUFFER, vertex_buffer, GL_STATIC_DRAW);
		
		// Create the indices and upload
		IntBuffer element_buffer = BufferUtils.createIntBuffer(element_array.length);
		element_buffer.put(element_array).flip();
		
		ebo_id = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo_id);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, element_buffer, GL_STATIC_DRAW);
		
		// Add the vertex attribute pointers
		int position_size = 3;
		int color_size = 4;
		int float_size_bytes = 4;
		int vertex_size_bytes = (position_size + color_size) * float_size_bytes;
		glVertexAttribPointer(0, position_size, GL_FLOAT, false, vertex_size_bytes, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, color_size, GL_FLOAT, false, vertex_size_bytes, position_size * float_size_bytes);
		glEnableVertexAttribArray(1);
	}
	
	@Override
	public void update (float dt) {
		default_shader.use();
		default_shader.uploadMat4f("u_proj_matrix", camera.getProjectionMatrix());
		default_shader.uploadMat4f("u_view_matrix", camera.getViewMatrix());
		
		// Bind the VAO that we're using
		glBindVertexArray(vao_id);		
		
		// Enable the vertex attribute pointers
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(GL_TRIANGLES, element_array.length, GL_UNSIGNED_INT, 0);
		
		// Unbind everything
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		
		default_shader.detach();	
	}
}
