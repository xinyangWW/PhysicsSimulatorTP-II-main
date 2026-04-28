package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class StatusBar extends JPanel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;

	// Añadir los atributos necesarios, si hace falta …
	JLabel _timeLabel;
	JLabel _groupsLabel;

	StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));

		_timeLabel = new JLabel("Time: 0");
		this.add(_timeLabel);
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);

		_groupsLabel = new JLabel("Groups: 0");
		this.add(_groupsLabel);
		JSeparator s2 = new JSeparator(JSeparator.VERTICAL);
		s2.setPreferredSize(new Dimension(10, 20));
		this.add(s2);
	}

	// el resto de métodos van aquí…
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		_timeLabel.setText("Time: " + time);
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		_timeLabel.setText("Time: " + time);
		_groupsLabel.setText("Groups: " + groups.size());
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		_timeLabel.setText("Time: " + time);
		_groupsLabel.setText("Groups: " + groups.size());
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		_groupsLabel.setText("Groups: " + groups.size());
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
	}
}
