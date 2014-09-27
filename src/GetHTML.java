import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class GetHTML {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//	Add URL as command line parameter
		//	Add Log4j
		
		String sURL					= "http://www.google.com/";	// A valid URL
		String sHTTpResponseLine	= "";	// One line from the HTTP response
		String sHTTpResponseLines	= "";	// Entire HTTP response
		
		try {
			URL urlSiteToGetHTML = new URL(sURL);
			try {
				InputStream isGetHtmlData = urlSiteToGetHTML.openStream();
				BufferedReader brGetHtmlData = new BufferedReader(new InputStreamReader(isGetHtmlData));
				while ((sHTTpResponseLine = brGetHtmlData.readLine()) != null) {
					sHTTpResponseLines += sHTTpResponseLine;
				}

			} 
			catch (IOException e) {
				System.out.println("Error:  Problem with url " + sURL + ". IOException");
			}
		}
		catch (MalformedURLException e) {
			System.out.println("Error:  Problem with url " + sURL + ". MalformedURLException");
		}
		System.out.println("sURL=" + sURL);
		System.out.println("success");
		System.out.println(sHTTpResponseLines);

	} // main end

}
