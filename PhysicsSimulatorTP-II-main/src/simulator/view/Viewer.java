package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import simulator.misc.Vector2D;
import simulator.model.BodiesGroup;
import simulator.model.Body;

@SuppressWarnings("serial")
class Viewer extends SimulationViewer {

	private static final int _WIDTH = 500;
	private static final int _HEIGHT = 500;

	// (_centerX,_centerY) is used as the origin when drawing
	// the bodies
	private int _centerX;
	private int _centerY;

	// values used to shift the actual origin (the middle of
	// the window), when calculating (_centerX,_centerY)
	private int _originX = 0;
	private int _originY = 0;

	// the scale factor, used to reduce the bodies coordinates
	// to the size of the component
	private double _scale = 1.0;

	// indicates if the help message should be shown
	private boolean _showHelp = true;

	// indicates if the position/velocity vectors should be shown
	private boolean _showVectors = true;

	// the list bodies and groups
	private List<Body> _bodies;
	private List<BodiesGroup> _groups;

	// a color generator, and a map that assigns colors to groups
	private ColorsGenerator _colorGen;
	private Map<String, Color> _gColor;

	// the index and Id of the selected group, -1 and null means all groups
	private int _selectedGroupIdx = -1;
	private String _selectedGroup = null;

	Viewer() {
		initGUI();
	}

	private void initGUI() {

		// add a border
		setBorder(BorderFactory.createLineBorder(Color.black, 2));

		// initialize the color generator, and the map, that we use
		// assign colors to groups
		_colorGen = new ColorsGenerator();
		_gColor = new HashMap<>();

		// initialize the lists of bodies and groups
		_bodies = new ArrayList<>();
		_groups = new ArrayList<>();

		// The preferred and minimum size of the components
		setMinimumSize(new Dimension(_WIDTH, _HEIGHT));
		setPreferredSize(new Dimension(_WIDTH, _HEIGHT));

		// add a key listener to handle the user actions
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '-':
					_scale = _scale * 1.1;
					repaint();
					break;
				case '+':
					_scale = Math.max(1000.0, _scale / 1.1);
					repaint();
					break;
				case '=':
					autoScale();
					repaint();
					break;
				default:
				}

				switch (e.getKeyCode()) {
				case KeyEvent.VK_J:
					_originX += 10;
					repaint();
					break;
				case KeyEvent.VK_L:
					_originX -= 10;
					repaint();
					break;
				case KeyEvent.VK_I:
					_originY += 10;
					repaint();
					break;
				case KeyEvent.VK_M:
					_originY -= 10;
					repaint();
					break;
				case KeyEvent.VK_K:
					_originX = 0;
					_originY = 0;
					repaint();
					break;
				case KeyEvent.VK_H:
					_showHelp ^= true;
					repaint();
					break;
				case KeyEvent.VK_V:
					_showVectors ^= true;
					repaint();
					break;
				case KeyEvent.VK_G:
					_selectedGroupIdx = (_selectedGroupIdx + 2) % (_groups.size() + 1) - 1;
					_selectedGroup = _selectedGroupIdx == -1 ? null : _groups.get(_selectedGroupIdx).getId();
					repaint();
					break;
				}
			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// a better graphics object
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// calculate the center
		_centerX = getWidth() / 2 - _originX;
		_centerY = getHeight() / 2 - _originY;

		// draw red cross at (_centerX,_centerY)
		gr.setColor(Color.RED);
		gr.drawLine(_centerX - 5, _centerY, _centerX + 5, _centerY);
		gr.drawLine(_centerX, _centerY - 5, _centerX, _centerY + 5);

		// draw bodies
		drawBodies(gr);

		// show help if needed
		if (_showHelp) {
			showHelp(gr);
		}
	}

	private void showHelp(Graphics2D g) {
		g.setColor(Color.RED);
		g.setFont(new Font(null, 1, 15));
		g.drawString("h: toggle help, v: toggle vectors, +: zoom-in, -: zoom-out, =: fit", 5, 20);
		g.drawString("l: move right, j: move left, i: move up, m: move down: k: reset", 5, 40);
		g.drawString("g: show next group", 5, 60);
		g.drawString("Scaling ratio: " + _scale, 5, 80);

		g.setColor(Color.BLUE);
		g.drawString("Selected Group: " + (_selectedGroup == null ? "all" : _selectedGroup), 5, 100);
	}

	private void drawBodies(Graphics2D g) {
		for (Body b : _bodies) {
			if (isVisible(b)) {
				Vector2D bodyDir = b.getPosition().scale(1.0 / _scale);
				Vector2D pos = new Vector2D(_centerX + bodyDir.getX(), _centerY - bodyDir.getY());
				g.setColor(_gColor.get(b.getgId()));
				g.fillOval((int) pos.getX() - 5, (int) pos.getY() - 5, 10, 10);
				g.setColor(Color.BLACK);
				g.drawString(b.getId(), (int) pos.getX() - g.getFontMetrics().stringWidth(b.getId()) / 2, // pos x
						(int) pos.getY() - 8);

				if (_showVectors) {
					Vector2D _force = b.getForce().direction().scale(30);
					Vector2D _velo = b.getVelocity().direction().scale(30);
					Vector2D _vForce = new Vector2D(pos.getX() + _force.getX(), pos.getY() - _force.getY());
					Vector2D _vVelo = new Vector2D(pos.getX() + _velo.getX(), pos.getY() - _velo.getY());
					drawLineWithArrow(g, (int) pos.getX(), (int) pos.getY(), (int) _vForce.getX(), (int) _vForce.getY(),
							5, 5, Color.RED, Color.RED);
					drawLineWithArrow(g, (int) pos.getX(), (int) pos.getY(), (int) _vVelo.getX(), (int) _vVelo.getY(),
							5, 5, Color.GREEN, Color.GREEN);
				}
			}
		}
	}

	private boolean isVisible(Body b) {
		return _selectedGroup == null || _selectedGroup.equals(b.getgId());
	}

	// calculates a value for scale such that all visible bodies fit in the window
	private void autoScale() {

		double max = 1.0;

		for (Body b : _bodies) {
			Vector2D p = b.getPosition();
			max = Math.max(max, Math.abs(p.getX()));
			max = Math.max(max, Math.abs(p.getY()));
		}

		double size = Math.max(1.0, Math.min(getWidth(), getHeight()));

		_scale = max > size ? 4.0 * max / size : 1.0;
	}

	@Override
	public void addGroup(BodiesGroup g) {
		_groups.add(g);
		g.forEach(body -> _bodies.add(body));

		_gColor.put(g.getId(), _colorGen.nextColor()); // assign color to group
		autoScale();
		update();
	}

	@Override
	public void addBody(Body b) {
		_bodies.add(b);

		autoScale();
		update();
	}

	@Override
	public void reset() {
		_groups.clear();
		_bodies.clear();
		_gColor.clear();

		_colorGen.reset(); // reset the color generator
		_selectedGroupIdx = -1;
		_selectedGroup = null;
		update();
	}

	@Override
	void update() {
		repaint();
	}

	// This method draws a line from (x1,y1) to (x2,y2) with an arrow.
	// The arrow is of height h and width w.
	// The last two arguments are the colors of the arrow and the line
	private void drawLineWithArrow(//
			Graphics g, //
			int x1, int y1, //
			int x2, int y2, //
			int w, int h, //
			Color lineColor, Color arrowColor) {

		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - w, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;

		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;

		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;

		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };

		g.setColor(lineColor);
		g.drawLine(x1, y1, x2, y2);
		g.setColor(arrowColor);
		g.fillPolygon(xpoints, ypoints, 3);
	}

}
