# API Extractor

### HOW TO USE
* **IMPORTANT** - make sure you're not behind a proxy, otherwise it could be that some data is not fetched properly or NullPointerException is obtained
* Create **Symbol.xlsx** under the desired location (an example file is packed within the root folder)
* Fill out the first cell (upper left corner) of the Symbol.xlsx file with a supported symbol, as you can also add additional symbols below this one - **one per cell**  
* **Save** and **close** the file  
* Navigate to the file location with the "Choose" button    
* Select the Symbol.xlsx file and click on "Get report"  
* Logfile, called **apiLog.json**, containing a PriceList for the current symbol(s), is being generated on your Desktop  
* It contains also the JSON object, as well as the API URLs, based on the symbol(s)

### FEATURES
* The implementation of the "Show partner logo" button allows you to retrieve randomly the PNG logos for all supported symbols real-time, as they get streamed over the network, along with the respective logo URL
* Added checks if there is a file on the path, if exactly Symbol.xlsx is selected and if Symbol.xlsx is opened in the root dir (the last one works properly **only if the .JAR-file and Symbol.xlsx are both inside the same dir**)  

### SOURCES
* The app supports ca. 30 symbols/companies of choice: https://www.fool.com/investing/2017/12/05/the-30-largest-companies-on-the-stock-market.aspx
* API location: https://api.iextrading.com/1.0/stock/{{supported_symbol_toLower}}/quote
* Images location: https://storage.googleapis.com/iex/api/logos/{{supported_symbol_toUpper}}.png
