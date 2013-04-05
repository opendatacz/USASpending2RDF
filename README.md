USASpending tarql mapping
=========================

Mapping for CSV data from [USASpending](http://usaspending.gov/data) to RDF via [tarql](https://github.com/cygri/tarql).

Use
---

Download a [release of tarql](http://lab.linkeddata.deri.ie/2013/tarql/), extract and use the script `bin/tarql` to execute the transformation. Invoke the transformation using the mapping (`mapping.sparql`) from command line:

```bash
bin/tarql mapping.sparql USASpending.csv
```

Issues
------

* Since tarql doesn't support streaming processing, you may encounter Java's `OutOfMemoryError` when working with bigger data on a smaller machine. When you're dealing with larger CSV's, consider increasing Java heap space (e.g., `set JAVA_OPTS="-Xms128m -Xmx1024m"`) or splitting the source data into multiple files to be processed separately. 
