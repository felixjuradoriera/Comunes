package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import conf.Configuracion;
import dto.Event;
import dto.NinjaRoot;
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
	    	
		    while (hayMasResultados && offset<7) {
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
		                    
		                    odd.setTipoOdd("N");
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
	    	
		    if (TelegramSender.eventosIniciales==0) {
		    	TelegramSender.eventosIniciales=lectura.size();	
		    }
	    	  	
	    	return lectura;
	    	
	    }
	 
	 
	// ============================================================
	// SERVICIO NUEVO (oddsmatcher): petición paginada + mapeo a Odd
	// Devuelve el mismo ArrayList<Odd> (tipoOdd "N") que el flujo antiguo.
	// ============================================================
	public static String crearPeticionOddsmatcher(int offset) {
		StringBuilder response = new StringBuilder();
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode payload = mapper.createObjectNode();
			payload.put("bet_amount", Configuracion.ninjaBetAmount);
			ArrayNode ex = payload.putArray("exchange_ids");
			for (Integer id : Configuracion.ninjaExchangeIds) ex.add(id);
			payload.put("matcher_type", Configuracion.ninjaMatcherType);
			payload.put("mode", Configuracion.ninjaMode);
			payload.put("order_by", Configuracion.ninjaOrderBy);
			payload.put("offset", offset);
			payload.put("pagesize", Configuracion.ninjaPagesize);
			ArrayNode pids = payload.putArray("pids");
			for (Integer id : Configuracion.ninjaPids) pids.add(id);
			ArrayNode prov = payload.putArray("provider1");
			for (Integer id : Configuracion.ninjaProvider1) prov.add(id);
			payload.put("rating_from", Integer.valueOf(Configuracion.ratingInicial));
			ArrayNode sport = payload.putArray("sport");
			for (Integer id : Configuracion.ninjaSport) sport.add(id);
			// filtro de mercado (1=local,2=visitante,3=empate). El servidor puede ignorarlo;
			// de todas formas se vuelve a filtrar en cliente al mapear.
			if (Configuracion.ninjaSelecciones != null && !Configuracion.ninjaSelecciones.isEmpty()) {
				ArrayNode sel = payload.putArray("selection1");
				for (Integer sv : Configuracion.ninjaSelecciones) sel.add(sv);
			}
			byte[] postData = mapper.writeValueAsBytes(payload);

			URL obj = new URL(Configuracion.urlData);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Origin", "https://ninjabet.es");
			conn.setRequestProperty("Referer", "https://ninjabet.es/");
			conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/154.0.0.0 Safari/537.36");
			if (Configuracion.getNinjaSessionToken() != null) {
				conn.setRequestProperty("X-Session-Token", Configuracion.getNinjaSessionToken());
			} else {
				System.out.println("❌ Falta NINJA_SESSION_TOKEN en bot.properties: la petición a Ninjabet fallará");
			}
			conn.setDoOutput(true);
			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.write(postData);
			}

			int responseCode = conn.getResponseCode();
			codeRespuesta = responseCode;
			Configuracion.actualizarNinjaSessionToken(conn.getHeaderField("x-session-token"));
			System.out.println("Response Code Ninjabet: " + responseCode);
			InputStream is = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();
			if (is != null) {
				try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
					String line;
					while ((line = in.readLine()) != null) response.append(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			codeRespuesta = -1;
		}
		return response.toString();
	}

	public static ArrayList<Odd> leerOddsNinja() {
		ArrayList<Odd> lectura = new ArrayList<Odd>();
		ObjectMapper mapper = new ObjectMapper();
		int offset = 0;
		boolean inicial = true;

		while (offset < Configuracion.ninjaMaxPaginas) {
			String response = crearPeticionOddsmatcher(offset);

			if (codeRespuesta == null || codeRespuesta.intValue() != 200) {
				TelegramSender.response403++;
				TelegramSender.sendTelegramMessageVigilante(codeRespuesta);
				return null;
			}
			if (inicial) { TelegramSender.response200_Inicial++; inicial = false; }
			else { TelegramSender.response200_Adicional++; }

			if (offset == 0) DatosPruebasUtils.guardarJsonEnArchivo(new StringBuilder(response));

			NinjaRoot root;
			try {
				root = mapper.readValue(response, NinjaRoot.class);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
			if (root == null || root.evs == null || root.evs.isEmpty()) break;

			// mapa id_evento -> datos del partido
			java.util.HashMap<Long, NinjaRoot.NinjaMd> partidos = new java.util.HashMap<>();
			if (root.md != null) {
				for (NinjaRoot.NinjaMd m : root.md) {
					if (m.e1 != null) for (Long eid : m.e1) partidos.put(eid, m);
				}
			}

			for (NinjaRoot.NinjaEv ev : root.evs) {
				// filtro de mercado (local/visitante) en cliente
				if (ev.s1 == null || !Configuracion.ninjaSelecciones.contains(ev.s1)) continue;
				// filtro de cuota mínima en cliente (el servidor nuevo no filtra por cuota)
				if (ev.o1 == null || ev.o1 < Configuracion.nCuotaMinima) continue;
				// sin datos de partido no se puede construir una alerta usable
				NinjaRoot.NinjaMd m = (ev.e1 != null) ? partidos.get(ev.e1) : null;
				if (m == null) continue;

				Odd odd = new Odd();
				odd.setTipoOdd("N");
				odd.setBookie(String.valueOf(ev.p1));
				odd.setRating(String.valueOf(ev.rt1));
				odd.setRatingOriginal(String.valueOf(ev.rt1));
				odd.setBackOdd(String.valueOf(ev.o1));
				odd.setBackOddOriginal(String.valueOf(ev.o1));
				odd.setLayOdd(ev.o2 != null ? String.valueOf(ev.o2) : "0");
				odd.setSelectionId(String.valueOf(ev.s1));
				odd.setMarket_id(ev.mk);
				odd.setEvent(m.ev);
				odd.setCompetition(m.cm);
				odd.setCountry(m.ct);

				// selección textual a partir del evento "A v B"
				String sel = String.valueOf(ev.s1);
				if (m.ev != null && m.ev.contains(" v ")) {
					String[] parts = m.ev.split(" v ");
					if (ev.s1 == 1) sel = parts[0].trim();
					else if (ev.s1 == 2 && parts.length > 1) sel = parts[1].trim();
					else if (ev.s1 == 3) sel = "empate";
				}
				odd.setSelection(sel);

				// fecha del partido
				try {
					LocalDateTime fecha = LocalDateTime.parse(m.st);
					odd.setFechaPartido(fecha);
					odd.setsFechaPartido(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
				} catch (Exception e) {
					continue; // sin fecha válida, descartamos (los filtros posteriores la necesitan)
				}

				// edad en minutos a partir de ut (ahora es un timestamp absoluto)
				odd.setUpdate_time(ev.ut);
				int edad = 0;
				if (ev.ut != null) {
					try {
						edad = (int) java.time.Duration.between(java.time.Instant.parse(ev.ut), java.time.Instant.now()).toMinutes();
					} catch (Exception e) { edad = 0; }
				}
				if (edad < 0) edad = 0;
				odd.setTimeInMin(edad);

				// rebaja de cuota/rating para Codere, William Hill, Retabet, Daznbet
				odd = OddUtils.ajustaCuotaRating(odd);

				// nivel de alerta (misma lógica que el servicio antiguo)
				try {
					Double cuota = Double.valueOf(odd.getBackOdd());
					Double rating = Double.valueOf(odd.getRating());
					Integer nivelAlerta = 0;
					if (cuota > 5) {
						if (rating > 93.5) nivelAlerta = 1;
						if (rating > 95) nivelAlerta = 2;
					} else {
						if (rating > 96.25) nivelAlerta = 1;
						if (rating > 97.5) nivelAlerta = 2;
					}
					odd.setNivelAlerta(nivelAlerta);
				} catch (Exception e) {
					odd.setNivelAlerta(0);
				}

				lectura.add(odd);
			}

			// ¿quedan más páginas?
			int traidos = (offset + 1) * Configuracion.ninjaPagesize;
			if (root.tc == null || traidos >= root.tc.intValue()) break;
			offset++;
		}

		if (TelegramSender.eventosIniciales == 0) TelegramSender.eventosIniciales = lectura.size();
		return lectura;
	}

	 public static StringBuilder crearPeticionData(String urlParameters, String urlConexion) {
	    	StringBuilder response = new StringBuilder();
	        try {
	        	 String json =
	        		        "{"
	        		        + "\"bet_amount\":100,"
	        		        + "\"exchange_ids\":[10],"
	        		        + "\"matcher_type\":\"oddsmatcher\","
	        		        + "\"mode\":\"realmoney\","
	        		        + "\"order_by\":\"rating\","
	        		        + "\"offset\":7,"
	        		        + "\"pagesize\":10,"
	        		        + "\"pids\":["
	        		        + "73,87,2,147,3,5,10,12,95,20,24,27,146,30,"
	        		        + "24,37,41,43,30,204,143,170,49,50,147,147,151,"
	        		        + "63,144,24,72,24,74,76,145,85,90,100,104,107,109,"
	        		        + "105,204,119"
	        		        + "]"
	        		        + "}";
	        	
	        	byte[] postData = json.getBytes(StandardCharsets.UTF_8);
	        	

	        URL obj = new URL(urlConexion);
	        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
	        conn.setRequestMethod("POST");

	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Accept", "*/*");

	        conn.setRequestProperty("Origin", "https://ninjabet.es");
	        conn.setRequestProperty("Referer", "https://ninjabet.es/");

	        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

	        conn.setRequestProperty("User-Agent",
	            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
	            "AppleWebKit/537.36 (KHTML, like Gecko) " +
	            "Chrome/151.0.0.0 Safari/537.36");
	        
	        //TOKEN DE SESIÓN DE USUARIO NINJABET (bot.properties: NINJA_SESSION_TOKEN)
	        if (Configuracion.getNinjaSessionToken() != null) {
	        	conn.setRequestProperty("X-Session-Token", Configuracion.getNinjaSessionToken());
	        } else {
	        	System.out.println("❌ Falta NINJA_SESSION_TOKEN en bot.properties: la petición a Ninjabet fallará");
	        }

	        conn.setDoOutput(true);
	        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
	            wr.write(postData);
	        }

	        int responseCode = conn.getResponseCode();
	        System.out.println("Response Code : " + responseCode);
	        String code=Integer.valueOf(responseCode).toString();
	        
	        if(responseCode!=200) {
	        	codeRespuesta=Integer.valueOf(code);
	                         	
	        } else {
	        	codeRespuesta=200;
	        }

	        
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
			} catch (Exception e) {
				// TODO: handle exception
			}

			return eventos;

		}
	 
	    public static StringBuilder crearPeticionEvents(String name) throws IOException {
	        // Payload JSON del servicio nuevo: {"search_term": "...", "pids": [...]}
	        ObjectMapper mapper = new ObjectMapper();
	        ObjectNode payload = mapper.createObjectNode();
	        payload.put("search_term", name);
	        ArrayNode pids = payload.putArray("pids");
	        for (Integer id : Configuracion.ninjaPids) pids.add(id);
	        byte[] postData = mapper.writeValueAsBytes(payload);

	        URL url = new URL(Configuracion.urlEvents);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Accept", "*/*");
	        conn.setRequestProperty("Origin", "https://ninjabet.es");
	        conn.setRequestProperty("Referer", "https://ninjabet.es/");
	        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/154.0.0.0 Safari/537.36");
	        if (Configuracion.getNinjaSessionToken() != null) {
	        	conn.setRequestProperty("X-Session-Token", Configuracion.getNinjaSessionToken());
	        } else {
	        	System.out.println("❌ Falta NINJA_SESSION_TOKEN en bot.properties: la búsqueda de eventos fallará");
	        }

	        conn.setDoOutput(true);
	        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
	            wr.write(postData);
	        }

	        int responseCode = conn.getResponseCode();
	        Configuracion.actualizarNinjaSessionToken(conn.getHeaderField("x-session-token"));
	        if(responseCode==200) {
	        	TelegramSender.response200_Events++;
	        } else {
	        	TelegramSender.response403++;
	        }

	        InputStream inputStream = (responseCode >= 200 && responseCode < 300)
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
	    
	    
	    
	// true si la casa (p1) está en la lista configurada (local/visitante); el empate no filtra
	private static boolean esCasaConfigurada(Odd o) {
		try { return Configuracion.ninjaProvider1.contains(Integer.valueOf(o.getBookie())); }
		catch (Exception e) { return false; }
	}

	// ===== Mejores cuotas por selección a partir de la lista ya descargada del evento =====
	public static Odd rellenaMejoresHome(Odd odd, ArrayList<Odd> todas) {
		java.util.ArrayList<Odd> home = new java.util.ArrayList<>();
		for (Odd o : todas) if ("1".equals(o.getSelectionId()) && esCasaConfigurada(o)) home.add(o);
		home.sort(Comparator.comparingDouble(o -> Double.parseDouble(o.getBackOdd())));
		Collections.reverse(home);
		for (Odd o : home.subList(0, Math.min(4, home.size()))) {
			odd.setEquipoHome(o.getSelection());
			if (!o.getBookie().equals(odd.getBookie())) odd.getMejoresHome().add(o);
		}
		return odd;
	}

	public static Odd rellenaMejoresDraw(Odd odd, ArrayList<Odd> todas) {
		java.util.ArrayList<Odd> draw = new java.util.ArrayList<>();
		for (Odd o : todas) if ("3".equals(o.getSelectionId())) draw.add(o);
		draw.sort(Comparator.comparingDouble(o -> Double.parseDouble(o.getBackOdd())));
		Collections.reverse(draw);
		for (Odd o : draw.subList(0, Math.min(4, draw.size()))) {
			if (!o.getBookie().equals(odd.getBookie())) odd.getMejoresDraw().add(o);
		}
		return odd;
	}

	public static Odd rellenaMejoresAway(Odd odd, ArrayList<Odd> todas) {
		java.util.ArrayList<Odd> away = new java.util.ArrayList<>();
		for (Odd o : todas) if ("2".equals(o.getSelectionId()) && esCasaConfigurada(o)) away.add(o);
		away.sort(Comparator.comparingDouble(o -> Double.parseDouble(o.getBackOdd())));
		Collections.reverse(away);
		for (Odd o : away.subList(0, Math.min(4, away.size()))) {
			odd.setEquipoAway(o.getSelection());
			if (!o.getBookie().equals(odd.getBookie())) odd.getMejoresAway().add(o);
		}
		return odd;
	}

		public static Odd rellenaDrawExchange(Odd odd, String marketId) throws Exception {

			
			//Buscamos la cuota "empate" en betfair exchange
			String urlParameters=ExchangeService.crearUrlFiltroPeticionExchange(marketId);
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

		
		
	// ===== Cuotas de un evento concreto (oddsmatcher con filter_events) =====
	// Convierte "e13398860,e13399493,..." en [13398860,13399493,...]
	public static List<Long> parseEventIds(String csv) {
		java.util.ArrayList<Long> ids = new java.util.ArrayList<>();
		if (csv == null || csv.isEmpty()) return ids;
		for (String t : csv.split(",")) {
			String n = t.trim();
			if (n.startsWith("e") || n.startsWith("E")) n = n.substring(1);
			if (n.isEmpty()) continue;
			try { ids.add(Long.valueOf(n)); } catch (NumberFormatException e) { /* ignorar */ }
		}
		return ids;
	}

	public static String crearPeticionOddsmatcherEvento(List<Long> eventIds, int offset) {
		StringBuilder response = new StringBuilder();
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode payload = mapper.createObjectNode();
			payload.put("bet_amount", Configuracion.ninjaBetAmount);
			ArrayNode ex = payload.putArray("exchange_ids");
			for (Integer id : Configuracion.ninjaExchangeIds) ex.add(id);
			ArrayNode fe = payload.putArray("filter_events");
			for (Long id : eventIds) fe.add(id);
			payload.put("matcher_type", Configuracion.ninjaMatcherType);
			payload.put("mode", Configuracion.ninjaMode);
			payload.put("order_by", Configuracion.ninjaOrderBy);
			payload.put("offset", offset);
			payload.put("pagesize", Configuracion.ninjaPagesizeEvento);
			ArrayNode pids = payload.putArray("pids");
			for (Integer id : Configuracion.ninjaPids) pids.add(id);
			byte[] postData = mapper.writeValueAsBytes(payload);

			URL obj = new URL(Configuracion.urlData);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Origin", "https://ninjabet.es");
			conn.setRequestProperty("Referer", "https://ninjabet.es/");
			conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/154.0.0.0 Safari/537.36");
			if (Configuracion.getNinjaSessionToken() != null) {
				conn.setRequestProperty("X-Session-Token", Configuracion.getNinjaSessionToken());
			}
			conn.setDoOutput(true);
			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.write(postData);
			}
			int responseCode = conn.getResponseCode();
			codeRespuesta = responseCode;
			Configuracion.actualizarNinjaSessionToken(conn.getHeaderField("x-session-token"));
			InputStream is = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();
			if (is != null) {
				try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
					String line;
					while ((line = in.readLine()) != null) response.append(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			codeRespuesta = -1;
		}
		return response.toString();
	}

	// Descarga TODAS las cuotas (todas las casas y selecciones) de un evento y las mapea a Odd
	public static ArrayList<Odd> leerOddsEvento(List<Long> eventIds) {
		ArrayList<Odd> lista = new ArrayList<Odd>();
		if (eventIds == null || eventIds.isEmpty()) return lista;
		ObjectMapper mapper = new ObjectMapper();
		int offset = 0;
		while (offset < Configuracion.ninjaMaxPaginasEvento) {
			String response = crearPeticionOddsmatcherEvento(eventIds, offset);
			if (codeRespuesta == null || codeRespuesta.intValue() != 200) break;
			NinjaRoot root;
			try { root = mapper.readValue(response, NinjaRoot.class); }
			catch (Exception e) { e.printStackTrace(); break; }
			if (root == null || root.evs == null || root.evs.isEmpty()) break;

			java.util.HashMap<Long, NinjaRoot.NinjaMd> partidos = new java.util.HashMap<>();
			if (root.md != null) {
				for (NinjaRoot.NinjaMd m : root.md) if (m.e1 != null) for (Long eid : m.e1) partidos.put(eid, m);
			}

			for (NinjaRoot.NinjaEv ev : root.evs) {
				if (ev.s1 == null) continue;
				NinjaRoot.NinjaMd m = (ev.e1 != null) ? partidos.get(ev.e1) : null;

				Odd odd = new Odd();
				odd.setTipoOdd("N");
				odd.setBookie(String.valueOf(ev.p1));
				odd.setRating(String.valueOf(ev.rt1));
				odd.setRatingOriginal(String.valueOf(ev.rt1));
				odd.setBackOdd(String.valueOf(ev.o1));
				odd.setBackOddOriginal(String.valueOf(ev.o1));
				odd.setLayOdd(ev.o2 != null ? String.valueOf(ev.o2) : "0");
				odd.setSelectionId(String.valueOf(ev.s1));
				odd.setMarket_id(ev.mk);
				odd.setUpdate_time(ev.ut);
				if (m != null) {
					odd.setEvent(m.ev);
					odd.setCompetition(m.cm);
					odd.setCountry(m.ct);
					String sel = String.valueOf(ev.s1);
					if (m.ev != null && m.ev.contains(" v ")) {
						String[] pr = m.ev.split(" v ");
						if (ev.s1 == 1) sel = pr[0].trim();
						else if (ev.s1 == 2 && pr.length > 1) sel = pr[1].trim();
						else if (ev.s1 == 3) sel = "empate";
					}
					odd.setSelection(sel);
					try {
						LocalDateTime f = LocalDateTime.parse(m.st);
						odd.setFechaPartido(f);
						odd.setsFechaPartido(f.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
					} catch (Exception e) { /* sin fecha */ }
				}
				odd = OddUtils.ajustaCuotaRating(odd);
				lista.add(odd);
			}

			int traidos = (offset + 1) * Configuracion.ninjaPagesizeEvento;
			if (root.tc == null || traidos >= root.tc.intValue()) break;
			offset++;
		}
		return lista;
	}

	public static Odd rellenaCuotasTodas(Odd odd, String codigosEventos, String marketId) throws Exception {
		if (codigosEventos == null || codigosEventos.isEmpty()) {
			List<Event> eventos = buscaEventos(odd.getEvent());
			StringBuilder sb = new StringBuilder();
			for (Event e : eventos) sb.append(e.getId()).append(",");
			codigosEventos = sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
		}
		ArrayList<Odd> todas = leerOddsEvento(parseEventIds(codigosEventos));
		odd = rellenaMejoresHome(odd, todas);
		if (odd.getMejoresHome() == null || odd.getMejoresHome().isEmpty()) return odd;
		if (marketId == null || marketId.isEmpty()) marketId = odd.getMejoresHome().get(0).getMarket_id();
		odd = rellenaMejoresDraw(odd, todas);
		odd = rellenaDrawExchange(odd, marketId);
		odd = rellenaMejoresAway(odd, todas);
		return odd;
	}

	public static Odd rellenaCuotasSoloHome(Odd odd) throws Exception {
		List<Event> eventos = buscaEventos(odd.getEvent());
		StringBuilder sb = new StringBuilder();
		for (Event e : eventos) sb.append(e.getId()).append(",");
		String cod = sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
		ArrayList<Odd> todas = leerOddsEvento(parseEventIds(cod));
		return rellenaMejoresHome(odd, todas);
	}

	public static Odd rellenaCuotasSoloAway(Odd odd) throws Exception {
		List<Event> eventos = buscaEventos(odd.getEvent());
		StringBuilder sb = new StringBuilder();
		for (Event e : eventos) sb.append(e.getId()).append(",");
		String cod = sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
		ArrayList<Odd> todas = leerOddsEvento(parseEventIds(cod));
		return rellenaMejoresAway(odd, todas);
	}

}
