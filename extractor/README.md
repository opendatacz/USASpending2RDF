USASpending CSV splitter
=========================

Splitting CSV file to smaller files to process by TARQL

Use
---

Download CSV file, set correct values in "settings.txt"
 
 [source] -> url        path to the csv
 [system] -> exec       commant to execute on each fragment (use $1 as substitute)

run:
java -jar Extractor.jar

