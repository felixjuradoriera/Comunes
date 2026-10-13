package utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import dto.Odd;

public class AlertasFactory {
	
	
	
	
	public static StringBuilder createAlerta(Odd odd) {
		
	 	boolean vili=odd.getTipoOdd().equals("V")?true:false;
	 	boolean ninja=odd.getTipoOdd().equals("N")?true:odd.getTipoOdd().isEmpty()?true:false;
		
		StringBuilder mensaje = new StringBuilder();
		
		if(vili) {
			mensaje.append("<b>👉👉👉 V I L I B E T 👈👈👈").append("</b>\n\n");
		}
		if(ninja) {
			mensaje.append("<b>👉👉 N I N J A B E T 👈👈").append("</b>\n\n");
		}
    	
		 if(odd.getNivelAlerta()==1) {
			 mensaje.append("‼️🔥🔥‼️").append("\n");
		 }
		 if(odd.getNivelAlerta()==2) {
			 mensaje.append("‼️‼️🔥🔥🔥🔥🔥🔥‼️‼️").append("\n");
		 }
		 mensaje.append("⚽ <b>").append(odd.getEvent()).append("</b>\n");
		 mensaje.append("🏆 <b>").append(odd.getCompetition()).append("</b>");
		 if(ninja)
		 mensaje.append(" <b>(").append(odd.getCountry()).append(")</b>");
		 
		 mensaje.append("\n");
		 mensaje.append("🗓️ <b>").append(odd.getsFechaPartido()).append("h").append("</b>\n");
		 
		
		 
		 
		 mensaje.append("\n");
		 
		
				 int bookies=odd.getOddsFusion().size();
				 ArrayList oddsFusion=odd.getOddsFusion();
				 oddsFusion.sort(Comparator.comparingDouble(o -> Double.parseDouble(((Odd) o).getRating()))
		                    .reversed());
				 int iteracion=0;
				 for (Odd odFusion : odd.getOddsFusion()) {
					 
					 if(ninja) {
					
						 if(bookies>1) {
							 mensaje.append("‼️🔔<u><b> 2UP SIMPLE </b></u>\n");
						 } else {
							 mensaje.append("🔔<u><b> 2UP SIMPLE </b></u>\n");	 
						 }
						 
						 mensaje.append("    🏛 <b>").append(getNombreBookie(odFusion.getBookie())).append("</b>\n");
						 
						 if (odFusion.getBookie().equals("37") || odFusion.getBookie().equals("107")|| odFusion.getBookie().equals("145")|| odFusion.getBookie().equals("43")) {
							 mensaje.append("    📈 <b>").append(odFusion.getRating()).append("%</b> (").append(odFusion.getRatingOriginal()).append(")\n");
						 } else {
							 mensaje.append("    📈 <b>").append(odFusion.getRating()).append("%</b>\n");
						 }
						             		
						 mensaje.append("    Ap: <b>").append(odFusion.getSelection()).append("</b>\n");
						 
						 if (odFusion.getBookie().equals("37") || odFusion.getBookie().equals("107")|| odFusion.getBookie().equals("145")|| odFusion.getBookie().equals("43")) {
							 mensaje.append("    📋 Back: <b>").append(odFusion.getBackOdd()).append("</b> (").append(odFusion.getBackOddOriginal()).append(") | Lay: <b>").append(odFusion.getLayOdd()).append("</b>\n");	 
						 } else {
							 mensaje.append("    📋 Back: <b>").append(odFusion.getBackOdd()).append("</b> | Lay: <b>").append(odFusion.getLayOdd()).append("</b>\n"); 
					   		 }
					            		 
						mensaje.append("    ⏱ ").append(odFusion.getUpdate_time()).append("\n");
						mensaje.append("    🔗 <a href=\"https://www.betfair.es/exchange/plus/football/market?id=").append(odFusion.getMarket_id()).append("\">Ver en Betfair</a>\n\n"); 
					 }
					 
					 if(vili && iteracion<3) {
						 if(bookies>1) {
							 mensaje.append("‼️🔔<u><b> 3WAY 2UP </b></u>\n");
						 } else {
							 mensaje.append("🔔<u><b> 3WAY 2UP </b></u>\n");	 
						 }
						 mensaje.append("📈 <b>").append(odFusion.getRating()).append("%</b>\n");	 
						 
						 mensaje.append("🟢<b>").append("1-> ").append("</b>");
						 mensaje.append("  ").append(odFusion.getBookie1()).append("->").append(odFusion.getOdd1()).append("\n");
						 mensaje.append("🟢<b>").append("X-> ").append("</b>");
						 mensaje.append("  ").append(odFusion.getBookie2()).append("->").append(odFusion.getOdd2()).append("\n");
						 mensaje.append("🟢<b>").append("2-> ").append("</b>");
						 mensaje.append("  ").append(odFusion.getBookie3()).append("->").append(odFusion.getOdd3()).append("\n");
											 
					 }
					 iteracion++;
				}
				
				if(ninja) {
				 mensaje.append("🔔<u><b> OTRAS BOOKIES </b></u>\n");
				
				if("1".equals(odd.getSelectionId()))
				{
					mensaje.append("🟢<b>").append(odd.getEquipoHome()).append("</b>\n");
					for (Odd o : odd.getMejoresHome()) {
						 if (o.getBookie().equals("37") || o.getBookie().equals("107")|| o.getBookie().equals("145")|| o.getBookie().equals("43")) {
							mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("(").append(o.getBackOddOriginal()).append(")").append("\n");	 
						 } else {
							mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("\n"); 
						 }
					}
				}
			
				if("2".equals(odd.getSelectionId()))
				{
					mensaje.append("🟢<b>").append(odd.getEquipoAway()).append("</b>\n");
					for (Odd o : odd.getMejoresAway()) {
						if (o.getBookie().equals("37") || o.getBookie().equals("107")|| o.getBookie().equals("145")|| o.getBookie().equals("43")) {
							mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("(").append(o.getBackOddOriginal()).append(")").append("\n");	
						} else {
							mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("\n");
						}
					}
				}
		    
				}
		 
		 if(odd.getNivelAlerta()==1) {
			 mensaje.append("‼️🔥🔥‼️").append("\n");
		 }
		 if(odd.getNivelAlerta()==2) {
			 mensaje.append("‼️‼️🔥🔥🔥🔥🔥🔥‼️‼️").append("\n");
		 }
		
		
		
		return mensaje;
		
		
		
	}
	
