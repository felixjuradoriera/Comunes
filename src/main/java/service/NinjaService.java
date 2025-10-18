package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import conf.Configuracion;
import dto.Odd;
import telegram.TelegramSender;
import utils.DatosPruebasUtils;
import utils.OddUtils;

public class NinjaService {
	
	public static Integer codeRespuesta = 0;
	
	 public static String crearUrlFiltroPeticionData(String uid, ArrayList<String> filtroBookies, String ratingInicial, String cuotaMinima, ArrayList<String> filtroApuestas, String filtroEventos) {
	    	
	    	String urlFiltro="";
	    	
	    	urlFiltro+="combinazioni=2&action=get_odds_data";
	    	urlFiltro+="&uid=" + uid;
	    	urlFiltro+="&refund=100&back_stake=100";
	    	
	    	if(!filtroEventos.isEmpty()) {
	    		urlFiltro+="&name[]=" + filtroEventos;	
	    	}
	    	
	    	if (!filtroBookies.isEmpty()) {
	    		String sFiltroBookies="";
	        	for (String bookie : filtroBookies) {
	    			sFiltroBookies+="&filterbookies[]=" + bookie;
	    		}
	        	urlFiltro+=sFiltroBookies;
	    	} else {
	    		urlFiltro+="&filterbookies=";
	    	}
	    	
	    	urlFiltro+="&bookies=-0,68,1,54,108,2,75,53,56,59,7,62,61,41,106,39,78,104,102,103,73,40,43,42,76,64,71,44,55,45,107,46,47,29,57,109,48,105,65,20,69,52,74";
	    	
	    	urlFiltro+="&rating-from=" + ratingInicial + "&rating-to=";
	    	
	    	urlFiltro+="&odds-from="+ cuotaMinima +"&odds-to=";
	    	
	    	urlFiltro+="&min-liquidity=&sort-column=4&sort-direction=desc";
	    	
	    	urlFiltro+="&offset=$&date-from=&date-to=&exchange=all&exchanges=all";
	    		
	    	urlFiltro+="&sport[]=1";
	    	
	    	urlFiltro+="&betfair-commission=2&matchbook-commission=";
	    	
	    	String sFiltroTiposApuesta="";
	    	for (String tipo : filtroApuestas) {
	    		sFiltroTiposApuesta+="&bet-type[]=" + tipo;
			}
	    	urlFiltro+=sFiltroTiposApuesta;
	    	
	    	urlFiltro+="&rating-type=normal";
	    	
	    	urlFiltro+="&roll-real-money=100&roll-bonus=100&roll-remaining=100&roll-rating=95&tz=-120";
	    	    	
	    	return urlFiltro;
	    }
	 
	 public static ArrayList<Odd> mapearListaResultadosData(String filtroPeticion, String urlPeticion) throws JsonMappingException, JsonProcessingException { 
	    	
	    	
	    	ArrayList<Odd> lectura=new ArrayList<Odd>();
	    	
	    	boolean hayMasResultados=true;
	    	int offset=0;
	    	
		    while (hayMasResultados && offset<6) {
		    	String urlParameters=filtroPeticion.replace("offset=$", "offset=" + offset);
		    	StringBuilder response= crearPeticionData(urlParameters, Configuracion.urlData);
		    	
		    	
		    	
		    	if(codeRespuesta.intValue()==200) {
		    		
		    		DatosPruebasUtils.guardarJsonEnArchivo(response);
		    		
		    		// 🔹 Procesar JSON con Jackson
		            ObjectMapper mapper = new ObjectMapper();
		            JsonNode root = mapper.readTree(response.toString());
		            JsonNode dataArray = root.get("data");
		            	    		
		            if (dataArray != null && dataArray.isArray()) {
		            	
		                for (JsonNode item : dataArray) {
		                    Odd odd = new Odd();
		                    odd.setEvent(item.path("event").asText());
		                    odd.setBookie(item.path("bookie_id").asText());
		                    odd.setRating(item.path("rating").asText());
		                    odd.setRatingOriginal(item.path("rating").asText());
		                    odd.setBackOdd(item.path("back_odd").asText());
		                    odd.setBackOddOriginal(item.path("back_odd").asText());
		                    odd.setLayOdd(item.path("lay_odd").asText());
		                    odd.setSelection(item.path("selection").asText());
		                    odd.setCompetition(item.path("competition").asText());
		                    odd.setUpdate_time(item.path("update_time").asText());
		                    odd.setCountry(item.path("country").asText());
		                    odd.setMarket_id(item.path("market_id").asText());
		                    
		                    String parteHora = item.path("update_time").asText().split(":")[0];  
		                    Integer horaEntero = Integer.parseInt(parteHora);
		                    odd.setTimeInMin(horaEntero);
		                    
		                    
		                 // Definir el formato de entrada (tal como llega el String)
		                    DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		                    // Parsear el String a LocalDateTime
		                    LocalDateTime fecha = LocalDateTime.parse(item.path("open_date").asText(), formatterEntrada);
		                    odd.setFechaPartido(fecha);
		                    // Definir el formato de salida
		                    DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		                    // Convertir a String en el nuevo formato
		                    String fechaFormateada = fecha.format(formatterSalida);
		                    odd.setsFechaPartido(fechaFormateada);
		                    
		                    
		                    //rebaja cuotas  (ej:codere)
		                    odd=OddUtils.ajustaCuotaRating(odd);
		                    
		                    
		                    //asignar Nivel Alerta
		                    Double cuota=Double.valueOf(odd.getBackOdd());
		                    Double rating=Double.valueOf(odd.getRating());
		                    Integer nivelAlerta=0;
		                    if(cuota>5) {
		                    	if(rating>93.5) {
		                    		nivelAlerta=1;
		                    	}
		                    	if(rating>95) {
		                    		nivelAlerta=2;
		                    	}
		                    	
		                    } else {
		                    	if(rating>96.25) {
		                    		nivelAlerta=1;
		                    	}
		                    	if(rating>97.5) {
		                    		nivelAlerta=2;
		                    	}
		                    	
		                    }
		                    odd.setNivelAlerta(nivelAlerta);
		                    
		                    
		                    lectura.add(odd);
		                }
		                
		                if(dataArray.size()<10) {
		                	hayMasResultados=false;
		                } else {
		                	offset++;
		                }
		                
		            } else {
		            	hayMasResultados=false;
		            }
		    		
		    		
		    	} else {
		    		hayMasResultados=false;
	        		TelegramSender.sendTelegramMessageVigilante();
	        		return null;
	        	    
	        	}
		    	
			}
	    	
	    	   	
	    	return lectura;
	    	
	    }
	 
	 
	 private static StringBuilder crearPeticionData(String urlParameters, String urlConexion) {
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
	        StringBuilder mensajeDebug = new StringBuilder();
	        mensajeDebug.append("<b>Debug Ejecucion</b>\n");
	        
	        if(responseCode!=200) {
	        	codeRespuesta=Integer.valueOf(code);
	        	mensajeDebug.append("resultado Petición HTTP: <b>").append(code).append("</b>\n");
	        	//mensajeDebug.append("⚽ <b>").append(code).append("</b>\n");
	                         	
	        } else {
	        	codeRespuesta=200;
	        	mensajeDebug.append("resultado Petición HTTP: <b>").append(code).append("</b>\n");
	        	//mensajeDebug.append("⚽ <b>").append(code).append("</b>\n");
	        }

	       TelegramSender.sendTelegramMessageDebug(mensajeDebug.toString());	
	        
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
	 

}
