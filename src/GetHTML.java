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

	static Logger				log										= Logger.getLogger(GetHTML.class.getName());
    public static final String	ClassNameJdbcMySQL						= "com.mysql.jdbc.Driver";
    public static final String	ClassNameP6SpyJdbcMySQL					= "com.p6spy.engine.spy.P6SpyDriver";
    
	public static final String 	sUrlYahooStockSymbol					= "$$$SYMBOL$$$";
	public static final String 	sUrlYahooStartingMonth					= "$$$STARTMONTH$$$";
	public static final String 	sUrlYahooStartingYear					= "$$$STARTYEAR$$$";
	public static final String 	sUrlYahooStartingDay					= "$$$STARTDAY$$$";
	public static final String 	sUrlYahooEndingMonth					= "$$$ENDMONTH$$$";
	public static final String 	sUrlYahooEndingYear						= "$$$ENDYEAR$$$";
	public static final String 	sUrlYahooEndingDay						= "$$$ENDDAY$$$";
		
	public static final String 	sUrlYahooFinanceHistorical 				= "http://ichart.finance.yahoo.com";
	//																													  &a=9(zero based October=9, November=10)
	public static final	String	sUrlYahooFinanceIntelHistorical 		= sUrlYahooFinanceHistorical + "/table.csv?s=INTC&a=9&b=31&c=2014";  										   
	public static String		sUrlYahooFinanceHistoricalGeneric 		= sUrlYahooFinanceHistorical + "/table.csv?s=" + sUrlYahooStockSymbol + "&d=" + sUrlYahooEndingMonth + "&e=" + sUrlYahooEndingDay + "&f=" + sUrlYahooEndingYear + "&g=d&a=" + sUrlYahooStartingMonth + "&b=" + sUrlYahooStartingDay + "&c=" + sUrlYahooStartingYear + "&ignore=.csv"; 
	public static final	String	sUrlYahooFinanceStockName				= "http://download.finance.yahoo.com/d/quotes.txt?s=AIG&f=n&e=.csv";
	public static final	String	sURL									= sUrlYahooFinanceStockName;
	
	public static final	String	sEmptyString 							= "";
	public static final String	sUrlYahooHistoricalHeader 				= "Date,Open,High,Low,Close,Volume,Adj Close";
	
	public static final	String sLeftThreeHTTP 					= "HTT";
	public static final	String sCommandLineParmsOutputSysOut	= "SYSOUT";	
	public static final	String sCommandLineParmsUrls			= "-U=";
	public static final	String sCommandLineParmsOutputFile 		= "-O=";
	public static final	String sCommandLineParmsDateFormat 		= "-D=";
	public static final	String sCommandLineParmsInput 			= "-I=";		// NOT supported
	public static final	String sCommandLineParmsHelp 			= "-H=";		// NOT supported
	public static final	String sCommandLineParmsHelpAlt 		= "-?=";		// NOT supported
	public static final	String sCommandLineParmsOutputDB 		= "-B=";		// NOT supported
	
	private static		String 	sOutputFileName 				= "";
	private static		Boolean bOutputFile 					= Boolean.FALSE;
	private static		Boolean bOutputDB	 					= Boolean.FALSE;
	private static		Boolean bOutputFileWithTimeStamp 		= Boolean.FALSE;
	private static		Boolean bTraceSqlWithP6Spy 				= Boolean.TRUE;
	
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
	
	public static				String 				StockSymbol			= "";
	
	//jdbc:p6spy:mysql://localhost/feedback?" + "user=sqluser&password=sqluserpw

	protected static String setsFileNameDateFormatYahooFinance(
			String sFileNameDateFormatYahooFinance, String sSymbol,  String sMonth, String sDay, String sYear) {
		String sLocalFileNameDateFormatYahooFinance;
		sLocalFileNameDateFormatYahooFinance = sFileNameDateFormatYahooFinance.replace(sUrlYahooStockSymbol, sSymbol);
		sLocalFileNameDateFormatYahooFinance = sLocalFileNameDateFormatYahooFinance.replace(sUrlYahooEndingDay, sDay);
		sLocalFileNameDateFormatYahooFinance = sLocalFileNameDateFormatYahooFinance.replace(sUrlYahooStartingDay, sDay);
		sLocalFileNameDateFormatYahooFinance = sLocalFileNameDateFormatYahooFinance.replace(sUrlYahooEndingMonth, sMonth);
		sLocalFileNameDateFormatYahooFinance = sLocalFileNameDateFormatYahooFinance.replace(sUrlYahooStartingMonth, sMonth);
		sLocalFileNameDateFormatYahooFinance = sLocalFileNameDateFormatYahooFinance.replace(sUrlYahooEndingYear, sYear);
		sLocalFileNameDateFormatYahooFinance = sLocalFileNameDateFormatYahooFinance.replace(sUrlYahooStartingYear, sYear);
		return(sLocalFileNameDateFormatYahooFinance);
	}	
	
	protected static void setsFileNameDateFormatYahooFinance(
			String sFileNameDateFormatYahooFinance, String sSymbol,  String sStartingMonth, String sStartingDay, String sStartingYear, String sEndingMonth,String sEndingDay, String sEndingYear) {
		GetHTML.sFileNameDateFormatYahooFinance = sFileNameDateFormatYahooFinance.replaceAll(sUrlYahooStockSymbol, sSymbol);
		GetHTML.sFileNameDateFormatYahooFinance = sFileNameDateFormatYahooFinance.replaceAll(sUrlYahooEndingDay, sEndingDay);
		GetHTML.sFileNameDateFormatYahooFinance = sFileNameDateFormatYahooFinance.replaceAll(sUrlYahooStartingDay, sStartingDay);
		GetHTML.sFileNameDateFormatYahooFinance = sFileNameDateFormatYahooFinance.replaceAll(sUrlYahooEndingMonth, sEndingMonth);
		GetHTML.sFileNameDateFormatYahooFinance = sFileNameDateFormatYahooFinance.replaceAll(sUrlYahooStartingMonth, sStartingMonth);
		GetHTML.sFileNameDateFormatYahooFinance = sFileNameDateFormatYahooFinance.replaceAll(sUrlYahooEndingYear, sEndingYear);
		GetHTML.sFileNameDateFormatYahooFinance = sFileNameDateFormatYahooFinance.replaceAll(sUrlYahooStartingYear, sStartingYear);

	}	
	/**
	 * @return the bOutputDB
	 */
	protected static Boolean getbOutputDB() {
		return bOutputDB;
	}

	/**
	 * @param bOutputDB the bOutputDB to set
	 */
	protected static void setbOutputDB(Boolean bOutputDB) {
		GetHTML.bOutputDB = bOutputDB;
	}

	/**
	 * @return the bOutputFileWithTimeStamp
	 */
	protected static Boolean getbOutputFileWithTimeStamp() {
		return bOutputFileWithTimeStamp;
	}

	/**
	 * @param bOutputFileWithTimeStamp the bOutputFileWithTimeStamp to set
	 */
	protected static void setbOutputFileWithTimeStamp(
			Boolean bOutputFileWithTimeStamp) {
		GetHTML.bOutputFileWithTimeStamp = bOutputFileWithTimeStamp;
	}
	
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
	
	public void previousBusinessDay()
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
		String JdbcConnectString = "jdbc:p6spy:mysql://localhost/feedback?" + "user=sqluser&password=sqluserpw";
		
	    try {
	      // this will load the MySQL driver, each DB has its own driver
	    	if (bTraceSqlWithP6Spy) {
	    		Class.forName(ClassNameP6SpyJdbcMySQL);
	    	}
	    	else{
	    		Class.forName(ClassNameJdbcMySQL);
	    		JdbcConnectString = JdbcConnectString.replace("p6spy:", "");
	    	}
	      // setup the connection with the DB.
	    	
	      connect = DriverManager.getConnection(JdbcConnectString); //"user=root&password=12many");	      
	      preparedStatement = connect.prepareStatement("CREATE TABLE HistoricalStockPrice2 (MarketDate Date not null, OpenPrice DECIMAL(18,4), HighPrice DECIMAL(18,4), Low DECIMAL(18,4), Close DECIMAL(18,4), Volume	BIGINT, AdjClose DECIMAL(18,4), Symbol varchar(5))");
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

	public void writeDbInsertHistoricalStockPrice(String sInsertValues) throws Exception {
		
		String JdbcConnectString = "jdbc:p6spy:mysql://localhost/feedback?" + "user=sqluser&password=sqluserpw";		
		String sSqlStatement = "";
		
		try {
	      // this will load the MySQL driver, each DB has its own driver
	      //Class.forName("com.mysql.jdbc.Driver");
	      //Class.forName("com.p6spy.engine.spy.P6SpyDriver");
	    	if (bTraceSqlWithP6Spy) {
	    		Class.forName(ClassNameP6SpyJdbcMySQL);
	    	}
	    	else{
	    		Class.forName(ClassNameJdbcMySQL);
	    		JdbcConnectString = JdbcConnectString.replace("p6spy:", "");
	    	}
	      // setup the connection with the DB.
	      connect = DriverManager.getConnection(JdbcConnectString); //"user=root&password=12many");
	      sSqlStatement =  "INSERT INTO HistoricalStockPrice ";
	      sSqlStatement += " (MarketDate, OpenPrice, HighPrice, Low, Close, Volume, AdjClose, Symbol) ";
	      sSqlStatement += " VALUES ";
	      sSqlStatement += " ( " + sInsertValues + " ) " ;
	      preparedStatement = connect.prepareStatement(sSqlStatement);
	      preparedStatement.executeUpdate();

	      
	    } catch (Exception e) {
	    	//if (e.vendorCode == 1050)
	    	{
	    		log.error("error with this SQL prepare or execute " + sSqlStatement);
	    		log.error("SQL vendorCode=1050 " + e.getMessage());
	    	}
	    	throw e;
	    } finally {
	      close();
	    }

	  }	
	
		// you need to close all three to make sure
		private void close() {
			
			close(resultSet);
			close(statement);
			close(connect);

		}
		  
		  private void close(ResultSet c) {
		    try {
		      if (c != null) {
		        c.close();
		      }
		    } catch (Exception e) {
		    // don't throw now as it might leave following closables in undefined state
		    }
		  }	

		  private void close(Statement c) {
			    try {
			      if (c != null) {
			        c.close();
			      }
			    } catch (Exception e) {
			    // don't throw now as it might leave following closables in undefined state
			    }
			  }
		  
		  private void close(Connection c) {
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
		log.debug("File Name=sPathAndFileName=" + sPathAndFileName);
		BufferedWriter writer = null;
		try
		{
		    writer = new BufferedWriter( new FileWriter( sPathAndFileName, true));
		    writer.write( sDataToWriteToFile);
		    writer.newLine();

		}
		catch ( IOException e)
		{
			log.error("Error New or write to file " + e.getMessage() + " sPathAndFileName=" + sPathAndFileName);
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
		    	log.error("Error closing file " + e.getMessage() + " sPathAndFileName=" + sPathAndFileName);
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

			if ((bOutputDB) && (sLocalURL.contains(sUrlYahooFinanceHistorical)) )
			{
				StockSymbol = getStockSymbol(sLocalURL);
				log.debug("Begin out->DB ");
				try {
					String sOneLineInserts = sHTTpResponseLines;
					sOneLineInserts = sOneLineInserts.replaceAll(sUrlYahooHistoricalHeader, "");
					sOneLineInserts = sOneLineInserts + "," + StockSymbol;
					//1/0
					sOneLineInserts = sOneLineInserts.replaceAll("\n", "' , '");
					sOneLineInserts = sOneLineInserts.replaceAll("\r", "' , '");
					sOneLineInserts = " '" + sOneLineInserts.replaceAll(",", "','") + "' ";
					writeDbInsertHistoricalStockPrice(sOneLineInserts);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e);
					e.printStackTrace();
				}
				log.debug("End  out->DB ");
			}
		}
				
	}

	private String getStockSymbol(String sURLwithStockSymbol) {
		// TODO Auto-generated method stub
		//http://download.finance.yahoo.com/d/quotes.txt?s=AIG&f=n&e=.csv"
		String Stock = "";
		String QueryStringStartingStockSymbol = "s=";
		
		if (sURLwithStockSymbol.toLowerCase().contains(QueryStringStartingStockSymbol.toLowerCase())) {
			Integer BeginStock = sURLwithStockSymbol.toLowerCase().indexOf(QueryStringStartingStockSymbol);
		
			String QueryStringEndingStockSymbol = "&";
			Integer EndStock = sURLwithStockSymbol.toLowerCase().indexOf(QueryStringEndingStockSymbol);
			Stock = sURLwithStockSymbol.substring(BeginStock+QueryStringStartingStockSymbol.length(), EndStock);
		}
		return Stock;
	}

	private static boolean validURL(String sCommandLineParameter) {
		// TODO Auto-generated method stub
		if ( (sCommandLineParameter.substring(0,3).equalsIgnoreCase(sCommandLineParmsUrls)) || (sCommandLineParameter.substring(0,3).equalsIgnoreCase(sLeftThreeHTTP)) )
		{
			return true;
		}
		return false;
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
			Integer iCommandLineParameterLength = sCommandLineParameter.length();
			Integer iSubStringLength = 3;
			if (iSubStringLength > iCommandLineParameterLength)
			{
				iSubStringLength = iCommandLineParameterLength;
			}
			String sLeftMostCommandLineParameter = sCommandLineParameter.substring(0,iSubStringLength);
			if (sLeftMostCommandLineParameter.equalsIgnoreCase(sLeftThreeHTTP))
			{
				sCommandLineParameter = sCommandLineParmsUrls + sCommandLineParameter;
				sLeftMostCommandLineParameter = sCommandLineParameter.substring(0,iSubStringLength);
			}

			    if (sLeftMostCommandLineParameter.equalsIgnoreCase(sCommandLineParmsUrls)) { 
			    	log.debug("CommandLineParms: URLs 3 left most chars sLeftMostCommandLineParameter=" + sLeftMostCommandLineParameter );
			    	log.debug("CommandLineParms: URLs sCommandLineParameter=" + sCommandLineParameter);
			    }
			    if (sLeftMostCommandLineParameter.equalsIgnoreCase("-I=")) { 
			    	log.debug("CommandLineParms: Input File");
			    }
			    if (sLeftMostCommandLineParameter.equalsIgnoreCase(sCommandLineParmsOutputFile)) {
			    	log.debug("CommandLineParms: Output File");
			    	bOutputFile = Boolean.TRUE;
			    	sOutputFileName = sCommandLineParameter.replace(sCommandLineParmsOutputFile, "");
			    }
			    if (sLeftMostCommandLineParameter.equalsIgnoreCase("-B=")) {
			    	log.debug("CommandLineParms: Output DB");
			    	bOutputDB = Boolean.TRUE;
			    }			    
			    if ( (sLeftMostCommandLineParameter.equals("-?")) || (sLeftMostCommandLineParameter.equalsIgnoreCase("-H")) ) { 
			    	log.debug("CommandLineParms: Help");
			    }
			    if (sLeftMostCommandLineParameter.equalsIgnoreCase(sCommandLineParmsDateFormat)) {
			    	log.debug("Append GUID or TimeStamp to the end of the filename");
			    	sFileNameDateFormat =sCommandLineParameter.replace(sCommandLineParmsDateFormat, "");
			    }
			    if (sLeftMostCommandLineParameter.equalsIgnoreCase("XXX")) {
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
		
		//getHtmlgetPage.connect();
		try {
			//getHtmlgetPage.createTables();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			for(StackTraceElement stackTrace: stackTraceElements){
			    log.error(stackTrace.getClassName()+ "  "+ stackTrace.getMethodName()+" "+stackTrace.getLineNumber());
			}
			e.printStackTrace();
		}
		
		for (String sCommandLineParameter: sListURLs) {
			log.info("Time: Staring getHTML (main) Starting " + sCommandLineParameter );
			if (validURL(sCommandLineParameter))
			{
				getHtmlgetPage.getHTML(sCommandLineParameter);
			}
			log.info("Time: Ending  getHTML (main) Ending   " + sCommandLineParameter);
		}
		
		Calendar TodaysDate = Calendar.getInstance();
		
		Calendar cal = Calendar.getInstance();  
		Integer iYear = cal.get(cal.YEAR);  
		Integer iMonth = cal.get(cal.MONTH); //zero-based  
		Integer iDay = cal.get(cal.DAY_OF_MONTH); 
		
		String sYear = iYear.toString();
		String sMonth = iMonth.toString();
		String sDay = iDay.toString();
		
		StockSymbol = "INTC";
		String sLocalUrlYahooFinanceIntelHistoricalGeneric = setsFileNameDateFormatYahooFinance(sUrlYahooFinanceHistoricalGeneric, StockSymbol, sMonth, sDay, sYear );
		log.info("sLocalUrlYahooFinanceIntelHistoricalGeneric=" + sLocalUrlYahooFinanceIntelHistoricalGeneric);
		getHtmlgetPage.getHTML("-U=" + sLocalUrlYahooFinanceIntelHistoricalGeneric);
		
		StockSymbol = "AIG";
		sLocalUrlYahooFinanceIntelHistoricalGeneric = setsFileNameDateFormatYahooFinance(sUrlYahooFinanceHistoricalGeneric, StockSymbol, sMonth, sDay, sYear );
		log.info("sLocalUrlYahooFinanceIntelHistoricalGeneric=" + sLocalUrlYahooFinanceIntelHistoricalGeneric);
		getHtmlgetPage.getHTML("-U=" + sLocalUrlYahooFinanceIntelHistoricalGeneric);


		log.info("Time: Ending   (main)" );
	} // main end


}
