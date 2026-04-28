package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BodiesGroup implements Iterable<Body> {
	private String id;
	private ForceLaws laws;
	private List<Body> bodies;
	List<Body> _bodiesRO;

	public BodiesGroup(String id, ForceLaws laws) {
		if (id == null || laws == null)
			throw new IllegalArgumentException("Cualquier parámetro es null");

		if (id.trim().length() == 0)
			throw new IllegalArgumentException("El id no incluye al menos un carćter que no sea espacio en blanco");

		this.id = id;
		this.laws = laws;
		this.bodies = new ArrayList<>();
		this._bodiesRO = Collections.unmodifiableList(bodies);

	}

	public String getId() {
		return id;
	}

	void setForceLaws(ForceLaws fl) {
		if (fl == null)
			throw new IllegalArgumentException("El parámetro forcelaws es null");

		this.laws = fl;
	}

	public void addBody(Body b) {
		if (b == null)
			throw new IllegalArgumentException("El parámetro body es null");

		if (bodies.contains(b))
			throw new IllegalArgumentException("Existe otro cuerpo en el grupo con el mismo identificador");

		bodies.add(b);
	}

	void advance(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException("El valor de dt no sea positivo.");

		bodies.forEach(body -> body.resetForce());
		laws.apply(bodies);
		bodies.forEach(body -> body.advance(dt));
	}

	public JSONObject getState() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		bodies.forEach(body -> ja.put(body.getState()));
		jo.put("id", id);
		jo.put("bodies", ja);
		return jo;
	}

	@Override
	public String toString() {
		return getState().toString();
	}

	public String getForceLawsInfo() {
		return laws.toString();
	}

	@Override
	public Iterator<Body> iterator() {
		return _bodiesRO.iterator();
	}

}
