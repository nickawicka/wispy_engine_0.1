package components;

import org.joml.Vector2f;
import renderer.Texture;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
	
	private Texture texture;
	private List<Sprite> sprites;
	
	public Spritesheet(Texture texture, int sprite_width, int sprite_height, int num_sprites, int spacing) {
		this.sprites = new ArrayList<>();
		
		this.texture = texture;
		int current_x = 0;
		int current_y = texture.getHeight() - sprite_height;
		for (int i = 0; i < num_sprites; i++) {
			float top_y = (current_y + sprite_height) / (float)texture.getHeight();
			float right_x = (current_x + sprite_width) / (float)texture.getWidth();
			float left_x = current_x / (float)texture.getWidth();
			float bottom_y = current_y / (float)texture.getHeight();
			
			Vector2f[] tex_coords =  {
					new Vector2f(right_x, top_y),
					new Vector2f(right_x, bottom_y),
					new Vector2f(left_x, bottom_y),
					new Vector2f(left_x, top_y)
			};
			
			//Sprite sprite = new Sprite(this.texture, tex_coords);
			Sprite sprite = new Sprite();
			sprite.setTexture(this.texture);
			sprite.setTexCoords(tex_coords);
			this.sprites.add(sprite);
			
			current_x += sprite_width + spacing;
			if (current_x >= texture.getWidth()) {
				current_x = 0;
				current_y -= sprite_height + spacing;
			}
		}
	}
	
	public Sprite getSprite(int index) {
		return this.sprites.get(index);
	}

}
