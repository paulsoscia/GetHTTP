Basic java program that does a "get" of HTML

build class file (UNIX example) 

javac -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML.java

With Log4j support, example of running from the command line, assumes a path exists /home/paul/workspace/dev/libs
java -Dlog4j.configuration=file:"/home/paul/workspace/dev/GetHTML/properties/log4j.properties" -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML http://www.google.com/

Four ways to test program (from LINUX command line) 

(1) with command line parm
java -Dlog4j.configuration=file:"/home/paul/workspace/dev/GetHTML/properties/log4j.properties" -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML http://www.google.com/

(2) with no command line parms by default
java -Dlog4j.configuration=file:"/home/paul/workspace/dev/GetHTML/properties/log4j.properties" -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML

(3) with multiple command line parms
java -Dlog4j.configuration=file:"/home/paul/workspace/dev/GetHTML/properties/log4j.properties" -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML -U=http://www.google.com/ -U=http://www.yahoo.com/ 

(4) with multiple command line parms
java -Dlog4j.configuration=file:"/home/paul/workspace/dev/GetHTML/properties/log4j.properties" -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML http://www.google.com/ http://www.yahoo.com/

You can add an output file instead of the default (standard out // sysout) add -O command line parm example  -O=/home/paul/workspace/dev/GetHTML/output/test.txt

(A) with -O
java -Dlog4j.configuration=file:"/home/paul/workspace/dev/GetHTML/properties/log4j.properties" -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML http://download.finance.yahoo.com/d/quotes.txt?s=INTC&f=n&e=.csv http://ichart.finance.yahoo.com/table.csv?s=INTC&a=09&b=10&c=2014 http://download.finance.yahoo.com/d/quotes.txt?s=AIG&f=n&e=.csv http://ichart.finance.yahoo.com/table.csv?s=AIG&a=09&b=10&c=2014 -O=/home/paul/workspace/dev/GetHTML/output/test.txt

(B) with -O you can use -D
java -Dlog4j.configuration=file:"/home/paul/workspace/dev/GetHTML/properties/log4j.properties" -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML http://download.finance.yahoo.com/d/quotes.txt?s=INTC&f=n&e=.csv http://ichart.finance.yahoo.com/table.csv?s=INTC&a=09&b=10&c=2014 http://download.finance.yahoo.com/d/quotes.txt?s=AIG&f=n&e=.csv http://ichart.finance.yahoo.com/table.csv?s=AIG&a=09&b=10&c=2014 -O=/home/paul/workspace/dev/GetHTML/output/test.txt -D=MMddyyyy_HH:mm:ss:SSS

URLs

Example 
http://ichart.finance.yahoo.com/table.csv?s=AIG&a=06&b=9&c=1986&d=2&e=5&f=2008&g=d

Query String variables  (s, a, b,c, d, e, f, and g) 
s - This is where you can specify your stock quote, if you want to download stock quote for Intel, just enter it as 's=INTC' (replacing AIG in the URL above) 
Start Date  (a, b, and c) 
a - This parameter is to get the input for the start month. '00' is for January, '01' is for February and so on.
b - This parameter is to get the input for the start day, this one quite straight forward, '1' is for day one of the month, '2' is for second day of the month and so on.
c - This parameter is to get the input for the start year
End Date  (d, e, and f) (optional; defaults to the current date) 
d - This parameter is to get the input for end month, and again '00' is for January, '02' is for February and so on.
e - This parameter is to get the input for the end day
f - This parameter is to get the input for the end year
g - Optional.  This parameter is to specify the interval of the data you want to download. 'd' is for daily, 'w' is for weekly and 'm' is for monthly prices. The default is 'daily' if you ignore this parameter.

Example

http://download.finance.yahoo.com/d/[FILENAME]?s=[TICKERSYMBOLS]&f=[TAGS]&e=.csv 



example
java -Dlog4j.configuration=file:"/home/paul/workspace/dev/GetHTML/properties/log4j.properties" -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML http://download.finance.yahoo.com/d/quotes.txt?s=INTC&f=n&e=.csv http://ichart.finance.yahoo.com/table.csv?s=INTC&a=09&b=10&c=2014 http://download.finance.yahoo.com/d/quotes.txt?s=AIG&f=n&e=.csv http://ichart.finance.yahoo.com/table.csv?s=AIG&a=09&b=10&c=2014 -O=/home/paul/workspace/dev/GetHTML/output/test.txt -D=MMddyyyy_HH:mm:ss:SSS



