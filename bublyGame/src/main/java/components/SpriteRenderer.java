package components;

import rose.Component;
import rose.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {
	
	private Vector4f color;
	private Sprite sprite;
	
	private Transform last_transform;
	private boolean is_dirty = false;
	
	public SpriteRenderer(Vector4f color) {
		this.color = color;
		this.sprite = new Sprite(null);
	}
	
	public SpriteRenderer(Sprite sprite) {
		this.sprite = sprite;
		this.color = new Vector4f(1, 1, 1, 1);
	}
	
	@Override
	public void start() {
		this.last_transform = game_object.transform.copy();
	}
	
	@Override
	public void update(float dt) {
		if (!this.last_transform.equals(this.game_object.transform)) {
			this.game_object.transform.copy(this.last_transform);
			is_dirty = true;
		}
	}
	
	public Vector4f getColor() {
		return this.color;
	}
	
	public Texture getTexture() {
		return sprite.getTexture();
	}
	
	public Vector2f[] getTexCoords() {
		return sprite.getTexCoords();
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		this.is_dirty = true;
	}
	
	public void setColor(Vector4f color) {
		if (!this.color.equals(color)) {
			this.is_dirty = true;
			this.color.set(color);
		}
	}
	
	public boolean isDirty() {
		return this.is_dirty;
	}
	
	public void setClean() {
		this.is_dirty = false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
