package rose;

import components.FontRenderer;
import components.SpriteRenderer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import renderer.Shader;
import renderer.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;
import util.Time;

public class LevelEditorScene extends Scene {
	
	private float[] vertex_array = {
			// Position				// Color
			1000.0f, 0.0f, 0.0f,		1.0f, 0.0f, 0.0f, 1.0f,		1, 1, // Bottom right 0
			0.0f, 450.0f, 0.0f,		0.0f, 1.0f, 0.0f, 1.0f,		0, 0, // Top left		1
			1000.0f, 450.0f, 0.0f,	1.0f, 0.0f, 1.0f, 1.0f,		1, 0, // Top right	2
			0.0f, 0.0f, 0.0f,		1.0f, 1.0f, 0.0f, 1.0f, 	0, 1  // Bottom left	3
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
	private Texture test_texture;
	
	GameObject test_obj;
	private boolean first_time = false;
	
	public LevelEditorScene() {
		
	}
	
	@Override
	public void init() {	
		System.out.println("Creating 'test object'");
		this.test_obj = new GameObject("test object");
		this.test_obj.addComponent(new SpriteRenderer());
		this.test_obj.addComponent(new FontRenderer());
		this.addGameObjectToScene(this.test_obj);
		
		this.camera = new Camera(new Vector2f());
		default_shader = new Shader("assets/shaders/default.glsl");
		default_shader.compile();
		this.test_texture = new Texture("assets/images/test_image.png");
		
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
		int uv_size = 2;
		int vertex_size_bytes = (position_size + color_size + uv_size) * Float.BYTES;
		
		glVertexAttribPointer(0, position_size, GL_FLOAT, false, vertex_size_bytes, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, color_size, GL_FLOAT, false, vertex_size_bytes, position_size * Float.BYTES);
		glEnableVertexAttribArray(1);
		
		glVertexAttribPointer(2, uv_size, GL_FLOAT, false, vertex_size_bytes, (position_size + color_size) * Float.BYTES);
		glEnableVertexAttribArray(2);
	}
	
	@Override
	public void update (float dt) {
		camera.position.x -= dt * 50.0f;
		camera.position.y -= dt * 20.0f;
		
		default_shader.use();
		
		// Upload texture to shader
		default_shader.uploadTexture("TEX_SAMPLER", 0);
		glActiveTexture(GL_TEXTURE0);
		test_texture.bind();
		
		default_shader.uploadMat4f("u_proj_matrix", camera.getProjectionMatrix());
		default_shader.uploadMat4f("u_view_matrix", camera.getViewMatrix());
		default_shader.uploadFloat("u_time", Time.getTime());
		
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
		
		if (!first_time) {
			System.out.println("Creating game object!");
			GameObject go = new GameObject("Game Test 2");
			go.addComponent(new SpriteRenderer());
			this.addGameObjectToScene(go);
			first_time = true;
		}
		
		for (GameObject go : this.game_objects) {
			go.update(dt);
		}
	}
}
