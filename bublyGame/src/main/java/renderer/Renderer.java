package renderer;

import components.SpriteRenderer;
import rose.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
	private final int MAX_BATCH_SIZE = 1000;
	private List<RenderBatch> batches;
	
	public Renderer() {
		this.batches = new ArrayList<>();
	}
	
	public void addGameObject(GameObject go) {
		SpriteRenderer sprite = go.getComponent(SpriteRenderer.class);
		if (sprite != null) {
			addSprite(sprite);
		}
	}
	
	private void addSprite(SpriteRenderer sprite) {
		boolean added = false;
		for (RenderBatch batch : batches) {
			if (batch.hasRoom() && batch.getZIndex() == sprite.game_object.getZIndex()) {
				Texture tex = sprite.getTexture();
				if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
					batch.addSprite(sprite);
					added = true;
					break;
				}
			}
		}
		
		if (!added) {
			RenderBatch new_batch = new RenderBatch(MAX_BATCH_SIZE, sprite.game_object.getZIndex());
			new_batch.start();
			batches.add(new_batch);
			new_batch.addSprite(sprite);
			Collections.sort(batches);
		}		
	}
	
	public void render() {
		for (RenderBatch batch : batches) {
			batch.render();
		}
	}
}
