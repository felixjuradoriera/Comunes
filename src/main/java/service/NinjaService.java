package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import conf.Configuracion;
import dto.Event;
import dto.Odd;
import exchange.PriceSize;
import exchange.Runner;
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
	 
	 public static ArrayList<Odd> mapearListaResultadosData(String filtroPeticion, String urlPeticion, boolean inicial) throws JsonMappingException, JsonProcessingException { 
	    	
	    	
	    	ArrayList<Odd> lectura=new ArrayList<Odd>();
	    	
	    	boolean hayMasResultados=true;
	    	int offset=0;
	    	
		    while (hayMasResultados && offset<6) {
		    	String urlParameters=filtroPeticion.replace("offset=$", "offset=" + offset);
		    	StringBuilder response= crearPeticionData(urlParameters, Configuracion.urlData);
		    	
		    	
		    	
		    	if(codeRespuesta.intValue()==200) {
		    		
		    		if(inicial) {
		    			TelegramSender.response200_Inicial++;	
		    		} else {
		    			TelegramSender.response200_Adicional++;
		    		}
		    		
		    		
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
		                    odd.setSelectionId(item.path("selection_id").asText());
		                    
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
		    		if(inicial) {
		    			TelegramSender.response403++;	
		    		} else {
		    			TelegramSender.response403++;
		    		}
		    		
		    		hayMasResultados=false;
	        		TelegramSender.sendTelegramMessageVigilante();
	        		return null;
	        	    
	        	}
		    	
			}
	    	
	    	TelegramSender.eventosIniciales=lectura.size();   	
	    	return lectura;
	    	
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
	 
	 
		public static List<Event> buscaEventos(String nombreEvento) {

			List<Event> eventos = new ArrayList<Event>();

			try {
				StringBuilder peticionEventos = crearPeticionEvents(nombreEvento);
				System.out.println(peticionEventos.toString());

				eventos = mapearListaResultadosEvents(peticionEventos.toString());

				String codigosEventos = "";

				if (!eventos.isEmpty()) {
					for (Event event : eventos) {
						codigosEventos += event.getId() + ",";
					}

					codigosEventos = codigosEventos.substring(0, codigosEventos.length() - 1);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			return eventos;

		}
	 
	    public static StringBuilder crearPeticionEvents(String name) throws IOException {
	        // Montar parámetros POST
	        String urlParameters = "name=" + URLEncoder.encode(name, "UTF-8");

	        // Preparar la conexión
	        URL url = new URL(Configuracion.urlEvents);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");

	        // Headers principales
	        conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	        conn.setRequestProperty("Origin", "https://www.ninjabet.es");
	        conn.setRequestProperty("Referer", "https://www.ninjabet.es/oddsmatcher");
	        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36");
	        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	        //conn.setRequestProperty("Cookie", cookies);

	        // Permitir envío de datos
	        conn.setDoOutput(true);

	        // Enviar body POST
	        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
	            wr.write(urlParameters.getBytes(StandardCharsets.UTF_8));
	        }

	        // Leer respuesta
	        int responseCode = conn.getResponseCode();
	        
	        if(responseCode==200) {
	        	TelegramSender.response200_Events++;
	        } else {
	        	TelegramSender.response403++;
	        }
	        
	        InputStream inputStream = (responseCode == 200)
	                ? conn.getInputStream()
	                : conn.getErrorStream();

	        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	        StringBuilder response = new StringBuilder();
	        String line;
	        while ((line = in.readLine()) != null) {
	            response.append(line);
	        }
	        in.close();

	        return response;
	    }
	    
	    
	    public static List<Event> mapearListaResultadosEvents(String json) throws Exception {
	        ObjectMapper objectMapper = new ObjectMapper();
	        return objectMapper.readValue(json, new TypeReference<List<Event>>(){});
	    }
	    
	    
	    
		public static Odd rellenaMejoresHome(Odd odd, String codigosEventos) throws Exception {
			
			ArrayList<Odd> lectura = new ArrayList<>();
			
			String urlParameters=NinjaService.crearUrlFiltroPeticionData(Configuracion.uid, Configuracion.filtroBookies2UP2WAY, "1", "1", Configuracion.filtroApuestasHome, codigosEventos);
			lectura=NinjaService.mapearListaResultadosData(urlParameters, Configuracion.urlData, false);
	    	lectura.sort(Comparator.comparingDouble(o -> Double.parseDouble(o.getBackOdd())));
	    	Collections.reverse(lectura);
			for (Odd o : lectura.subList(0, Math.min(4, lectura.size()))) {
				odd.setEquipoHome(o.getSelection());
				if(!o.getBookie().equals(odd.getBookie())) {
					odd.getMejoresHome().add(o);	
				}
				
			}
			
			
			
			return odd;

		}

		public static Odd rellenaMejoresDraw(Odd odd, String codigosEventos) throws Exception {
			ArrayList<Odd> lectura = new ArrayList<>();
			String urlParameters=NinjaService.crearUrlFiltroPeticionData(Configuracion.uid, Configuracion.filtroBookiesVacio, "1", "1", Configuracion.filtroApuestasDraw, codigosEventos);
			lectura=NinjaService.mapearListaResultadosData(urlParameters, Configuracion.urlData, false);
			lectura.sort(Comparator.comparingDouble(o -> Double.parseDouble(o.getBackOdd())));
	    	Collections.reverse(lectura);
	    	for (Odd o : lectura.subList(0, Math.min(4, lectura.size()))) {
				
				if(!o.getBookie().equals(odd.getBookie())) {
					odd.getMejoresDraw().add(o);	
				}
				
			}
			
			return odd;
		}

		public static Odd rellenaMejoresAway(Odd odd, String codigosEventos) throws Exception {
			
			ArrayList<Odd> lectura = new ArrayList<>();
						
			String urlParameters=NinjaService.crearUrlFiltroPeticionData(Configuracion.uid, Configuracion.filtroBookies2UP2WAY, "1", "1", Configuracion.filtroApuestasAway, codigosEventos);
			lectura=NinjaService.mapearListaResultadosData(urlParameters, Configuracion.urlData, false);
			lectura.sort(Comparator.comparingDouble(o -> Double.parseDouble(o.getBackOdd())));
	    	Collections.reverse(lectura);
			for (Odd o : lectura.subList(0, Math.min(4, lectura.size()))) {
				odd.setEquipoAway(o.getSelection());
				if(!o.getBookie().equals(odd.getBookie())) {
					odd.getMejoresAway().add(o);	
				}
			}
				
				return odd;
		}

		public static Odd rellenaDrawExchange(Odd odd) throws Exception {

			
			//Buscamos la cuota "empate" en betfair exchange
			String urlParameters=ExchangeService.crearUrlFiltroPeticionExchange(odd.getMarket_id());
			StringBuilder response= ExchangeService.crearPeticionData(urlParameters, Configuracion.urlExchange);
			List<Runner> listaExchange=ExchangeService.MapearResultadosExchange(response.toString());
			
			Double mejorCuotaDrawExchange=0.0;
			Double liquidez=0.0;
			if (listaExchange!=null && !listaExchange.isEmpty()) {
				for (Runner runner : listaExchange) {
					if (runner.getDescription().getRunnerName().equalsIgnoreCase("empate")) {
						List<PriceSize> cuotas = runner.getExchange().getAvailableToBack();
						if(cuotas!=null && !cuotas.isEmpty()) {
						for (PriceSize p : cuotas) {
							if(p.getPrice()>mejorCuotaDrawExchange) {
								mejorCuotaDrawExchange=p.getPrice();
								liquidez=p.getSize();
							}
							}
						}
					}
				}
				
				if(mejorCuotaDrawExchange>0) {
					Odd ex=new Odd();
					ex.setBackOddOriginal(mejorCuotaDrawExchange.toString());
					double reducida=(mejorCuotaDrawExchange-1)*(0.98)+1;
					Double reducidaRedondeada = Math.round(reducida * 100.0) / 100.0;
					ex.setBackOdd(reducidaRedondeada.toString());
					ex.setLayOdd(liquidez.toString());
					odd.setExchangeDraw(ex);
				}
			}
			
			return odd;
			
		}

		
		
	    public static Odd rellenaCuotasTodas(Odd odd) throws Exception {
	    	
	       	
	    	List<Event> eventos=buscaEventos(odd.getEvent());
	    	
	    	String codigosEventos="";
	    	
			if (!eventos.isEmpty()) {
				for (Event event : eventos) {
					codigosEventos += event.getId() + ",";
				}
				
				codigosEventos=codigosEventos.substring(0, codigosEventos.length() - 1);
			}
			
			odd=rellenaMejoresHome(odd, codigosEventos);
			
			odd=rellenaMejoresDraw(odd, codigosEventos);			
		
			odd=rellenaDrawExchange(odd);
			
			odd=rellenaMejoresAway(odd, codigosEventos);
					
	    	
	    	return odd;
	    }
	    
	    public static Odd rellenaCuotasSoloHome(Odd odd) throws Exception {
	    	
	       	
	    	List<Event> eventos=buscaEventos(odd.getEvent());
	    	
	    	String codigosEventos="";
	    	
			if (!eventos.isEmpty()) {
				for (Event event : eventos) {
					codigosEventos += event.getId() + ",";
				}
				
				codigosEventos=codigosEventos.substring(0, codigosEventos.length() - 1);
			}
			
			odd=rellenaMejoresHome(odd, codigosEventos);
			    	
	    	return odd;
	    }
	    
	    public static Odd rellenaCuotasSoloAway(Odd odd) throws Exception {
	    	
	       	
	    	List<Event> eventos=buscaEventos(odd.getEvent());
	    	
	    	String codigosEventos="";
	    	
			if (!eventos.isEmpty()) {
				for (Event event : eventos) {
					codigosEventos += event.getId() + ",";
				}
				
				codigosEventos=codigosEventos.substring(0, codigosEventos.length() - 1);
			}
			
			odd=rellenaMejoresAway(odd, codigosEventos);
					
	    	
	    	return odd;
	    }
		
		
}
