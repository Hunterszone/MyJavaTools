package tradingapplication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConnectionToAPI {

    private static final String TOKEN = "pk_f95f594bc33048ac835258af34c1910b";
    private static final String SYMBOLS_ENDPOINT = "https://cloud.iexapis.com/stable/stock/";
    private static final String LOGOS_ENDPOINT = "https://storage.googleapis.com/iex/api/logos/";

    private String gettingResponseFromAPI(String symbol) throws FileNotFoundException {
        try {
            //URLs which hold path to api, filtered by zero symbol - TBD for all symbols in the XLSX file
            for (String companyName : TradingApplication.companyNames) {
                if (companyName.equalsIgnoreCase(ImportExcel.importSymbolsFromExcel(TradingApplication.path2).get(0))) {

                    //Define endpoints and log the output
                    String urlSymbol = SYMBOLS_ENDPOINT + symbol.toLowerCase() + "/quote?token=" + TOKEN;
                    String urlLogo = LOGOS_ENDPOINT + symbol.toUpperCase() + ".png";
                    CustomLogger log = new CustomLogger();
                    log.addToLog("Retrieving symbol for " + symbol.toUpperCase() + " from IEXTrading API: " + urlSymbol + "\n");
                    log.addToLog("Retrieving logoPNG for " + symbol.toUpperCase() + " from IEXTrading API: " + urlLogo + "\n");

                    //GET response from API
                    URL obj = new URL(urlSymbol);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    con.connect();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    String sResponse = response.toString();
                    log.addToLog("JSON response from server: " + response.toString());
//                    int code = con.getResponseCode();
//                    System.out.println("Returned HTTP status code: " + code);
                    return sResponse;
                }
            }
        } catch (MalformedURLException ex) {
            CustomLogger log = new CustomLogger();
            log.addToLog("Exception caught: " + ex);
        } catch (UnknownHostException uhe) {
            System.out.println("No connection!");
            CustomLogger log = new CustomLogger();
            log.addToLog("Exception caught: " + uhe);
        } catch (ProtocolException ex) {
            CustomLogger log = new CustomLogger();
            log.addToLog("Exception caught: " + ex);
        } catch (IOException ex) {
            CustomLogger log = new CustomLogger();
            log.addToLog("Exception caught: " + ex);
        }
        return null;
    }

    private JSONObject parsingJSON(String symbol, String response) throws ParseException {

        //parsing response in JSON object
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(response);
        JSONObject result = (JSONObject) json.get(symbol.toUpperCase());
        return result;
    }

    public Object[] connectToAPIAndParseValues(String symbol) throws JSONException, IOException {

        try {
            CustomLogger log = new CustomLogger();
            log.addToLog("Getting values for: " + symbol.toUpperCase() + "\n");
            //calling gettingresponse method and initializing response object
            String response = gettingResponseFromAPI(symbol);
            //calling method for parsing response in JSON object
            JSONObject result = parsingJSON(symbol, response);
            if (result != null) {
                log.addToLog("Adding: " + symbol.toUpperCase());
                System.out.println("Data imported");
            } else {
                log.addToLog(System.getProperty("line.separator"));
                return new Object[]{symbol.toUpperCase(), 0, 0, 0, "NO DATA"};
            }
        } catch (ParseException | FileNotFoundException e) {
            CustomLogger log = new CustomLogger();
            log.addToLog("Error! File is not found");
            log.addToLog(e.getLocalizedMessage());
        }
        //log.addToLog(System.getProperty("line.separator"));
        return new Object[]{symbol.toUpperCase(), 0, 0, 0, "NO DATA"};
    }

    public String[] extractPrices(Object symbol) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            String jsonRespBlock = gettingResponseFromAPI((String) symbol);
            json = (JSONObject) parser.parse(jsonRespBlock);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Object latestPrice = json.get("latestPrice");
        Object calculationPrice = json.get("calculationPrice");
        Object iexRealtimePrice = json.get("iexRealtimePrice");
        Object delayedPrice = json.get("delayedPrice");
        Object extendedPrice = json.get("extendedPrice");
        Object iexBidPrice = json.get("iexBidPrice");
        Object iexAskPrice = json.get("iexAskPrice");
        symbol = json.get("symbol");

        CustomLogger log = new CustomLogger();

        try {
            log.addToLog("\n");
            log.addToLog("---------------------LIST OF PRICES------------------------");
            log.addToLog(" latestPrice for " + symbol + " is: " + latestPrice);
            log.addToLog(" calculationPrice for " + symbol + " is: " + calculationPrice);
            log.addToLog(" iexRealtimePrice for " + symbol + " is: " + iexRealtimePrice);
            log.addToLog(" delayedPrice for " + symbol + " is: " + delayedPrice);
            log.addToLog(" extendedPrice for " + symbol + " is: " + extendedPrice);
            log.addToLog(" iexBidPrice for " + symbol + " is: " + iexBidPrice);
            log.addToLog(" iexAskPrice for " + symbol + " is: " + iexAskPrice);
            log.addToLog("-----------------------------------------------------------" + "\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new String[]{symbol.toString(), latestPrice.toString()};
    }
}