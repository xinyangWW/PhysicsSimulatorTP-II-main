package simulator.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class GroupsTableModel extends AbstractTableModel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;

	String[] _header = { "Id", "Force Laws", "Bodies" };
	List<BodiesGroup> _groups;

	GroupsTableModel(Controller ctrl) {
		_groups = new ArrayList<>();
		ctrl.addObserver(this);
	}

	// el resto de métodos van aquí …
	@Override
	public int getRowCount() {
		return _groups.size();
	}

	@Override
	public int getColumnCount() {
		return _header.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return _groups.get(rowIndex).getId();
		case 1:
			return _groups.get(rowIndex).getForceLawsInfo();
		case 2:
			String res = "";
			for (Body body : _groups.get(rowIndex))
				res += body.getId() + " ";

			return res;
		}

		throw new IllegalArgumentException();
	}

	@Override
	public String getColumnName(int column) {
		return _header[column];
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		_groups.clear();
		fireTableStructureChanged();
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		groups.values().forEach(bg -> _groups.add(bg));
		fireTableStructureChanged();
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		_groups.add(g);
		fireTableStructureChanged();
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		fireTableStructureChanged();
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		fireTableStructureChanged();
	}
}
