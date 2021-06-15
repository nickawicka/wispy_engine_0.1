package components;

import org.joml.Vector2f;
import renderer.Texture;

public class Sprite {
	
	private Texture texture;
	private Vector2f[] tex_coords;
	
	public Sprite(Texture texture) {
		this.texture = texture;
		Vector2f[] tex_coords = {
				new Vector2f(1, 1),
				new Vector2f(1, 0),
				new Vector2f(0, 0),
				new Vector2f(0, 1)
		};
		this.tex_coords = tex_coords;
	}
	
	public Sprite(Texture texture, Vector2f[] tex_coords) {
		this.texture = texture;
		this.tex_coords = tex_coords;
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	
	public Vector2f[] getTexCoords() {
		return this.tex_coords;
	}
	
}
