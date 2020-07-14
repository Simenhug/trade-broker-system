package quote;

import com.simen.tradesystem.securities.Equity;
import com.simen.tradesystem.securities.EquityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        optionPool.put("SPY200619P0033000", 25.00);
        optionPool.put("SPY200808P0031600", 11.45);
        optionPool.put("AAPL200619C0034000", 5.8);
        optionPool.put("AAPL200918P0034500", 23.95);
        optionPool.put("MSFT200717C0017500", 16.45);
        optionPool.put("MSFT200717C0020000", 3.05);
        optionPool.put("SPY200821C0026600", 42.85);
        optionPool.put("SPY200821P0026600", 6.63);
        optionPool.put("SPY200706C0028300", 23.38);
    }

    public static Double getStockLastPrice(String symbol) throws IllegalArgumentException{
        Double basePrice = stockPool.get(symbol);
        if (symbol == null) {
            throw new IllegalArgumentException("symbol not found in stock pool;");
        }
        Random random = new Random();
        Double delta = random.nextDouble();
        Double lastPrice = basePrice + (0.5 - delta)*basePrice*0.1;
        //limit the price to 2 decimal places
        Double answer = Double.valueOf(new DecimalFormat("#.##").format(lastPrice));
        return answer;
    }

    public static Double getOptionLastPrice(String symbol) throws IllegalArgumentException {
        Double basePrice = optionPool.get(symbol);
        if (symbol == null) {
            throw new IllegalArgumentException("symbol not found in stock poo;");
        }
        Random random = new Random();
        Double delta = random.nextDouble();
        Double lastPrice = basePrice + (0.5 - delta)*basePrice*0.1;
        //limit the price to 2 decimal places
        Double answer = Double.valueOf(new DecimalFormat("#.##").format(lastPrice));
        return answer;
    }

    public void changeMaintenanceRequirement(String symbol, double requirement) {
        Equity equity = equityRepository.findBySymbol(symbol);
        equity.setMaintenanceRequirement(requirement);
        equityRepository.save(equity);
    }
}
