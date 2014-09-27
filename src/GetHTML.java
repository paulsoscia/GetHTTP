import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class GetHTML {

	String sURL					= "http://www.google.com/";	// A valid URL
	
	public GetHTML() {

		String sHTTpResponseLine	= "";	// One line from the HTTP response
		String sHTTpResponseLines	= "";	// Entire HTTP response
		Integer iReturn = 0;
		
		try {
			iReturn = 1;
			URL urlSiteToGetHTML = new URL(sURL);
			try {
				InputStream isGetHtmlData = urlSiteToGetHTML.openStream();
				BufferedReader brGetHtmlData = new BufferedReader(new InputStreamReader(isGetHtmlData));
				while ((sHTTpResponseLine = brGetHtmlData.readLine()) != null) {
					sHTTpResponseLines += sHTTpResponseLine;
				}

			} 
			catch (IOException e) {
				iReturn = 0;
				System.out.println("Error:  Problem reading HTML, with url " + sURL + ". IOException");
			}
		}
		catch (MalformedURLException e) {
			iReturn = 0;
			System.out.println("Error:  Problem with url " + sURL + ". MalformedURLException");
		}
		
		System.out.println("Completed: sURL=" + sURL);
		if (iReturn == 1)
		{
			System.out.println("Completed: HTTP get was successful");
			System.out.println("Completed: response length=" + sHTTpResponseLines.length());
			System.out.println("Completed: " + sHTTpResponseLines);
		}
				
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//	Add URL as command line parameter
		//	Add Log4j
		GetHTML getGooglePage = new GetHTML();

	} // main end

}
