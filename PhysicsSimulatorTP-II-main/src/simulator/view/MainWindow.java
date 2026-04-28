package simulator.view;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import simulator.control.Controller;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private Controller _ctrl;

	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);

		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);

		// Definición del panel de tablas (usa un BoxLayout vertical)
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);

		InfoTable groupTable = new InfoTable("Groups", new GroupsTableModel(_ctrl));
		groupTable.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(groupTable);

		InfoTable bodyTable = new InfoTable("Bodies", new BodiesTableModel(_ctrl));
		bodyTable.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(bodyTable);

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				Utils.quit(MainWindow.this);
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

		});

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
