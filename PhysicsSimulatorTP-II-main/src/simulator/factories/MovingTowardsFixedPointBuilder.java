package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {

	public MovingTowardsFixedPointBuilder() {
		super("mtfp", "Moving towards a fixed point");
	}

	@Override
	protected MovingTowardsFixedPoint createInstance(JSONObject data) {
		Vector2D c = new Vector2D();

		if (data.has("c")) {
			JSONArray _c = data.getJSONArray("c");

			if (_c.length() != 2)
				throw new IllegalArgumentException("El argumento c no es un Vector2D");

			c = new Vector2D(_c.getDouble(0), _c.getDouble(1));
		}

		return new MovingTowardsFixedPoint(c, data.has("g") ? data.getDouble("g") : 9.81);
	}

	@Override
	protected void fillInData(JSONObject data) {
		data.put("c", "the point towards which bodies move (e.g., [100.0,50.0])");
		data.put("g", "the length of the acceleration vector (a number)");
	}

}
