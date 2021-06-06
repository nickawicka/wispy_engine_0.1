package rose;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
	private Matrix4f projection_matrix, view_matrix;
	public Vector2f position;
	
	public Camera(Vector2f position) {
		this.position = position;
		this.projection_matrix = new Matrix4f();
		this.view_matrix = new Matrix4f();
		adjustProjection();
	}
	
	public void adjustProjection() {
		projection_matrix.identity();
		projection_matrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
	}
	
	public Matrix4f getViewMatrix() {
		Vector3f camera_front = new Vector3f(0.0f, 0.0f, -1.0f);
		Vector3f camera_up = new Vector3f(0.0f, 1.0f, 0.0f);
		this.view_matrix.identity();
		view_matrix.lookAt(new Vector3f(position.x, position.y, 20.0f), 
							camera_front.add(position.x, position.y, 0.0f), 
							camera_up);
		
		return this.view_matrix;
	}
	
	public Matrix4f getProjectionMatrix() {
		return this.projection_matrix;
	}
}
