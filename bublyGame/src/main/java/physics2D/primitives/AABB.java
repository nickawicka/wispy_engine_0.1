package physics2D.primitives;

import org.joml.Vector2f;
import physics2D.rigidbody.Rigidbody2D;

// Axis aligned bounding box
public class AABB {
	private Vector2f size = new Vector2f();
	private Vector2f half_size;
	private Rigidbody2D rigidbody = null;
	
	public AABB() {
		this.half_size = new Vector2f(size).mul(0.5f);
	}
	
	public AABB(Vector2f min, Vector2f max) {
		this.size = new Vector2f(max).sub(min);
		this.half_size = new Vector2f(size).mul(0.5f);
	}
	
	public Vector2f getMin() {
		return new Vector2f(this.rigidbody.getPosition()).sub(this.half_size);
	}
	
	public Vector2f getMax() {
		return new Vector2f(this.rigidbody.getPosition()).add(this.half_size);
	}

}
