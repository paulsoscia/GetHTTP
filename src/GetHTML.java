import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class GetHTML {

	static String  sURL					= "http://www.yahoo.com/";	// A valid URL
	
	public GetHTML() {
		this(sURL);
	}
	
	public GetHTML(String sLocalURL) {

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
				System.out.println("Error:  Problem reading HTML, with url " + sLocalURL + ". IOException");
			}
		}
		catch (MalformedURLException e) {
			iReturn = 0;
			System.out.println("Error:  Problem with url " + sLocalURL + ". MalformedURLException");
		}
		
		System.out.println("Completed: sURL=" + sLocalURL);
		if (iReturn == 1)
		{
			System.out.println("Completed: HTTP get was successful");
			System.out.println("Completed: response length=" + sHTTpResponseLines.length());
			System.out.println("Completed: " + sHTTpResponseLines);
		}
				
	}
	
	public static void commandlineParms(String[] args)
	{
		
		for (String sCommandLineParameter: args) {
			sCommandLineParameter = sCommandLineParameter.trim().toUpperCase();
			String sLeftMostCommandLineParameter = sCommandLineParameter.substring(0,3);
			if (sLeftMostCommandLineParameter.equals("HTT"))
			{
				sCommandLineParameter = "-U=" + sCommandLineParameter;
				sLeftMostCommandLineParameter = sCommandLineParameter.substring(0,3);
			}
		    switch(sLeftMostCommandLineParameter){
			    case "-U=": 
			    	System.out.println("URLs");
			    	break;
			    case "-I=": 
			    	System.out.println("Input File");
			    	break;
			    case "-O=": 
			    	System.out.println("Input File");
			    	break;
			    default :
			    	System.out.println("Invalid Number of Day");
		     }
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//	Add URL as command line parameter
		//	Add Log4j
		commandlineParms(args);
		if (args.length == 0) {
			// no/none command line parameters were provided
			GetHTML getGooglePage = new GetHTML();			
		}
		else {
			// command line parameters were provided
			for (String sCommandLineParameter: args) {
				GetHTML getCmdParmsPage = new GetHTML(sCommandLineParameter);
			}
		}

	} // main end

}
