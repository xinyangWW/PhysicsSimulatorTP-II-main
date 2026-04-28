package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator implements Observable<SimulatorObserver> {
	double dt, time = 0;
	ForceLaws laws;
	Map<String, BodiesGroup> groups;
	List<SimulatorObserver> observadores;
	private Map<String, BodiesGroup> _groupsRO;


	public PhysicsSimulator(ForceLaws laws, double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException("El valor de tiempo real no sea válido");
		if (laws == null)
			throw new IllegalArgumentException("El valor de ForceLaws es null");

		this.dt = dt;
		this.laws = laws;
		this.groups = new HashMap<>();
		this.observadores = new ArrayList<>();
		this._groupsRO = Collections.unmodifiableMap(groups);
	}

	public void advance() {
		groups.values().forEach(group -> group.advance(dt));
		time += dt;
		
		observadores.forEach(o -> o.onAdvance(_groupsRO, time));
	}

	public void addGroup(String id) {
		if (groups.get(id) != null)
			throw new IllegalArgumentException("No existe este group id");
		
		BodiesGroup bg = new BodiesGroup(id, this.laws);
		
		groups.put(id, bg);
		
		observadores.forEach(o -> o.onGroupAdded(_groupsRO, bg));
	}

	public void addBody(Body b) {
		BodiesGroup bg = groups.get(b.getgId());
		if (bg == null)
			throw new IllegalArgumentException("El group id de este cuerpo no existe");
		bg.addBody(b);
		
		observadores.forEach(o -> o.onBodyAdded(_groupsRO, b));
	}

	public void setForceLaws(String id, ForceLaws fl) {
		BodiesGroup bg = groups.get(id);
		if (bg == null)
			throw new IllegalArgumentException("No existe este group id");
		bg.setForceLaws(fl);
		
		observadores.forEach(o -> o.onForceLawsChanged(bg));
	}

	public JSONObject getState() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		groups.values().forEach(group -> ja.put(group.getState()));
		jo.put("time", time);
		jo.put("groups", ja);
		return jo;
	}

	@Override
	public String toString() {
		return getState().toString();
	}

	public void reset() {
		groups.clear();
		time = 0;
		
		observadores.forEach(o -> o.onReset(_groupsRO, time, dt));
	}
	
	public void setDeltaTime(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException("El valor de tiempo real no sea válido");
		
		this.dt = dt;
		
		observadores.forEach(o -> o.onDeltaTimeChanged(dt));
	}

	@Override
	public void addObserver(SimulatorObserver o) {
		if(observadores.contains(o))
			throw new IllegalArgumentException("El observador ya existe");
		
		observadores.add(o);
		o.onRegister(_groupsRO, time, dt);
	}

	@Override
	public void removeObserver(SimulatorObserver o) {
		observadores.remove(o);
	}
}
