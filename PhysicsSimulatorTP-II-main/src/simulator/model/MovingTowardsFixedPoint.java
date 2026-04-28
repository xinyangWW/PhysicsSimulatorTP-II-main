package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws {
	double g;
	Vector2D c;

	public MovingTowardsFixedPoint(Vector2D c, double g) {
		if (g <= 0 || c == null)
			throw new IllegalArgumentException("El punto fijo c es null o g no es positivo.");

		this.g = g;
		this.c = c;
	}

	@Override
	public void apply(List<Body> bs) {
		bs.forEach(body -> {
			Vector2D dist = c.minus(body.getPosition());
			body.addForce(dist.direction().scale(g * body.getMass()));
		});
	}

	@Override
	public String toString() {
		return "Moving towards "+ c +" with constant acceleration " + g;
	}
	
}
