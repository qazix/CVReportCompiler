import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;

public class CVReportCompiler 
	implements Runnable	
{
	/**
	 * This will hold the absolute path to the properties file
	 */
	private static String mPropDir;
	
	/**
	 * This will hold the absolute path to the xml report sources
	 */
	private static String mSourceDir;
	
	/**
	 * This tells the program whether it was called by a .bat or 
	 * by a person
	 */
	private static final String RKEY = "toRun";
	
	/**
	 * Text within the failobjects tag of the xml files
	 */
	private List<String> mText;
	
	/**
	 * Frequency given server didn't backup
	 */
	private Map<String, Integer> mFreqMap;
	
	/**
	 * Cause that the server didn't back up
	 */
	private Map<String, String> mCauseMap;
	
	/**
	 * starts run
	 * 
	 * @param args should be blank
	 */
	public static void main (String[] args)
	{
		CVReportCompiler CVRC = new CVReportCompiler();
		
//		System.out.println("=" + System.getenv(RKEY) + "=");
		if(System.getenv(RKEY) == null || System.getenv(RKEY).equals(""))
		{
			CVReportGUI CVGUI = new CVReportGUI(CVRC.getSourceDir());
			
			while(! CVGUI.isSet())
			{
				try
				{
					Thread.sleep(200);
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
			}
			
			mSourceDir = CVGUI.getDir();
			CVGUI.end();
			CVRC.writeSourceDir(mSourceDir);
		}

		CVRC.run();
	}
	
	/**
	 * This grabs the source directory
	 */
	public CVReportCompiler()
	{
		mFreqMap = new HashMap<String, Integer>();
		mCauseMap = new HashMap<String, String>();
		
		String propDir = new File("").getAbsolutePath();
		mPropDir = propDir + File.separator +
					  "resources";
		
		readSourceDir();
	}
	
	/**
	 * Starts the parser
	 */
	public void run()
	{
		if(mSourceDir != null)
		{
//			System.out.println("Running from " + mSourceDir);
			CVDocumentGetter CVDG = new CVDocumentGetter(mSourceDir);
			mText = new CVReportParser(CVDG.getDocuments()).getList();
			compile();
			new CVHTMLBuilder(mFreqMap, mCauseMap).buildHTML(mPropDir);
		}
		else 
		{
			JOptionPane.showMessageDialog(null, "While running CVReportCompiler there was no directory found\n" +
													"Try running it manually");
		}
	}
	
	/**
	 * This function fills the maps
	 */
	public void compile()
	{
		int i;
		String vmName;
		String failCause;
		
		for (String text : mText)
		{
//			System.out.println(text);
			i = text.indexOf(' ');
			vmName = text.substring(0, i);
			failCause = text.substring(i + 1, text.length() - 1);
			failCause = Character.toUpperCase(failCause.charAt(0)) + failCause.substring(1);
			
			mFreqMap.put(vmName, (mFreqMap.containsKey(vmName) ? mFreqMap.get(vmName) + 1 : 1));
			mCauseMap.put(vmName, failCause);
		}
 	}
	
	/**
	 * This is a simple getter for mSourceDir
	 * @return the absolute path
	 */
	public String getSourceDir()
	{
		return mSourceDir;
	}
	
	/**
	 * Writes the source dir to the props file
	 * @param pDir is the absolute path
	 */
	private void writeSourceDir(String pDir)
	{
		try
		{
			File file = new File(mPropDir + File.separator + "Source.txt");
			if(! file.exists())
			{
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			bw.write(mSourceDir);
			
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}	
	}
	
	/**
	 * Reads the source location from the resource file
	 */
	private void readSourceDir()
	{
		try
		{
			File file = new File(mPropDir + File.separator + "Source.txt");
			if(! file.exists())
			{
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			mSourceDir = br.readLine();
			
			br.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}