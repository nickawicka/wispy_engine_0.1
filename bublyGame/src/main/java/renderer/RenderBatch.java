package renderer;

import components.SpriteRenderer;
import rose.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
	// Vertex Properties
	// ======
	// Position				Color							tex coords		tex id
	// float, float,		float, float, float, float		float, float	float
	
	private final int POS_SIZE = 2;
	private final int COLOR_SIZE = 4;
	
	private final int TEX_COORDS_SIZE = 2;
	private final int TEX_ID_SIZE = 1;
	
	private final int POS_OFFSET = 0;
	private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
	private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
	private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
	private final int VERTEX_SIZE = 9;
	private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
	
	private SpriteRenderer[] sprites;
	private int num_sprites;
	private boolean has_room;
	private float[] vertices;
	private int[] tex_slots = {0, 1, 2, 3, 4, 5, 6, 7};
	
	private List<Texture> textures;
	private int vao_id, vbo_id;
	private int max_batch_size;
	private Shader shader;
	
	public RenderBatch(int max_batch_size) {
		shader = AssetPool.getShader("assets/shaders/default.glsl");
		this.sprites = new SpriteRenderer[max_batch_size];
		this.max_batch_size = max_batch_size;
		
		// Four quad vertices
		vertices = new float[max_batch_size * 4 * VERTEX_SIZE];
		
		this.num_sprites = 0;
		this.has_room = true;
		this.textures = new ArrayList<>();
	}
	
	public void start() {
		// Generate and bind a Vertex Array Object
		vao_id = glGenVertexArrays();
		glBindVertexArray(vao_id);
		
		// Allocate space for vertices
		vbo_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
		glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
		
		// Create and upload indices buffer
		int ebo_id = glGenBuffers();
		int[] indices = generateIndices();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo_id);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		// Enable the buffer attribute pointers
		glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
		glEnableVertexAttribArray(1);
		
		glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
		glEnableVertexAttribArray(2);
		
		glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
		glEnableVertexAttribArray(3);
	}
	
	public void addSprite(SpriteRenderer sprite) {
		// Get index and add render_object
		int index = this.num_sprites;
		this.sprites[index] = sprite;
		this.num_sprites++;
		
		if (sprite.getTexture() != null) {
			if (!textures.add(sprite.getTexture())) {
				textures.add(sprite.getTexture());
			}
		}
		
		// Add properties to local vertices array
		loadVertexProperties(index);
		
		if (num_sprites >= this.max_batch_size) {
			this.has_room = false;
		}
	}
	
	public void render() {
		// Re-buffer all data every frame
		glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
		glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
		
		// Use shader
		shader.use();
		shader.uploadMat4f("u_proj_matrix", Window.getScene().camera().getProjectionMatrix());
		shader.uploadMat4f("u_view_matrix", Window.getScene().camera().getViewMatrix());
		
		for (int i = 0; i < textures.size(); i++) {
			glActiveTexture(GL_TEXTURE0 + i + 1);
			textures.get(i).bind();
		}
		shader.uploadIntArray("u_textures", tex_slots);
		
		glBindVertexArray(vao_id);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(GL_TRIANGLES, this.num_sprites * 6, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		
		for (int i = 0; i < textures.size(); i++) {
			textures.get(i).unbind();
		}
		
		shader.detach();
	}
	
	private void loadVertexProperties(int index) {
		SpriteRenderer sprite = this.sprites[index];
		
		// Find offset within array (4 vertices per sprite)
		int offset = index * 4 * VERTEX_SIZE;
		
		Vector4f color = sprite.getColor();
		Vector2f[] tex_coords = sprite.getTexCoords();
		
		int tex_id = 0;
		if (sprite.getTexture() != null) {
			for (int i = 0; i < textures.size(); i++) {
				if (textures.get(i) == sprite.getTexture()) {
					tex_id = i + 1;
					break;
				}
			}
		}
		
		// Add vertices with the appropriate properties
		float x_add = 1.0f;
		float y_add = 1.0f;
		for (int i = 0; i < 4; i++) {
			if (i == 1) {
				y_add = 0.0f;
			} else if (i == 2) {
				x_add = 0.0f;
			} else if (i == 3) {
				y_add = 1.0f;
			}
			
			// Load position
			vertices[offset] = sprite.game_object.transform.position.x + (x_add * sprite.game_object.transform.scale.x);
			vertices[offset + 1] = sprite.game_object.transform.position.y + (y_add * sprite.game_object.transform.scale.y);
		
			// Load color
			vertices[offset + 2] = color.x;
			vertices[offset + 3] = color.y;
			vertices[offset + 4] = color.z;
			vertices[offset + 5] = color.w;
			
			// Load texture coordinates
			vertices[offset + 6] = tex_coords[i].x;
			vertices[offset + 7] = tex_coords[i].y;
			
			// Load texture id
			vertices[offset + 8] = tex_id;
			
			offset += VERTEX_SIZE;
		}		
	}
	
	private int[] generateIndices() {
		// 6 indices per quad (3 per triangle)
		int[] elements = new int[6 * max_batch_size];
		for (int i = 0; i < max_batch_size; i++) {
			loadElementIndices(elements, i);
		}
		return elements;
	}
	
	private void loadElementIndices(int[] elements, int index) {
		int offset_array_index = 6 * index;
		int offset = 4 * index;
		
		// 3, 2, 0, 0, 2, 1			7, 6, 4, 4, 6, 5
		// Triangle 1
		elements[offset_array_index + 0] = offset + 3;
		elements[offset_array_index + 1] = offset + 2;
		elements[offset_array_index + 2] = offset + 0;
		
		elements[offset_array_index + 3] = offset + 0;
		elements[offset_array_index + 4] = offset + 2;
		elements[offset_array_index + 5] = offset + 1;
	}
	
	public boolean hasRoom() {
		return this.has_room;
	}
	
	public boolean hasTextureRoom() {
		return this.textures.size() < 8;
	}
	
	public boolean hasTexture(Texture tex) {
		return this.textures.contains(tex);
	}
	
}
