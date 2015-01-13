package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultEditorKit;

import utility.FileLoader;
import utility.SqlParser;

public class MainPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private final String
		CONTENT = "Treść",
		DESCRIPTION = "*.txt", 
		EXTENSION = "txt",
		FILE_CONTENT = "Zawartość pliku:",
		FILE_NAME = "Nazwa pliku:", 
		LOAD_FILE = "Wczytaj plik",
		PARSE_ROW = "Parsuj wiersz";	
	private final int LABEL_LENGTH = 15;	
	private JPanel fileNamePanel = new JPanel();
	private JPanel optionsPanel = new JPanel();
	private JLabel fileNameLabel = new JLabel(this.FILE_NAME);
	private JTextField fileNameTextField = new JTextField(this.LABEL_LENGTH);	
	private DefaultTableModel model = new DefaultTableModel()
	{
		private static final long serialVersionUID = 1L;

		@Override public boolean isCellEditable(int _row, int _column)
		{ return false; }
	};
	private JTable table = new JTable(this.model);
	private JScrollPane scrollPane = new JScrollPane(this.table);
	private JPanel tablePanel = new JPanel();	
	private ActionListener actionListener = new ActionListener()
	{		
		@Override public void actionPerformed(ActionEvent _actionEvent)
		{
			String actionCommand = _actionEvent.getActionCommand();
			switch (actionCommand)
			{
				case LOAD_FILE:
					MainPanel.this.makeChoose();
					break;
				case PARSE_ROW:
					MainPanel.this.makeParse();
					break;
			}
		}		
	};
	JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = 
		new FileNameExtensionFilter(DESCRIPTION, EXTENSION);
	private String orginalTitle;
	private MainFrame mainFrame;
	private ArrayList<String> dataList;
	
	public MainPanel(MainFrame _mainFrame) 
	{ 
		this.mainFrame = _mainFrame;
		this.orginalTitle = this.mainFrame.getTitle();
	}
	
	public void createWorkspace()
	{
		this.setLayout(new BorderLayout());
		this.optionsPanel.setLayout(new GridLayout(3, 1));
		this.fileNamePanel.add(this.fileNameLabel);
		this.fileNameTextField.setEditable(false);
		this.fileNamePanel.add(this.fileNameTextField);
		this.optionsPanel.add(this.fileNamePanel);
		this.makeButton(this.LOAD_FILE);
		this.makeButton(this.PARSE_ROW);
		this.add(this.optionsPanel, BorderLayout.NORTH);		
		this.tablePanel.setLayout(new BorderLayout());
		this.tablePanel.setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createEmptyBorder(), this.FILE_CONTENT,
                TitledBorder.LEFT, TitledBorder.TOP
            )
        );
		this.scrollPane = new JScrollPane(this.table);
		this.table.setFillsViewportHeight(true);
		this.model.addColumn(this.CONTENT);		
		this.tablePanel.add(
			this.table.getTableHeader(), BorderLayout.PAGE_START
		);
		this.tablePanel.add(this.scrollPane, BorderLayout.CENTER);	
		this.add(this.tablePanel, BorderLayout.CENTER);
	}
	
	private void makeButton(String _name)
	{
		JButton button = new JButton(_name);
		button.addActionListener(this.actionListener);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button);
		this.optionsPanel.add(buttonPanel);
		this.fileChooser.setFileFilter(this.filter);
	    this.fileChooser.setCurrentDirectory(
    		new File(System.getProperty("user.dir"))
		);
	}
	
	private void makeChoose()
	{	    
	    int option = fileChooser.showOpenDialog(this);
	    if (option == JFileChooser.APPROVE_OPTION)
	    {
	    	File file = fileChooser.getSelectedFile();
	    	this.fileChooser.setCurrentDirectory(file);
	    	String fileName = file.getName();
	    	this.mainFrame.setTitle(this.mainFrame.getTitle() + 
    			": " + fileName);
	    	this.fileNameTextField.setText(fileName);
	    	this.dataList = FileLoader.getData(file, this);
	    	if (this.dataList != null) this.makeDataTable();
	    }
	    else if (option == JFileChooser.CANCEL_OPTION)
	    {
	    	final String EMPTY = "";
	    	this.mainFrame.setTitle(this.orginalTitle);
	    	this.fileNameTextField.setText(EMPTY);
	    	if (this.dataList != null) this.removeDataTable();
	    }
	}
	
	private void makeParse()
	{
		final String COPY = "Copy", EMPTY = "", PARSED_QUERY = "Parsed query";
		int selection = this.table.getSelectedRow();
		String result = EMPTY, rowValue = EMPTY;
		JTextArea textArea = new JTextArea();
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem();
		if (selection != -1)
		{
			rowValue = this.table.getValueAt(selection, 0).toString();
			result = SqlParser.parseData(rowValue);
			textArea.setText(result);
			textArea.setEditable(true);
			textArea.setWrapStyleWord(true);
			textArea.setLineWrap(true);
			textArea.setCaretPosition(0);
			menuItem.setAction(new DefaultEditorKit.CopyAction());
			menuItem.setMnemonic(KeyEvent.VK_C);
			menuItem.setText(COPY);
			popupMenu.add(menuItem);
			textArea.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent _me)
				{ if (_me.isPopupTrigger()) doPop(_me); }				
				    public void mouseReleased(MouseEvent _me)
				{ if (_me.isPopupTrigger()) doPop(_me); }
			    
				private void doPop(MouseEvent _me)
			    	{
			    		popupMenu.show(
		    				_me.getComponent(), _me.getX(), _me.getY()
    					);
		    		}
			});
			JOptionPane.showMessageDialog(
				this, textArea, PARSED_QUERY, JOptionPane.PLAIN_MESSAGE
			);
		}
	}
	
	private void makeDataTable()
    {
		for (String data : this.dataList) 
			this.model.addRow(new Object[] {data});
    }
	
	private void removeDataTable()
    {
		this.dataList.clear();
		this.model.setRowCount(0);
    }
}
