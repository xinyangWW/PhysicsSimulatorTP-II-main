package simulator.model;

import simulator.misc.Vector2D;

public class MovingBody extends Body {

	public MovingBody() {
	}

	public MovingBody(String id, String gid, Vector2D p, Vector2D v, double m) {
		super(id, gid, p, v, m);
	}

	@Override
	void advance(double dt) {
		Vector2D a = f.scale(1.0 / m);
		p = p.plus(v.scale(dt)).plus(a.scale(dt * dt / 2));
		v = v.plus(a.scale(dt));
	}

}
