package components;

import rose.Component;

public class SpriteRenderer extends Component {
	
	private boolean first_time = false;
	
	@Override
	public void start() {
		System.out.println("I am starting");
	}
	
	@Override
	public void update(float dt) {
		if (!first_time) {
			System.out.println("I am updating");
			first_time = true;
		}
	}
}
