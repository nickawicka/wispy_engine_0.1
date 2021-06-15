package util;

import components.Spritesheet;
import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class AssetPool {
	private static Map<String, Shader> shaders = new HashMap<>();
	private static Map<String, Texture> textures = new HashMap<>();
	private static Map<String, Spritesheet> spritesheets = new HashMap<>();
	
	public static Shader getShader(String resource_name) {
		File file = new File(resource_name);
		if (AssetPool.shaders.containsKey(file.getAbsolutePath())) {
			return AssetPool.shaders.get(file.getAbsolutePath());
		} else {
			Shader shader = new Shader(resource_name);
			shader.compile();
			AssetPool.shaders.put(file.getAbsolutePath(), shader);
			return shader;
		}
	}
	
	public static Texture getTexture(String resource_name) {
		File file = new File(resource_name);
		if (AssetPool.textures.containsKey(file.getAbsolutePath())) {
			return AssetPool.textures.get(file.getAbsolutePath());
		} else {
			Texture texture = new Texture(resource_name);
			AssetPool.textures.put(file.getAbsolutePath(), texture);
			return texture;
		}
	}
	
	public static void addSpriteSheet(String resource_name, Spritesheet spritesheet) {
		File file = new File(resource_name);
		if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
			AssetPool.spritesheets.put(file.getAbsolutePath(), spritesheet);
		}
	}
	
	public static Spritesheet getSpritesheet(String resource_name) {
		File file = new File(resource_name);
		if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
			assert false : "Error: Tried to access spritesheet '" + resource_name + "' and it has not been added to asset pool.";
		}
		return AssetPool.spritesheets.getOrDefault(file.getAbsolutePath(), null);
	}

}
