package gui;

import java.awt.Dimension;
import javax.swing.JFrame;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	private MainPanel mainPanel = new MainPanel(this);
	
	public MainFrame(String _projectName) { super(_projectName); }
	
	public void createMainPanel(int _width, int _height)
	{
		this.setPreferredSize(new Dimension(_width, _height));
		this.add(this.mainPanel);
		this.mainPanel.createWorkspace();		
		this.pack();
	}
}
