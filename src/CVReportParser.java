import java.util.*;
import org.w3c.dom.*;

public class CVReportParser
{
	/**
	 * list of the failed jobs and cause
	 */
	private List<String> mText;
	
	/**
	 * Document to read from
	 */
	private List<Document> mDocs;
	
	/**
	 * Constructs stuff
	 * @param pDocs Documents to be searched
	 */
	public CVReportParser(List<Document> pDocs)
	{
		mDocs = pDocs;
		mText = new ArrayList<String>();
	}

	/**
	 * Gets the list of failed servers and reason why
	 * @return that
	 */
	public List<String> getList()	
	{
		String val;
		try
		{
			for (Document doc : mDocs)
			{
//				System.out.println("Parsing " + doc.toString());
				
				NodeList nodeList = doc.getElementsByTagName("backupFailedFiles");
				for (int i = 0; i < nodeList.getLength(); i++)
				{
					NamedNodeMap attr = nodeList.item(i).getAttributes();
					val = attr.getNamedItem("val").getNodeValue();
					
					if (!val.equals("None"))
					{
						mText.add(val);
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return mText;
	}
}
