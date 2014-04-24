import java.util.*;

import org.w3c.dom.*;

import java.io.*;

import javax.xml.parsers.*;

public class CVDocumentGetter
{
	/**
	 * List of Documents
	 */
	private List<Document> mDocs;
	
	/**
	 * String for absolute path to source dir
	 */
	private String mDir;
	
	/**
	 * Constructor
	 * @param pDir absolute path to source dir
	 */
	public CVDocumentGetter(String pDir)
	{
		mDir = pDir;
		mDocs = new ArrayList<Document>();
	}
	
	/**
	 * Time in milliseconds
	 */
	private static final long DAY_IN_MS = 1000 * 60 * 60 * 24; 
	
	/**
	 * Goes through xml files returns the ones we want and delete the rest
	 * @return
	 */
	public List<Document> getDocuments()
	{
		//I set the time to be three days prior to get the last three days
		// Reports are run at 7 am so I set the hour to before that to ensure 
		// we grab three
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(cal.getTimeInMillis() - (2 * DAY_IN_MS)));
		cal.set(Calendar.HOUR_OF_DAY, 5);

		try 
		{
			DocumentBuilder docFactory = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			File[] files = new File(mDir).listFiles(new FileFilter()
			{
            public boolean accept(File file)
            {
	            return file.toString().startsWith("BackupJob", file.toString().lastIndexOf(File.separator) + 1) && 
	            		 file.toString().endsWith(".xml") && 
	            		 (cal.getTimeInMillis() < file.lastModified());
            }
			});
			
//			System.out.println("getting docs" + files.length);
						
			for (File file : files)
			{
//				System.out.println("ToDoc " + file.toString());	
				
				mDocs.add(docFactory.parse(file));
			}
			
			File[] remove = new File(mDir).listFiles(new FileFilter()
			{
            public boolean accept(File file)
            {
	            return file.toString().startsWith("BackupJob", file.toString().lastIndexOf(File.separator) + 1) && 
	            		 file.toString().endsWith(".xml") && 
	            		 (cal.getTimeInMillis() > file.lastModified());
            }
			});
			
			for (File file : remove)
			{
				file.delete();
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		
		return mDocs;
	}
}
