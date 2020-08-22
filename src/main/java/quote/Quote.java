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


    public static Double getStockLastPrice(String symbol) {
        String baseUrl = "http://finance.yahoo.com/q?s=";
        String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36";

        String url = baseUrl + symbol;
        Document doc  = null;
        try {
            doc = Jsoup.connect(url).userAgent(ua).timeout(10*1000).get();
        } catch (IOException e) {
            //throw new IllegalArgumentException("symbol not found in stock pool;");
            return 0.00;
        }
        String price = doc.getElementsByClass("Trsdu(0.3s) Fw(b) Fz(36px) Mb(-4px) D(ib)").first().text().replaceAll(",", "");

//        System.out.println(String.format("[%s] is trading at %s", symbol, price));
        return Double.parseDouble(price);
    }

    public static Double getOptionLastPrice(String symbol) {
        String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36";

        // be careful, this url is not the same as the one used for stocks
        String url = String.format("http://finance.yahoo.com/quote/%s?p=%s&.tsrc=fin-srch", symbol, symbol);
        Document doc  = null;
        try {
            doc = Jsoup.connect(url).userAgent(ua).timeout(30*1000).get();
        } catch (IOException e) {
            //throw new IllegalArgumentException("symbol not found in option pool;");
            return 0.00;
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
