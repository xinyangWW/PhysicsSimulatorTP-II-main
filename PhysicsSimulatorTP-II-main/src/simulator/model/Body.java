package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public abstract class Body {
	protected String id;
	protected String gid;
	protected Vector2D v;
	protected Vector2D f;
	protected Vector2D p;
	protected double m;

	public Body() {
	}

	public Body(String id, String gid, Vector2D p, Vector2D v, double m) {
		if (id == null || gid == null || v == null || p == null || id.trim().length() == 0 || gid.trim().length() == 0
				|| m <= 0)
			throw new IllegalArgumentException("Hay uno o mas argumentos es nulo");

		this.id = id;
		this.gid = gid;
		this.p = p;
		this.v = v;
		this.m = m;
		this.f = new Vector2D();
	}

	public String getId() {
		return id;
	}

	public String getgId() {
		return gid;
	}

	public Vector2D getVelocity() {
		return v;
	}

	public Vector2D getForce() {
		return f;
	}

	public Vector2D getPosition() {
		return p;
	}

	public double getMass() {
		return m;
	}

	void addForce(Vector2D f) {
		this.f = this.f.plus(f);
	}

	void resetForce() {
		f = new Vector2D();
	}

	public JSONObject getState() {
		JSONObject j = new JSONObject();
		j.put("id", id);
		j.put("m", m);
		j.put("p", p.asJSONArray());
		j.put("v", v.asJSONArray());
		j.put("f", f.asJSONArray());

		return j;
	}

	@Override
	public String toString() {
		return getState().toString();
	}

	@Override
	public boolean equals(Object anObject) {
		if (getClass() != anObject.getClass())
			return false;

		return this.id.equals(((Body) anObject).getId());
	}

	abstract void advance(double dt);
}
