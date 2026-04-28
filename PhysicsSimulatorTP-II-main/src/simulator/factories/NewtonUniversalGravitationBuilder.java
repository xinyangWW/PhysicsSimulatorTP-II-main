package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {

	public NewtonUniversalGravitationBuilder() {
		super("nlug", "Newton’s law of universal gravitation");
	}

	@Override
	protected NewtonUniversalGravitation createInstance(JSONObject data) {
		return new NewtonUniversalGravitation(data.has("G") ? data.getDouble("G") : 6.67E-11);
	}

	@Override
	protected void fillInData(JSONObject data) {
		data.put("G", "the gravitational constant (a number)");
	}
}
