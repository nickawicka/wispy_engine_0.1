package components;

import org.joml.Vector2f;
import org.joml.Vector3f;

import renderer.DebugDraw;
import rose.Window;
import util.Settings;

public class GridLines extends Component {
	
	@Override
	public void update(float dt) {
		Vector2f camera_pos = Window.getScene().camera().position;
		Vector2f projection_size = Window.getScene().camera().getProjectionSize();
		
		int first_x = ((int)(camera_pos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
		int first_y = ((int)(camera_pos.y / Settings.GRID_HEIGHT) - 1) * Settings.GRID_HEIGHT;
	
		int num_vt_lines = (int)(projection_size.x / Settings.GRID_WIDTH) + 2;
		int num_hz_lines = (int)(projection_size.y / Settings.GRID_HEIGHT) + 2;
		
		int height = (int)projection_size.y + Settings.GRID_HEIGHT * 2;
		int width = (int)projection_size.x + Settings.GRID_WIDTH * 2;
		
		int max_lines = Math.max(num_vt_lines, num_vt_lines);
		Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);
		
		for (int i = 0; i < max_lines; i++) {
			int x = first_x + (Settings.GRID_WIDTH * i);
			int y = first_y + (Settings.GRID_HEIGHT * i);
			
			if (i < num_vt_lines) {
				DebugDraw.addLine2D(new Vector2f(x, first_y), new Vector2f(x, first_y + height), color);
			}
			
			if (i < num_hz_lines) {
				DebugDraw.addLine2D(new Vector2f(first_x, y), new Vector2f(first_x + width, y), color);
			}
		}		
	}
}
