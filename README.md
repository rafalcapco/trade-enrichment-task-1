### Running the project

You can use IntelliJ run configuration 'TradeEnrichmentServiceApplication'

### Using the API

Once the application is started, you can use the API via terminal:

```
curl -F "file=@<path_to_file>" -o <output_file_name> http://localhost:8080/api/v1/enrich
```
Example:
```
curl -F "file=@C:\dev\tmp\largeTrade.csv" -o modified.csv http://localhost:8080/api/v1/enrich
```

### Limitations of the code

Processing ~10 million records was taking around 1 minute and 40 seconds on my machine.