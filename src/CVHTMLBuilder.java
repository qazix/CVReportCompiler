import java.util.*;
import java.io.*;

public class CVHTMLBuilder
{
	/**
	 * Frequency given server didn't backup
	 */
	private Map<String, Integer> mFreqMap;
	
	/**
	 * Cause that the server didn't back up
	 */
	private Map<String, String> mCauseMap;
	
	/**
	 * Contains the top part of the html
	 */
	private String mHead;
	
	/**
	 * Contains the bottom part of the html
	 */
	private String mFoot;
	
	/**
	 * Constructor
	 * 
	 * @param pFreqMap
	 * @param pCauseMap
	 */
	public CVHTMLBuilder(Map<String, Integer> pFreqMap, Map<String, String> pCauseMap)
	{
		mFreqMap = pFreqMap;
		mCauseMap = pCauseMap;
		
		init();
	}
	
	/**
	 * Builds an HTML page at the SourceDir
	 * @param pSourceDir
	 */
	public void buildHTML(String pSourceDir)
	{
		File html = new File(pSourceDir + File.separator + "html" + File.separator + "CVReports.html");
		File css = new File(pSourceDir + File.separator + "html" + File.separator + "CVR.css");
		
		try
		{
			if(!html.exists() || !css.exists())
			{	
				html.getParentFile().mkdirs();
				html.createNewFile();
				css.createNewFile();
			}
			
			BufferedWriter br = new BufferedWriter(new FileWriter(html));
			br.write(mHead);
		
			for (String key : mFreqMap.keySet())
			{
				br.write("<tr><td>" + key + "</td>\n" + "<td class=\"_" + 
						   (mFreqMap.get(key) > 3 ? 3 : mFreqMap.get(key)) + "\">" + mFreqMap.get(key) + "</td>\n" +
							"<td>" + mCauseMap.get(key) + "</td></tr>");
			}
			br.write(mFoot);
			
			br.close();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	private void init()
	{
		mHead = "<!DOCTYPE html>\n<html>\n<head>\n<title>CommVault Reports</title>\n" +
				  "<link href=\"CVR.css\" rel=\"stylesheet\" type=\"text/css\"/>" +
 				  "</head>\n<body>\n<header>\n<h1>3 Day Review of CommVault " +
				  "Backups\' Failed Jobs</h1>\n<h2>As of " + Calendar.getInstance().getTime() +
				  "</h2>\n</header>\n<main>\n<table class=\"table\">" +
				  "<thead><tr>\n<th>Failed Virtual Server</th>\n<th>Failures in past 3 days</th>" +
				  "<th>Failure Reason</th></tr></thead><tbody>";
		
		mFoot = "</tbody></table></main><footer>\n<h2>Java for CV reports by \n" + 
		"<a href=\"mailto:han12018@byui.edu\">Aaron Hanich</a> CSS by \n" +
		"<a href=\"mailto:bee09005@byui.edu\">Braden Beer</a>\n</h2></footer></body></html>";
	}
}
