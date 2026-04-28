package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.StationaryBody;

public class StationaryBodyBuilder extends Builder<Body> {

	public StationaryBodyBuilder() {
		super("st_body", "StationaryBody");
	}

	@Override
	protected StationaryBody createInstance(JSONObject data) {
		if (!data.has("id") || !data.has("gid") || !data.has("p") || !data.has("m"))
			throw new IllegalArgumentException("Falta uno o mas argumentos.");

		double m = data.getDouble("m");
		JSONArray p = data.getJSONArray("p");
		String id = data.getString("id"), gid = data.getString("gid");

		if (p.length() != 2)
			throw new IllegalArgumentException("El argumento posicion no es un Vector2D");

		return new StationaryBody(id, gid, new Vector2D(p.getDouble(0), p.getDouble(1)), m);
	}

}
