package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ControlPanel extends JPanel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	private JToolBar _toolaBar;
	private JFileChooser _fc;
	private boolean _stopped = true; // utilizado en los botones de run/stop
	private JButton _quitButton;
	private JButton _fileButton;
	private JButton _forceLawsButton;
	private JButton _viewerButton;
	private JButton _runButton;
	private JButton _stopButton;
	private JSpinner _stepsEnter;
	private JTextField _deltaTimeEnter;
	private ForceLawsDialog _forceLawsDialog;

	// añade más atributos aquí …
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		_toolaBar = new JToolBar();
		add(_toolaBar, BorderLayout.PAGE_START);

		// File Button
		_fileButton = new JButton();
		_fileButton.setToolTipText("Load an input file into SlidePuzzle");
		_fileButton.setIcon(new ImageIcon("resources/icons/open.png"));
		_fileButton.addActionListener((e) -> {
			int res = _fc.showOpenDialog(Utils.getWindow(this));
			if (res == JFileChooser.APPROVE_OPTION) {
				try {
					File _file = _fc.getSelectedFile();
					_ctrl.reset();
					_ctrl.loadData(new FileInputStream(_file));
				} catch (Exception ex) {
					Utils.showErrorMsg(ex.getMessage());
				}
			}
		});
		_toolaBar.add(_fileButton);
		_toolaBar.addSeparator();

		// Force Laws Button
		_forceLawsButton = new JButton();
		_forceLawsButton.setToolTipText("Select force laws for groups");
		_forceLawsButton.setIcon(new ImageIcon("resources/icons/physics.png"));
		_forceLawsButton.addActionListener((e) -> {
			if (_forceLawsDialog == null)
				_forceLawsDialog = new ForceLawsDialog(Utils.getWindow(ControlPanel.this), _ctrl);

			_forceLawsDialog.open();
		});
		_toolaBar.add(_forceLawsButton);

		// Viewer Button
		_viewerButton = new JButton();
		_viewerButton.setToolTipText("Open viewer window");
		_viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		_viewerButton.addActionListener((e) -> {
			new ViewerWindow((JFrame) Utils.getWindow(ControlPanel.this), _ctrl);
		});
		_toolaBar.add(_viewerButton);
		_toolaBar.addSeparator();

		// Run Button
		_runButton = new JButton();
		_runButton.setToolTipText("Run the simulator");
		_runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		_runButton.addActionListener((e) -> {
			_stopped = false;
			enableToolbar(false);

			_ctrl.setDeltaTime(Double.parseDouble(_deltaTimeEnter.getText()));
			SwingUtilities.invokeLater(() -> run_sim((int) _stepsEnter.getValue()));
		});
		_toolaBar.add(_runButton);

		// Stop Button
		_stopButton = new JButton();
		_stopButton.setToolTipText("Stop the simulator");
		_stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		_stopButton.addActionListener((e) -> {
			_stopped = true;
			enableToolbar(true);
		});
		_toolaBar.add(_stopButton);

		_toolaBar.add(new JLabel("Steps: "));
		_stepsEnter = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		_stepsEnter.setToolTipText("Simulation tick to run: 1-10000");
		_stepsEnter.setMaximumSize(new Dimension(80, 40));
		_stepsEnter.setMinimumSize(new Dimension(80, 40));
		_stepsEnter.setPreferredSize(new Dimension(80, 40));
		_toolaBar.add(_stepsEnter);

		_toolaBar.add(new JLabel("Delta-Time: "));
		_deltaTimeEnter = new JTextField();
		_deltaTimeEnter.setPreferredSize(new Dimension(80, 40));
		_deltaTimeEnter.setMaximumSize(new Dimension(80, 40));
		_deltaTimeEnter.setText("10.0");
		_deltaTimeEnter.setToolTipText("Real time (seconds) corresponding to a step");
		_toolaBar.add(_deltaTimeEnter);

		_toolaBar.add(Box.createGlue()); // this aligns the button to the right
		_toolaBar.addSeparator();

		// Quit Button
		_quitButton = new JButton();
		_quitButton.setToolTipText("Exit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> Utils.quit(this));
		_toolaBar.add(_quitButton);

		_fc = new JFileChooser(".");
		_fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		_fc.setMultiSelectionEnabled(false);
	}

	// el resto de métodos van aquí…
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				enableToolbar(true);
				_stopped = true;
				
				Utils.showErrorMsg(e.getMessage());

				return;
			}
			SwingUtilities.invokeLater(() -> run_sim(n - 1));
		} else {
			enableToolbar(true);
			_stopped = true;
		}
	}
	
	void enableToolbar(boolean b) {
		_quitButton.setEnabled(b);
		_fileButton.setEnabled(b);
		_forceLawsButton.setEnabled(b);
		_viewerButton.setEnabled(b);
		_runButton.setEnabled(b);
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		_deltaTimeEnter.setText(Double.toString(dt));
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		_deltaTimeEnter.setText(Double.toString(dt));
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
	}
}
