import javax.swing.*;

import java.awt.event.*;

public class CVReportGUI
{
	/**
	 * This is the Frame for the GUI
	 */
	public JFrame mFrame;
	/**
	 * The current source directory
	 */
	public JTextField mCurrentDir;
	
	/**
	 * The desired source directory
	 */
	public JTextField mSourceDir;
	
	/**
	 * This button opens a JFileChooser dialog
	 */
	public JButton mBrowseButton;
	
	/**
	 * this holds source JTextField and browse button
	 */
	public JPanel mSourcePanel;
	
	/**
	 * This button saves the text area
	 */
	public JButton mSave;
	
	/**
	 * This button closes the window
	 */
	public JButton mCancel;
	
	/**
	 * This holds the buttons that will close the window
	 */
	public JPanel mClosePanel;
	
	/**
	 * This will hold the result of the change
	 */
	private String mDir;
	
	/**
	 * This tells me that a new path has been set
	 */
	private boolean mSet = false;
	
	/**
	 * Main just runs it 
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{		
	}
	
	/**
	 * Propagates up to CVReportGUI(String)
	 */
	public CVReportGUI()
	{
		this("");
	}
	
	/**
	 * Sets up the GUI
	 * 
	 * @param pDir is the source directory
	 */
	public CVReportGUI(String pDir)
	{
		mDir = pDir;
		
		mFrame = new JFrame("CVReport Manager");
		//I getContentPane because Box layouts are set to the panel and won't work otherwise
		mFrame.setLayout(new BoxLayout(mFrame.getContentPane(), BoxLayout.Y_AXIS));
		mFrame.setLocation(400, 200);
		mFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				close();
			}
		});
		
		mCurrentDir = new JTextField(mDir, 20);
		mCurrentDir.setEditable(false);
		
		mSourceDir = new JTextField(20);	
		mBrowseButton = new JButton("...");
		mBrowseButton.addActionListener(new ActionListener()
		{	
			public void actionPerformed(ActionEvent event)
			{
				try
				{  // This part opens a fileChooser to select a file very convenient
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					if (chooser.showOpenDialog(mFrame) == JFileChooser.APPROVE_OPTION)
					{
						mSourceDir.setText(chooser.getSelectedFile().getAbsolutePath());
					}
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
			}
		});
		
		mSourcePanel = new JPanel();
		mSourcePanel.setLayout(new BoxLayout(mSourcePanel, BoxLayout.X_AXIS));
		mSourcePanel.add(mSourceDir);
		mSourcePanel.add(mBrowseButton);
		
		mSave = new JButton("Save");
		mSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if (new CVDocumentGetter(mSourceDir.getText()).getDocuments().isEmpty())
				{
					JOptionPane.showMessageDialog(mFrame, "Folder you selected does not contain any valid xml files\ntry agian");
				}
				else
				{
					save(mSourceDir.getText());
				}
			}
		});
		mCancel = new JButton("Cancel");
		mCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				close();
			}
		});
		
		mClosePanel = new JPanel();
		mClosePanel.setLayout(new BoxLayout(mClosePanel, BoxLayout.X_AXIS));
		mClosePanel.add(mSave);
		mClosePanel.add(mCancel);
		
		mFrame.add(mCurrentDir);
		mFrame.add(mSourcePanel);
		mFrame.add(mClosePanel);
		mFrame.pack();
		mFrame.setResizable(false);
		mFrame.setVisible(true);
	}
	
	/**
	 * This saves the name of the file but only if it is valid
	 * 
	 * @param pDir
	 */
	private void save(String pDir)
	{
		if(pDir != "")
		{
			mDir = pDir;
		}
		
		close();
	}
	
	/**
	 * This program will only let you cancel if you have selected a directory
	 */
	private void close()
	{
		if (mDir == "")
		{
			JOptionPane.showMessageDialog(mFrame, "You must have a directory selected");
		}
		else 
		{
			mSet = true;
			
			mFrame.setVisible(false);
		}
	}
	
	/**
	 * This is to return the final result
	 * 
	 * @return the directory path
	 */
	public String getDir()
	{
		return mDir;
	}
	
	/**
	 * Says whether a directory has been chosen
	 * @return true or false
	 */
	public boolean isSet()
	{
		return mSet;
	}
	
	/**
	 * End disposes of the frame
	 */
	public void end()
	{
		mFrame.dispose();
	}
}