	public static StringBuilder createAlertaMover(Odd odd) {
		
		StringBuilder mensaje = new StringBuilder();
    	
		mensaje.append("‼️‼️<b>").append("ALERTA MOVER</b>‼️‼️\n");
		 
		mensaje.append("⚽ <b>").append(odd.getEvent()).append("</b>\n");
		 mensaje.append("🏆 <b>").append(odd.getCompetition()).append(" (").append(odd.getCountry()).append(")</b>\n");
		 mensaje.append("🗓️ <b>").append(odd.getsFechaPartido()).append("h").append("</b>\n\n");
		
		 mensaje.append("    🏛 <b>").append(getNombreBookie(odd.getBookie())).append("</b>\n");
		 mensaje.append("    📈 <b>").append(odd.getRating()).append("%</b>\n");
		 mensaje.append("    📋 Back: <b>").append(odd.getBackOdd()).append("</b> | Lay: <b>").append(odd.getLayOdd()).append("</b>\n");
		 
		
		mensaje.append("    ⏱ ").append(odd.getUpdate_time()).append("\n");
		mensaje.append("    🔗 <a href=\"https://www.betfair.es/exchange/plus/football/market?id=").append(odd.getMarket_id()).append("\">Ver en Betfair</a>\n\n"); 
		 
		
		return mensaje;
		
		
	}
	
