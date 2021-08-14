package physics2D;

import org.joml.Vector2f;
import org.junit.Test;
import physics2D.rigidbody.IntersectDetector2D;
import renderer.Line2D;

import static junit.framework.TestCase.assertTrue;

public class CollisionDetectorTests {
    private final float EPSILON = 0.000001f;

    @Test
    public void pointOnLine2DShouldReturnTrueTest() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(0, 0);

        assertTrue(IntersectDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnLine2DShouldReturnTrueTestTwo() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(12, 4);

        assertTrue(IntersectDetector2D.pointOnLine(point, line)); // fffgg
    }

    @Test
    public void pointOnVerticalLineShouldReturnTrue() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(0, 10));
        Vector2f point = new Vector2f(0, 5);

        boolean result = IntersectDetector2D.pointOnLine(point, line);
        assertTrue(result);
    }
}