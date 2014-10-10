Basic java program that does get of HTML


With Log4j support, example of running from the command line, assumes a path exists /home/paul/workspace/dev/libs
java -Dlog4j.configuration=file:"/home/paul/workspace/dev/GetHTML/log/log4j.properties" -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML http://www.google.com/

Three ways to test program

(1) with command line parm
java -Dlog4j.configuration=file:"/home/paul/workspace/dev/GetHTML/log/log4j.properties" -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML http://www.google.com/

(2) with no command line parms by default
java -Dlog4j.configuration=file:"/home/paul/workspace/dev/GetHTML/log/log4j.properties" -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML

(3) with multiple command line parms
java -Dlog4j.configuration=file:"/home/paul/workspace/dev/GetHTML/log/log4j.properties" -classpath /home/paul/workspace/dev/GetHTML/libs/log4j-1.2.17.jar:. GetHTML http://www.google.com/ http://www.yahoo.com/ 

