package simulator.view;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.*;

public class InfoTable extends JPanel {

	private static final long serialVersionUID = 1L;

	String _title;
	TableModel _tableModel;

	InfoTable(String title, TableModel tableModel) {
		_title = title;
		_tableModel = tableModel;
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(_title));

		add(new JScrollPane(new JTable(_tableModel)));
	}
}
