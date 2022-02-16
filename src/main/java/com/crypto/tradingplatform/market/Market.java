package com.crypto.tradingplatform.market;

import com.crypto.tradingplatform.domain.Cryptocurrency;
import com.crypto.tradingplatform.repository.CryptocurrencyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import java.lang.String;
import java.util.Map;

@Component
public class Market {

    private Map<Cryptocurrency, BigDecimal[]> prices;
    private CryptocurrencyRepository cryptocurrencyRepository;

    public Market(CryptocurrencyRepository cryptocurrencyRepository) {
        this.cryptocurrencyRepository = cryptocurrencyRepository;
        prices = new HashMap<>();
    }

    @PostConstruct
    private void updatePrices() {
        HashMap<Cryptocurrency, BigDecimal[]> currentPrices = new HashMap<>();
        List<Cryptocurrency> cryptocurrencies = cryptocurrencyRepository.findAll();
        for (Cryptocurrency cryptocurrency : cryptocurrencies) {
            BigDecimal price = getPriceAPI(cryptocurrency.getName());
            BigDecimal[] prices = new BigDecimal[2];
            //ask za tyle kupujemy
            prices[0] = price.multiply(BigDecimal.valueOf(1.01)).setScale(2, RoundingMode.HALF_UP);
            //bid za tyle sprzedajemy
            prices[1] = price.multiply(BigDecimal.valueOf(0.99)).setScale(2, RoundingMode.HALF_UP);
            currentPrices.put(cryptocurrency, prices);
        }
        prices = currentPrices;
    }

    private StringBuffer getRequest(URL url, int connectTimeout, int readTimeout) {
        HttpURLConnection connection = null;
        BufferedReader reader;
        String line;
        StringBuffer response = new StringBuffer();

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);

            int status = connection.getResponseCode();

            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null)
                    response.append(line);
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null)
                    response.append(line);
                reader.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        return response;
    }

    private BigDecimal getPriceAPI(String cryptocurrencyName) {
        BigDecimal price = null;

        try {
            URL url = new URL("https://api.coingecko.com/api/v3/simple/price?ids=" + cryptocurrencyName + "&vs_currencies=usd");

            StringBuffer response = getRequest(url, 5000, 5000);
            if (response.length() > 0) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode node = objectMapper.readTree(response.toString());
                JsonNode cryptoPrice = objectMapper.readTree(node.get(cryptocurrencyName).toString());

                price = new BigDecimal(cryptoPrice.get("usd").toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return price;
    }

    public Map<Cryptocurrency, BigDecimal[]> getPrices() {
        return prices;
    }
}
