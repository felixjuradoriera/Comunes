package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import exchange.Runner;
import telegram.TelegramSender;

public class ExchangeService {
	
	public static Integer codeRespuesta = 0;
	
    public static String crearUrlFiltroPeticionExchange(String marketUid) {
    	
    	String urlFiltro="";
    	
    	urlFiltro+="currencyCode=EUR&locale=es";

    	urlFiltro+="&marketIds=" + marketUid;
    	
    	urlFiltro+="&rollupLimit=10&rollupModel=STAKE&types=MARKET_STATE,MARKET_RATES,MARKET_DESCRIPTION,EVENT,RUNNER_DESCRIPTION,RUNNER_STATE,RUNNER_EXCHANGE_PRICES_BEST,RUNNER_METADATA,MARKET_LICENCE,MARKET_LINE_RANGE_INFO";
    	
    
    	    	
    	return urlFiltro;
    }
    
    
	 public static StringBuilder crearPeticionData(String urlParameters, String urlConexion) {
	    	StringBuilder response = new StringBuilder();
	        try {
	    	byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

	        URL obj = new URL(urlConexion);
	        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	        conn.setRequestProperty("Content-Length", String.valueOf(postData.length));
	        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
	        conn.setRequestProperty("Referer", "https://www.ninjabet.es/oddsmatcher");

	        conn.setDoOutput(true);
	        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
	            wr.write(postData);
	        }

	        int responseCode = conn.getResponseCode();
	        System.out.println("Response Code : " + responseCode);
	        String code=Integer.valueOf(responseCode).toString();
//	        StringBuilder mensajeDebug = new StringBuilder();
//	        mensajeDebug.append("<b>Debug Ejecucion</b>\n");
	        
	        if(responseCode!=200) {
	        	codeRespuesta=Integer.valueOf(code);
	        	//mensajeDebug.append("resultado Petición HTTP: <b>").append(code).append("</b>\n");
	        	//mensajeDebug.append("⚽ <b>").append(code).append("</b>\n");
	                         	
	        } else {
	        	codeRespuesta=200;
	        	//mensajeDebug.append("resultado Petición HTTP: <b>").append(code).append("</b>\n");
	        	//mensajeDebug.append("⚽ <b>").append(code).append("</b>\n");
	        }

	      // TelegramSender.sendTelegramMessageDebug(mensajeDebug.toString());	
	        
	        TelegramSender.peticionesAExchange++;
	        
	        InputStream is = (responseCode >= 200 && responseCode < 300)
	                ? conn.getInputStream()
	                : conn.getErrorStream();

	       
	        try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
	            String inputLine;
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	        }
	       
	        } catch (Exception e) {
				e.printStackTrace();
				return response;
			}
	        
	        return response;
	    	
	    }
	 
	 
	    public static List<Runner> MapearResultadosExchange(String json) throws Exception {
	        ObjectMapper mapper = new ObjectMapper();

	        // Leer todo el JSON como árbol
	        JsonNode root = mapper.readTree(json);

	        // Navegar hasta el array de runners
	        JsonNode runnersNode = root
	                .path("eventTypes").get(0)
	                .path("eventNodes").get(0)
	                .path("marketNodes").get(0)
	                .path("runners");

	        // Convertir ese nodo en lista de objetos Runner
	        return mapper.convertValue(runnersNode, new TypeReference<List<Runner>>() {});
	    }

    

}
