package components;

import rose.Component;
import rose.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;

import imgui.ImGui;
import renderer.Texture;

public class SpriteRenderer extends Component {
	
	private Vector4f color = new Vector4f(1, 1, 1, 1);
	private Sprite sprite = new Sprite();
	
	private transient Transform last_transform;
	private transient boolean is_dirty = false;
	
	/*
	public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
        this.is_dirty = true;
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
        this.is_dirty = true;
    }
    */
	
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
	
	@Override
	public void imGui() {
		float[] im_color = {color.x, color.y, color.z, color.w};
		if (ImGui.colorPicker4("Color picker", im_color)) {
			this.color.set(im_color[0], im_color[1], im_color[2], im_color[3]);
			this.is_dirty = true;
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