	public static StringBuilder createFichaEntrada(Odd odd) {
		
		StringBuilder mensaje = new StringBuilder();
		
		 mensaje.append("⚽ <b>").append(odd.getEvent()).append("</b>\n");
		 mensaje.append("🗓️ <b>").append(odd.getsFechaPartido()).append("h").append("</b>\n");
		 mensaje.append("<b>").append("Entrada Realizada:").append("</b>\n");
		 mensaje.append("<b>").append(odd.getSelection()).append("</b> Rat:<b>").append(odd.getRating()).append("%</b>\n");
		 mensaje.append(AlertasFactory.getNombreBookie(odd.getBookie())).append("-><b>").append(odd.getStakeEntradaBookie()).append("€ </b> BACK a <b>").append(odd.getBackOdd()).append("</b>\n");
		 mensaje.append("Exchange->").append(" <b>").append(odd.getStakeEntradaExchange()).append("€ </b> LAY a <b>").append(odd.getLayOdd()).append("</b>\n");
		 
		if(odd.getCierres()!=null && !odd.getCierres().isEmpty()) {
			 mensaje.append("\n");
			mensaje.append("<b>").append("Cierres 2UP:").append("</b>\n");
			for (Odd cierre : odd.getCierres()) {
				mensaje.append("-><b>").append(cierre.getStakeEarly()).append("</b> BACK a <b>").append(cierre.getBackOddEarly()).append("</b>\n");
			}
		}
		 
		 mensaje.append("\n");
		 mensaje.append("<b>").append("Resultado:").append("</b>\n");
		 if(odd.getBookieWins()<0) {
			 mensaje.append("BACK Wins: ").append("🔴 <b>").append(odd.getBookieWins()).append("€ </b> ").append("\n");	 
		 } else {
			 mensaje.append("BACK Wins: ").append("🟢 <b>").append(odd.getBookieWins()).append("€ </b> ").append("\n");
		 }
		 
		 if(odd.getExchangeWins()<0) {
			 mensaje.append("LAY Wins: ").append("🔴 <b>").append(odd.getExchangeWins()).append("€ </b> ").append("\n");	 
		 } else {
			 mensaje.append("LAY Wins: ").append("🟢 <b>").append(odd.getExchangeWins()).append("€ </b> ").append("\n");
		 }
		 
		 
		
		return mensaje;
		
	}
	
	
public static StringBuilder createFichaCierreParcial(Odd odd) {
		
		StringBuilder mensaje = new StringBuilder();
		
		 mensaje.append("⚽ <b>").append(odd.getEvent()).append("</b>\n");
		 mensaje.append("🗓️ <b>").append(odd.getsFechaPartido()).append("h").append("</b>\n");
		 mensaje.append("<b>").append("Entrada Realizada:").append("</b>\n");
		 mensaje.append("<b>").append(odd.getSelection()).append("</b> Rat:<b>").append(odd.getRating()).append("%</b>\n");
		 mensaje.append(AlertasFactory.getNombreBookie(odd.getBookie())).append("-><b>").append(odd.getStakeEntradaBookie()).append("€ </b> BACK a <b>").append(odd.getBackOdd()).append("</b>\n");
		 mensaje.append("Exchange->").append(" <b>").append(odd.getStakeEntradaExchange()).append("€ </b> LAY a <b>").append(odd.getLayOdd()).append("</b>\n");
		 
		 if(odd.getCierres()!=null && !odd.getCierres().isEmpty()) {
			 	mensaje.append("\n");
				mensaje.append("<b>").append("Cierres 2UP:").append("</b>\n");
				for (Odd cierre : odd.getCierres()) {
					mensaje.append("-><b>").append(cierre.getStakeEarly()).append("</b> BACK a <b>").append(cierre.getBackOddEarly()).append("</b>\n");
				}
		}
		 
		 mensaje.append("\n");
		 mensaje.append("👉Apuesta->").append("<b>").append(odd.getStakeEarly()).append("€ </b> BACK a <b>").append(odd.getBackOddEarly()).append("</b>\n\n");
		 		 
		 
		 mensaje.append("<b>").append("Resultado:").append("</b>\n");
		 if(odd.getBookieWins()<0) {
			 mensaje.append("Bookie Wins: ").append("🔴 <b>").append(odd.getBookieWins()).append("€ </b> ").append("\n");	 
		 } else {
			 mensaje.append("Bookie Wins: ").append("🟢 <b>").append(odd.getBookieWins()).append("€ </b> ").append("\n");
		 }
		 
		 if(odd.getExchangeWins()<0) {
			 mensaje.append("Exchange Wins: ").append("🔴 <b>").append(odd.getExchangeWins()).append("€ </b> ").append("\n");	 
		 } else {
			 mensaje.append("Exchange Wins: ").append("🟢 <b>").append(odd.getExchangeWins()).append("€ </b> ").append("\n");
		 }
		 
		 
		
		return mensaje;
		
	}
	
	
	
