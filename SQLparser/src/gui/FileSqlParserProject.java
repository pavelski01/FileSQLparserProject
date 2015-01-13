package gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class FileSqlParserProject
{
	private static final String PROJECT_NAME = 
		"FileSqlParserProject";
	public static final int DEFAULT_WIDTH = 1080;
	public static final int DEFAULT_HEIGHT = 600;
	private static MainFrame mainFrame;
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run()
			{
				FileSqlParserProject.mainFrame = 
					new MainFrame(FileSqlParserProject.PROJECT_NAME);
				FileSqlParserProject.mainFrame.createMainPanel(
					FileSqlParserProject.DEFAULT_WIDTH, 
					FileSqlParserProject.DEFAULT_HEIGHT
				);
				FileSqlParserProject.mainFrame.setDefaultCloseOperation(
					JFrame.EXIT_ON_CLOSE
				);
				FileSqlParserProject.mainFrame.setVisible(true);
			}
		});
	}
}
