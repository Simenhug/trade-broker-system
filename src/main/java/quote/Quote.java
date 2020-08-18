package quote;

import com.simen.tradesystem.securities.Equity;
import com.simen.tradesystem.securities.EquityRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class Quote {
    @Autowired
    private EquityRepository equityRepository;
    public static Map<String, Double> stockPool;
    public static Map<String, Double> optionPool;
    static {
        stockPool = new HashMap<>();
        stockPool.put("PDD", 73.11);
        stockPool.put("GE", 7.25);
        stockPool.put("SPY", 304.21);
        stockPool.put("GOOG", 1413.18);
        stockPool.put("FB", 228.58);
        stockPool.put("AAPL", 338.8);
        stockPool.put("AMZN", 2545.02);
        stockPool.put("NFLX", 418.07);
        stockPool.put("MSFT", 187.74);
        stockPool.put("INTC", 59.33);
        stockPool.put("TSLA", 999.90);
        stockPool.put("UGAZ", 10.65);

        optionPool = new HashMap<>();
        optionPool.put("SPY200619P00330000", 25.00);
        optionPool.put("SPY200808P00316000", 11.45);
        optionPool.put("AAPL200619C00340000", 5.8);
        optionPool.put("AAPL200918P00345000", 23.95);
        optionPool.put("MSFT200717C00175000", 16.45);
        optionPool.put("MSFT200717C00200000", 3.05);
        optionPool.put("SPY200821C00266000", 42.85);
        optionPool.put("SPY200821P00266000", 6.63);
        optionPool.put("SPY200706C00283000", 23.38);
    }

//    public static Double getStockLastPrice2(String symbol) throws IllegalArgumentException{
//        Double basePrice = stockPool.get(symbol);
//        if (symbol == null) {
//            throw new IllegalArgumentException("symbol not found in stock pool;");
//        }
//        Random random = new Random();
//        Double delta = random.nextDouble();
//        Double lastPrice = basePrice + (0.5 - delta)*basePrice*0.1;
//        //limit the price to 2 decimal places
//        Double answer = Double.valueOf(new DecimalFormat("#.##").format(lastPrice));
////        System.out.println("\n\n\n\n\n\n\n\n\n" + answer + "\n\n\n\n\n\n\n\n\n\n\n\n");
//        return answer;
//    }

    public static Double getStockLastPrice(String symbol) {
        String baseUrl = "http://finance.yahoo.com/q?s=";
        String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36";

        String url = baseUrl + symbol;
        Document doc  = null;
        try {
            doc = Jsoup.connect(url).userAgent(ua).timeout(10*1000).get();
        } catch (IOException e) {
            throw new IllegalArgumentException("symbol not found in stock pool;");
        }
        String price = doc.getElementsByClass("Trsdu(0.3s) Fw(b) Fz(36px) Mb(-4px) D(ib)").first().text().replaceAll(",", "");

//        System.out.println(String.format("[%s] is trading at %s", symbol, price));
        return Double.parseDouble(price);
    }

//    public static Double getOptionLastPrice2(String symbol) throws IllegalArgumentException {
//        Double basePrice = optionPool.get(symbol);
//        if (symbol == null) {
//            throw new IllegalArgumentException("symbol not found in stock poo;");
//        }
//        Random random = new Random();
//        Double delta = random.nextDouble();
//        Double lastPrice = basePrice + (0.5 - delta)*basePrice*0.1;
//        //limit the price to 2 decimal places
//        Double answer = Double.valueOf(new DecimalFormat("#.##").format(lastPrice));
//        return answer;
//    }

    public static Double getOptionLastPrice(String symbol) {
        String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36";

        // be careful, this url is not the same as the one used for stocks
        String url = String.format("http://finance.yahoo.com/quote/%s?p=%s&.tsrc=fin-srch", symbol, symbol);
        Document doc  = null;
        try {
            doc = Jsoup.connect(url).userAgent(ua).timeout(30*1000).get();
        } catch (IOException e) {
            throw new IllegalArgumentException("symbol not found in option pool;");
        }
        String price = doc.getElementsByClass("Trsdu(0.3s) Fw(b) Fz(36px) Mb(-4px) D(ib)").first().text().replaceAll(",", "");

//        System.out.println(String.format("[%s] is trading at %s", symbol, price));
        return Double.parseDouble(price);
    }

    public void changeMaintenanceRequirement(String symbol, double requirement) {
        Equity equity = equityRepository.findBySymbol(symbol);
        equity.setMaintenanceRequirement(requirement);
        equityRepository.save(equity);
    }
}
