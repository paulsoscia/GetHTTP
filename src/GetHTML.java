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
		
		String sURL					= "";	// A valid URL
		String sHTTpResponseLine	= "";	// One line from the HTTP response
		String sHTTpResponseLines	= "";	// Entire HTTP response
		sURL = "http://www.google.com/";
		
		try {
			URL url = new URL(sURL);
			try {
				InputStream is = url.openStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				while ((sHTTpResponseLine = in.readLine()) != null) {
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