	public static StringBuilder createAlerta2WAY(Odd odd) {
		
	 	StringBuilder mensaje = new StringBuilder();
    	
		
		 mensaje.append("⚽ <b>").append(odd.getEvent()).append("</b>\n");
		 mensaje.append("🏆 <b>").append(odd.getCompetition()).append(" (").append(odd.getCountry()).append(")</b>\n");
		 mensaje.append("🗓️ <b>").append(odd.getsFechaPartido()).append("h").append("</b>\n\n");
		
		mensaje.append("🔔<u><b> 2WAY 2UP </b></u>\n");
		mensaje.append("🟢<b>").append(odd.getEquipoHome()).append("</b>\n");
		for (Odd o : odd.getMejoresHome()) {
			 if (o.getBookie().equals("37") || o.getBookie().equals("107") || o.getBookie().equals("145")|| o.getBookie().equals("43")) {
				mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("(").append(o.getBackOddOriginal()).append(")").append("\n");	 
			 } else {
				mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("\n"); 
			 }
		}
		
		
		mensaje.append("🟢<b>").append("Empate").append("</b>\n");
		for (Odd o : odd.getMejoresDraw()) {
			if (o.getBookie().equals("37") || o.getBookie().equals("3") || o.getBookie().equals("107")|| o.getBookie().equals("145")|| o.getBookie().equals("43")) {
				mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("(").append(o.getBackOddOriginal()).append(")").append("\n");	
			} else {
				mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("\n");
			}
		}
		
		if(odd.getExchangeDraw()!=null) {
			mensaje.append("     ").append("Exchange BACK ->").append(odd.getExchangeDraw().getBackOdd()).append("(").append(odd.getExchangeDraw().getBackOddOriginal()).append(")\n");
			mensaje.append("       Liquidez:").append(odd.getExchangeDraw().getLayOdd()).append("€\n");
		}
		
		
		mensaje.append("🟢<b>").append(odd.getEquipoAway()).append("</b>\n");
		for (Odd o : odd.getMejoresAway()) {
			if (o.getBookie().equals("37") || o.getBookie().equals("107")|| o.getBookie().equals("145")|| o.getBookie().equals("43")) {
				mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("(").append(o.getBackOddOriginal()).append(")").append("\n");	
			} else {
				mensaje.append("     ").append(getNombreBookie(o.getBookie())).append("->").append(o.getBackOdd()).append("\n");
			}
		}
		
			
		return mensaje;
		
		
		
	}
	
	
    public static String getNombreBookie(String bookie) {
        Map<String, String> bookies = new HashMap<>();
        bookies.put("73", "1xbet");
        bookies.put("87", "888sport");
        bookies.put("2", "Admiral");
        bookies.put("147", "Aupabet");
        bookies.put("3", "Bet365");
        bookies.put("5", "Bet777");
        bookies.put("12", "Betfairsportbook");
        bookies.put("95", "Betsson");
        bookies.put("20", "Betway");
        bookies.put("27", "Bwin");
        bookies.put("146", "Casino Barcelona");
        bookies.put("170", "Casino Madrid");
        bookies.put("30", "Casinogranvía");
        bookies.put("24", "Casumo");
        bookies.put("37", "Codere");
        bookies.put("41", "Dafabet");
        bookies.put("43", "Daznbet");
        bookies.put("204", "Enracha");
        bookies.put("143", "Goldenpark");
        bookies.put("49", "Interwetten");
        bookies.put("50", "Jokerbet");
        bookies.put("147", "Kirolbet");
        bookies.put("151", "Leovegas");
        bookies.put("63", "Luckia");
        bookies.put("144", "MarcaApuestas");
        bookies.put("72", "Olybet");
        bookies.put("74", "Paston");
        bookies.put("76", "Pokerstars");
        bookies.put("145", "Retabet");
        bookies.put("85", "Solcasino");
        bookies.put("90", "Sportium");
        bookies.put("100", "Tonybet");
        bookies.put("104", "Versus");
        bookies.put("107", "Williamhill");
        bookies.put("109", "Winamax");
        bookies.put("105", "Yaass");
        bookies.put("119", "Zebet");

        return bookies.getOrDefault(bookie, bookie);
    }
	
	

}
