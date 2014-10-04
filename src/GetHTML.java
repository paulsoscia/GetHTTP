import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class GetHTML {

	static String  sURL					= "http://www.bing.com/";	// A valid URL
		
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
			    	System.out.println("CommandLineParms: URLs");
			    	break;
			    case "-I=": 
			    	System.out.println("CommandLineParms: Input File");
			    	break;
			    case "-O=": 
			    	System.out.println("CommandLineParms: Output File");
			    	break;
			    case "-?": 
			    	System.out.println("CommandLineParms: Help");
			    	break;
			    case "-H": 
			    	System.out.println("CommandLineParms: Help");
			    	break;
			    case "-HE": 
			    	System.out.println("CommandLineParms: Help");
			    	break;
			    default :
			    	System.out.println("CommandLineParms: Invalid Number of Day");
		     }
		}

		return (strArrCommandLineParms);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//	Add URL as command line parameter
		//	Add Log4j
		String[] sListURLs = commandlineParms(args);
		
		GetHTML getCmdParmsPage = new GetHTML();
		for (String sCommandLineParameter: sListURLs) {
			getCmdParmsPage.getHTML(sCommandLineParameter);
		}

	} // main end

}
