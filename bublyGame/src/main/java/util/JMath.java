package util;

import org.joml.Vector2f;

public class JMath {

	public static void rotate(Vector2f vec, float angle_deg, Vector2f origin) {
		float x = vec.x - origin.x;
		float y = vec.y - origin.y;
		
		float cos = (float)Math.cos(Math.toRadians(angle_deg));
		float sin = (float)Math.sin(Math.toRadians(angle_deg));
		
		float x_prime = (x * cos) - (y * sin);
		float y_prime = (y * sin) + (y * cos);
		
		x_prime += origin.x;
		y_prime += origin.y;
		
		vec.x = x_prime;
		vec.y = y_prime;
	}
	
	public static boolean compare(float x, float y, float epsilon) {
		return Math.abs(x - y) <= epsilon * Math.max(1.0f, Math.max(Math.abs(x), Math.abs(y)));
	}
	
	public static boolean compare(Vector2f vec_1, Vector2f vec_2, float epsilon) {
		return compare(vec_1.x, vec_2.x, epsilon) && compare(vec_1.y, vec_2.y, epsilon);
	}
	
	public static boolean compare(float x, float y) {
		return Math.abs(x - y) <= Float.MIN_VALUE * Math.max(1.0f, Math.max(Math.abs(x), Math.abs(y)));
	}
	
	public static boolean compare(Vector2f vec_1, Vector2f vec_2) {
		return compare(vec_1.x, vec_2.x) && compare(vec_1.y, vec_2.y);
	}
}
