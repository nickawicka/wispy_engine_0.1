package rose;

import com.google.gson.*;

import components.Component;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {

	@Override
	public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject json_object = json.getAsJsonObject();
		String name = json_object.get("name").getAsString();
		JsonArray components = json_object.getAsJsonArray("components");
		Transform transform = context.deserialize(json_object.get("transform"), Transform.class);
		int z_index = context.deserialize(json_object.get("z_index"), int.class);
		
		GameObject go = new GameObject(name, transform, z_index);
		for (JsonElement e : components) {
			Component c = context.deserialize(e, Component.class);
			go.addComponent(c);
		}
		return go;
	}
}
