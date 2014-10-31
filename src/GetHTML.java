import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class GetHTML {

	static Logger	log						= Logger.getLogger(GetHTML.class.getName());
	//static String	sURL					= "http://www.bing.com/";	// A valid URL
	//																				&a=09(zero based October) &b=8 (8th and later)
	//
	
	public static final	String	sUrlYahooFinanceIntelHistorical = "http://ichart.finance.yahoo.com/table.csv?s=INTC&a=09&b=8&c=2014";
	public static final	String	sUrlYahooFinanceStockName		= "http://download.finance.yahoo.com/d/quotes.txt?s=AIG&f=n&e=.csv";
	public static final	String	sURL							= sUrlYahooFinanceStockName;
	public static final	String	sEmptyString = "";
	
	public static final	String sLeftThreeHTTP 					= "HTT";
	public static final	String sCommandLineParmsOutputSysOut	= "SYSOUT";	
	public static final	String sCommandLineParmsUrls			= "-U=";
	public static final	String sCommandLineParmsOutput 			= "-O=";
	public static final	String sCommandLineParmsDateFormat 		= "-D=";
	public static final	String sCommandLineParmsInput 			= "-I=";	// NOT supported
	public static final	String sCommandLineParmsHelp 			= "-H";		// NOT supported
	
	private static		String 	sOutputFileName 				= "";
	private static		Boolean bOutputFile 					= Boolean.FALSE;
	private static		Boolean bOutputFileWithTimeStamp 		= Boolean.FALSE;
	
	private static		String		sFileNameDateFormat			= 	"yyyyMMdd_HH:mm:ss:SSS";
	private static		Date   		dtFileNameDate				=	new Date();
	private static		DateFormat	dfFileNameDateFormat		=	new SimpleDateFormat(sFileNameDateFormat);
	
	private static		String		sFileNameDateFormatYahooFinance			= 	"MM/d/yyyy";
	private static		Date   		dtFileNameDateYahooFinance				=	new Date();
	private static		DateFormat	dfFileNameDateFormatYahooFinance		=	new SimpleDateFormat(sFileNameDateFormat);
	
	private 			Connection			connect 			= null;
	private 			Statement 			statement 			= null;
	private 			PreparedStatement 	preparedStatement 	= null;
	private 			ResultSet 			resultSet 			= null;
	
	public static String getsFileNameDateFormat() {
		return sFileNameDateFormat;
	}

	public static void setsFileNameDateFormat(String sFileNameDateFormat) {
		GetHTML.sFileNameDateFormat = sFileNameDateFormat;
	}

	public static Date getDtFileNameDate() {
		return dtFileNameDate;
	}

	public static void setDtFileNameDate(Date dtFileNameDate) {
		GetHTML.dtFileNameDate = dtFileNameDate;
	}

	public static DateFormat getDfFileNameDateFormat() {
		return dfFileNameDateFormat;
	}

	public static void setDfFileNameDateFormat(DateFormat dfFileNameDateFormat) {
		GetHTML.dfFileNameDateFormat = dfFileNameDateFormat;
	}
	
	public static String getsOutputFileName() {
		return sOutputFileName;
	}

	public static void setsOutputFileName(String sOutputFileName) {
		GetHTML.sOutputFileName = sOutputFileName;
	}

	public static Boolean getbOutputFile() {
		return bOutputFile;
	}

	public static void setbOutputFile(Boolean bOutputFile) {
		GetHTML.bOutputFile = bOutputFile;
	}
	
	public void LastBusinessDay()
	{
		//TODO add holiday support ; add before 5PM logic to check previous business day
		Boolean isHoliday; 
		//List<Date> holidays = dia.getAllNationalHolidays();

		// get the current date without the hours, minutes, seconds and millis
		Calendar cal = Calendar.getInstance();		
		
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		if (dayOfWeek != Calendar.SUNDAY) {
			cal.add(Calendar.DAY_OF_YEAR, -2);
		}
		if (dayOfWeek != Calendar.SATURDAY) {
			cal.add(Calendar.DAY_OF_YEAR, -1);
		}
		
	}
	
	public void createTables() throws Exception {
	    try {
	      // this will load the MySQL driver, each DB has its own driver
	      Class.forName("com.mysql.jdbc.Driver");
	      // setup the connection with the DB.
	      connect = DriverManager.getConnection("jdbc:mysql://localhost/feedback?" + "user=sqluser&password=sqluserpw"); //"user=root&password=12many");

	      
	      // preparedStatements can use variables and are more efficient
	      preparedStatement = connect.prepareStatement("create table mytable (a integer, b varchar(100), c long, prime varchar(2), tictac varchar(9) )");
	      // "myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
	      // parameters start with 1
	      preparedStatement.executeUpdate();

	      
	    } catch (Exception e) {
	    	//if (e.vendorCode == 1050)
	    	{
	    		log.error("SQL vendorCode=1050 " + e.getMessage());
	    	}
	    	throw e;
	    } finally {
	      close();
	    }

	  }
		

		  // you need to close all three to make sure
		  private void close() {
		    //close(resultSet);
		    //close(statement);
		    //close(connect);
		  }
		  
		  public void close(Closeable c) {
		    try {
		      if (c != null) {
		        c.close();
		      }
		    } catch (Exception e) {
		    // don't throw now as it might leave following closables in undefined state
		    }
		  }	
	// END   JDBC
	
	public void writeFile(String sPathAndFileName, String sDataToWriteToFile) {
	
		dfFileNameDateFormat = new SimpleDateFormat(sFileNameDateFormat);
		dfFileNameDateFormatYahooFinance = new SimpleDateFormat(sFileNameDateFormatYahooFinance);
		sPathAndFileName = sPathAndFileName.replace(".", dfFileNameDateFormat.format(dtFileNameDate) + ".");
		
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
			e.printStackTrace();
		}
		finally
		{
		    try
		    {
		        if ( writer != null)
		        	writer.close();
		    }
		    catch ( IOException e)
		    {
		    	log.error("Error closing file " + e.getMessage());
		    	e.printStackTrace();
		    }
		}
	}
	
	public void getHTML(String sLocalURL) {

		String sHTTpResponseLine	= "";	// One line from the HTTP response
		String sHTTpResponseLines	= "";	// Entire HTTP response
		Integer iReturn = 0;
		
		if (sLocalURL.substring(0,sCommandLineParmsUrls.length()).equals(sCommandLineParmsUrls))
		{
			sLocalURL = sLocalURL.replace(sCommandLineParmsUrls, "");
		}
		try {
			iReturn = 1;
			URL urlSiteToGetHTML = new URL(sLocalURL);
			try {
				InputStream isGetHtmlData = urlSiteToGetHTML.openStream();
				BufferedReader brGetHtmlData = new BufferedReader(new InputStreamReader(isGetHtmlData));
				while ((sHTTpResponseLine = brGetHtmlData.readLine()) != null) {
					sHTTpResponseLines += sHTTpResponseLine;
				}
				if (sHTTpResponseLines.equals(""))
				{
					sHTTpResponseLines = sHTTpResponseLine;
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
		
		log.debug("Completed:getHTML sLocalURL=" + sLocalURL  + " iReturn=" + iReturn + "(1 is successful)");
		if (iReturn == 1)
		{
			log.debug("Completed:getHTML HTTP get was successful");
			log.debug("Completed:getHTML response length=" + sHTTpResponseLines.length());
			if (! bOutputFile)
			{
				log.debug("Begin out->sysout");
				//System.out.println( sHTTpResponseLines.replaceAll("\"", ""));
				System.out.println( sHTTpResponseLines);
				log.debug(sHTTpResponseLines);
				log.debug("End   out->sysout");
			}
			if (bOutputFile)
			{
				log.debug("Begin out->file sOutputFileName=" + sOutputFileName);
				writeFile(sOutputFileName, sHTTpResponseLines);
				log.debug("End  out->file sOutputFileName=" + sOutputFileName);
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
	    	log.debug("no command line parms was sent to the program");
	    	log.debug("using the default command line parms with default URLS=" + sURL);
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
			    	log.debug("CommandLineParms: URLs 3 left most chars sLeftMostCommandLineParameter=" + sLeftMostCommandLineParameter );
			    	log.debug("CommandLineParms: URLs sCommandLineParameter=" + sCommandLineParameter);
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
			    if (sLeftMostCommandLineParameter.equalsIgnoreCase(sCommandLineParmsDateFormat)) {
			    	log.debug("Append GUID or TimeStamp to the end of the filename");
			    	sFileNameDateFormat =sCommandLineParameter.replace(sCommandLineParmsDateFormat, "");
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
		log.info("Time: Starting (main)" );
		
		String[] sListURLs = commandlineParms(args);
		GetHTML getHtmlgetPage = new GetHTML();
		
		for (String sCommandLineParameter: sListURLs) {
			log.info("Time: Staring getHTML (main) Starting " + sCommandLineParameter );
			getHtmlgetPage.getHTML(sCommandLineParameter);
			log.info("Time: Ending  getHTML (main) Ending   " + sCommandLineParameter);
		}
		log.info("Time: Ending   (main)" );
	} // main end

}
