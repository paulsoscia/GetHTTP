import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class GetHTML {

	static Logger	log						= Logger.getLogger(GetHTML.class.getName());
	//static String	sURL					= "http://www.bing.com/";	// A valid URL
	//static String	sURL					= "http://download.finance.yahoo.com/d/[FILENAME]?s=[TICKERSYMBOLS]&f=[TAGS]&e=.csv";
	static String	sURL					= "http://download.finance.yahoo.com/d/quotes.txt?s=AIG&f=n&e=.csv";
	static String	sEmptyString = "";
	static String	sCommandLineParmsU = "-U=";
	
	public void getHTML(String sLocalURL) {

		String sHTTpResponseLine	= "";	// One line from the HTTP response
		String sHTTpResponseLines	= "";	// Entire HTTP response
		Integer iReturn = 0;
		
		try {
			iReturn = 1;
			URL urlSiteToGetHTML = new URL(sLocalURL);
			try {
				InputStream isGetHtmlData = urlSiteToGetHTML.openStream();
				BufferedReader brGetHtmlData = new BufferedReader(new InputStreamReader(isGetHtmlData));
				while ((sHTTpResponseLine = brGetHtmlData.readLine()) != null) {
					sHTTpResponseLines += sHTTpResponseLine;
				}

			} 
			catch (IOException e) {
				iReturn = 0;
				log.error("Error:  Problem reading HTML, with url " + sLocalURL + ". IOException");
			}
		}
		catch (MalformedURLException e) {
			iReturn = 0;
			log.error("Error:  Problem with url " + sLocalURL + ". MalformedURLException");
		}
		
		log.debug("Completed:getHTML sLocalURL=" + sLocalURL);
		if (iReturn == 1)
		{
			log.debug("Completed:getHTML HTTP get was successful");
			log.debug("Completed:getHTML response length=" + sHTTpResponseLines.length());
			System.out.println( sHTTpResponseLines);
		}
				
	}
	
	public static String[] commandlineParms(String[] args)
	{

	    List<String> listCommandLineParmsTemp = new ArrayList<String>();
	    for(String arg: args) {
	        if (!arg.trim().isEmpty()) {
	        	listCommandLineParmsTemp.add(arg);
	        }
	    }
	    if (args.length == 0) {
	    	listCommandLineParmsTemp.add(sURL);
	    }
	    // nonBlank will have all the elements which contain some characters.
	    String[] strArrCommandLineParms = (String[]) listCommandLineParmsTemp.toArray( new String[listCommandLineParmsTemp.size()] );
		
		for (String sCommandLineParameter: strArrCommandLineParms) {
			sCommandLineParameter = sCommandLineParameter.trim().toUpperCase();
			String sLeftMostCommandLineParameter = sCommandLineParameter.substring(0,3);
			if (sLeftMostCommandLineParameter.equals("HTT"))
			{
				sCommandLineParameter = "-U=" + sCommandLineParameter;
				sLeftMostCommandLineParameter = sCommandLineParameter.substring(0,3);
			}
		    switch(sLeftMostCommandLineParameter){
			    case "-U=": 
			    	log.debug("CommandLineParms: URLs " + sLeftMostCommandLineParameter + " " + sCommandLineParameter);
			    	break;
			    case "-I=": 
			    	log.debug("CommandLineParms: Input File");
			    	break;
			    case "-O=": 
			    	log.debug("CommandLineParms: Output File");
			    	break;
			    case "-?": 
			    	log.debug("CommandLineParms: Help");
			    	break;
			    case "-H": 
			    	log.debug("CommandLineParms: Help");
			    	break;
			    case "-HE": 
			    	log.debug("CommandLineParms: Help");
			    	break;
			    default :
			    	log.warn("CommandLineParms: Invalid Command Line parm");
		     }
		}

		return (strArrCommandLineParms);
	}

	
	
	public static void main(String[] args) {

		//Build -Dlog4j.configuration=name-and-path-of-config-file
		//String log4jConfPath = "/home/paul/workspace/dev/GetHTML/properties/log4j.properties";
		//PropertyConfigurator.configure(log4jConfPath);
		log.info("Time: Starting " );
	    	    
		String[] sListURLs = commandlineParms(args);
		
		GetHTML getCmdParmsPage = new GetHTML();
		for (String sCommandLineParameter: sListURLs) {
			log.info("Time: Staring getHTML Starting " + sCommandLineParameter );
			getCmdParmsPage.getHTML(sCommandLineParameter);
			log.info("Time: Ending getHTML Ending " + sCommandLineParameter);
		}
		log.info("Time: Ending " );
	} // main end

}
