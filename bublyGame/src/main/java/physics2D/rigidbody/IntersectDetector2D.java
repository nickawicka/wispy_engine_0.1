package physics2D.rigidbody;

import org.joml.Vector2f;

import physics2D.primitives.AABB;
import physics2D.primitives.Box2D;
import physics2D.primitives.Circle;
import renderer.Line2D;
import util.JMath;

public class IntersectDetector2D {
	// ========================================================
    // Point vs. Primitive Tests
    // ========================================================

    public static boolean pointOnLine(Vector2f point, Line2D line) {
    	float dy = line.getEnd().y - line.getStart().y;
    	float dx = line.getEnd().x - line.getStart().x;
    	if (dx == 0f) {
    		return JMath.compare(point.x, line.getStart().x);
    	}
    	float m = dy / dx;
    	
    	float b = line.getEnd().y - (m * line.getEnd().x);
    	
    	// Check the line equation
    	return point.y == m * point.x + b;
    }
    
    public static boolean pointInCircle(Vector2f point, Circle circle) {
    	Vector2f circle_center = circle.getCenter();
    	Vector2f center_to_point = new Vector2f(point).sub(circle_center);
    	
    	return center_to_point.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }
    
    public static boolean pointInAABB(Vector2f point, AABB box) {
    	Vector2f min = box.getMin();
    	Vector2f max = box.getMax();
    	
    	return point.x <= max.x && min.x <= point.x &&
    			point.y <= max.y && min.y <= point.y;
    }
    
    public static boolean pointInBox2D(Vector2f point, Box2D box) {
    	// Translate the point into local space
    	Vector2f point_local_box_space = new Vector2f(point);
    	JMath.rotate(point_local_box_space, box.getRigidbody().getRotation(), box.getRigidbody().getPosition());
    	
    	Vector2f min = box.getMin();
    	Vector2f max = box.getMax();
    	
    	return point_local_box_space.x <= max.x && min.x <= point_local_box_space.x &&
    			point_local_box_space.y <= max.y && min.y <= point_local_box_space.y;
    }
    
    public static boolean lineAndCircle(Line2D line, Circle circle) {
    	if (pointInCircle(line.getStart(), circle) || pointInCircle(line.getEnd(), circle)) {
    		return true;
    	}
    	
    	Vector2f ab = new Vector2f(line.getEnd()).sub(line.getStart());
    	
    	// Project point (circle position) onto ab (line segment)
    	// parameterized position t
    	Vector2f circle_center = circle.getCenter();
    	Vector2f center_to_line_start = new Vector2f(circle_center).sub(line.getStart());
    	float t = center_to_line_start.dot(ab) / ab.dot(ab);
    	
    	if (t < 0.0f || t > 1.0f) {
    		return false;
    	}
    	
    	// Find the closest point to the line segment
    	Vector2f closest_point = new Vector2f(line.getStart()).add(ab.mul(t));
    	
    	return pointInCircle(closest_point, circle);
    }
    
    public static boolean lineAndAABB(Line2D line, AABB box) {
    	if (pointInAABB(line.getStart(), box) || pointInAABB(line.getEnd(), box)) {
    		return true;
    	}
    	
    	Vector2f unit_vector = new Vector2f(line.getEnd()).sub(line.getStart());
    	unit_vector.normalize();
    	unit_vector.x = (unit_vector.x != 0) ? 1.0f / unit_vector.x : 0f;
    	unit_vector.y = (unit_vector.y != 0) ? 1.0f / unit_vector.y : 0f;
    	
    	Vector2f min = box.getMin();
    	min.sub(line.getStart()).mul(unit_vector);
    	Vector2f max = box.getMax();
    	max.sub(line.getStart()).mul(unit_vector);
    	
    	float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
    	float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));
    	
    	if (tmax < 0 || tmin > tmax) {
    		return false;
    	}
    	float t = (tmin < 0f) ? tmax : tmin;
    	return t > 0f && t * t < line.lengthSquared();
    }
}
