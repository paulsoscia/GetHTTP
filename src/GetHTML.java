import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class GetHTML {

	static Logger	log						= Logger.getLogger(GetHTML.class.getName());
	//static String	sURL					= "http://www.bing.com/";	// A valid URL
	//																				&a=09(zero based October) &b=8 (8th and later)
	//
	public static final	String	sURL							= "http://www.google.com/";
	public static final	String	sUrlYahooFinanceIntelHistorical = "http://ichart.finance.yahoo.com/table.csv?s=INTC&a=09&b=8&c=2014";
	public static final	String	sUrlYahooFinanceStockName		= "http://download.finance.yahoo.com/d/quotes.txt?s=AIG&f=n&e=.csv";
	public static final	String	sEmptyString = "";
	
	public static final		String sLeftThreeHTTP 					= "HTT";
	public static final		String sCommandLineParmsOutputSysOut	= "SYSOUT";	
	public static final		String sCommandLineParmsUrls			= "-U=";
	public static final		String sCommandLineParmsOutput 			= "-O=";
	
	public static String sOutputFileName 							= "";
	public static Boolean bOutputFile 								= Boolean.FALSE;
	
	public void writeFile(String sPathAndFileName, String sDataToWriteToFile) {
	
		PrintWriter prtwrtFileOut;
		try {
			prtwrtFileOut = new PrintWriter(new FileWriter(sPathAndFileName, true));
			prtwrtFileOut.println(sDataToWriteToFile);
			prtwrtFileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Error opening file " + sPathAndFileName + " " + e.getMessage());
			e.printStackTrace();
		}
		
		BufferedWriter writer = null;
		try
		{
		    writer = new BufferedWriter( new FileWriter( sPathAndFileName, true));
		    writer.write( sDataToWriteToFile);
		    writer.newLine();

		}
		catch ( IOException e)
		{
			log.error("Error New or write to file " + e.getMessage());
		}
		finally
		{
		    try
		    {
		        if ( writer != null)
		        	writer.close( );
		    }
		    catch ( IOException e)
		    {
		    	log.error("Error closing file " + e.getMessage());
		    }
		}
	}
	
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
			if (! bOutputFile)
			{
				log.debug("Begin out->sysout");
				System.out.println( sHTTpResponseLines);
				log.debug("End out->sysout");
			}
			if (bOutputFile)
			{
				log.debug("Begin out->file");
				writeFile(sOutputFileName, sHTTpResponseLines);
				log.debug("End out->file");
			}
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
			sCommandLineParameter = sCommandLineParameter.trim();
			String sLeftMostCommandLineParameter = sCommandLineParameter.substring(0,3);
			if (sLeftMostCommandLineParameter.equalsIgnoreCase(sLeftThreeHTTP))
			{
				sCommandLineParameter = sCommandLineParmsUrls + sCommandLineParameter;
				sLeftMostCommandLineParameter = sCommandLineParameter.substring(0,3);
			}

			    if (sLeftMostCommandLineParameter.equalsIgnoreCase(sCommandLineParmsUrls)) { 
			    	log.debug("CommandLineParms: URLs " + sLeftMostCommandLineParameter + " " + sCommandLineParameter);
			    }
			    if (sLeftMostCommandLineParameter.equalsIgnoreCase("-I=")) { 
			    	log.debug("CommandLineParms: Input File");
			    }
			    if (sLeftMostCommandLineParameter.equalsIgnoreCase(sCommandLineParmsOutput)) {
			    	log.debug("CommandLineParms: Output File");
			    	bOutputFile = Boolean.TRUE;
			    	sOutputFileName = sCommandLineParameter.replace(sCommandLineParmsOutput, "");
			    }
			    if (sLeftMostCommandLineParameter.equalsIgnoreCase("-?")) { 
			    	log.debug("CommandLineParms: Help");
			    }
			    if (sLeftMostCommandLineParameter.equalsIgnoreCase("-H")) {
			    	log.debug("CommandLineParms: Help");
			    }
			    if (sLeftMostCommandLineParameter.equalsIgnoreCase("-H=")) {
			    	log.debug("CommandLineParms: Help");
			    }
			    if (sLeftMostCommandLineParameter.equalsIgnoreCase("dffdf")) {
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
