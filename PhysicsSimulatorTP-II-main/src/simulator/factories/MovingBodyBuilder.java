package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MovingBody;

public class MovingBodyBuilder extends Builder<Body> {

	public MovingBodyBuilder() {
		super("mv_body", "MovingBody");
	}

	@Override
	protected MovingBody createInstance(JSONObject data) {
		if (!data.has("id") || !data.has("gid") || !data.has("v") || !data.has("p") || !data.has("m"))
			throw new IllegalArgumentException("Falta uno o mas argumentos.");

		double m = data.getDouble("m");
		String id = data.getString("id"), gid = data.getString("gid");
		JSONArray v = data.getJSONArray("v"), p = data.getJSONArray("p");

		if (v.length() != 2 || p.length() != 2)
			throw new IllegalArgumentException("El argumento velocidad o posicion no es un Vector2D");

		return new MovingBody(id, gid, new Vector2D(p.getDouble(0), p.getDouble(1)),
				new Vector2D(v.getDouble(0), v.getDouble(1)), m);
	}

}
