package rose;

import org.joml.Vector2f;

import components.Sprite;
import components.SpriteRenderer;

public class Prefabs {
	
	public static GameObject generateSpriteObject(Sprite sprite, float size_x, float size_y) {
		GameObject block = new GameObject("Sprite_Object_Gen",
				new Transform(new Vector2f(), new Vector2f(size_x, size_y)), 0);
		SpriteRenderer renderer = new SpriteRenderer();
		renderer.setSprite(sprite);
		block.addComponent(renderer);
		
		return block;
		// hey hey hey
	}
}
