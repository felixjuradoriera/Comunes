package utils;

import java.security.SecureRandom;

import conf.Configuracion;
import dto.Odd;

public class OddUtils {
	
	
	
    public static Odd ajustaCuotaRating(Odd odd) {
    	
		
		if(odd.getBookie().equals("39") && !odd.getSelection().equalsIgnoreCase("empate")) {
		System.out.println("AJUSTE CUOTA CODERE--> cuota Back: " + odd.getBackOdd() + "   rating: " + odd.getRating() + " cuota Lay: " + odd.getLayOdd() );
		Double cuotaBack=Double.valueOf(odd.getBackOdd());
		Double cuotaLay=Double.valueOf(odd.getLayOdd());
		
		Double cuotaBackReducida=0.0;
		if(cuotaBack<=3.0) {
			cuotaBackReducida=cuotaBack-Configuracion.restaCuotaCodere;	
		} else if (cuotaBack<=4.5) {
			cuotaBackReducida=cuotaBack-Configuracion.restaCuotaCodere -Configuracion.restaCuotaCodere;
		} else {
			cuotaBackReducida=cuotaBack-Configuracion.restaCuotaCodere -Configuracion.restaCuotaCodere -Configuracion.restaCuotaCodere;
		}
		    		
		cuotaBackReducida=Math.round(cuotaBackReducida * 100.0) / 100.0;
		Double layStake=100*cuotaBackReducida/(cuotaLay-0.02);
		Double profit=layStake*(1-0.02)-100;
		Double nuevoRating=((100+profit)/100)*100;
		Double nuevoRatingRedondeado = Math.round(nuevoRating * 100.0) / 100.0;
		
		odd.setBackOdd(cuotaBackReducida.toString());
		odd.setRating(nuevoRatingRedondeado.toString());
		System.out.println("AJUSTE CUOTA CODERE--> Nueva cuota Back: " + odd.getBackOdd() + "  nuevo rating: " + odd.getRating() );
	
		}
		
		if(odd.getBookie().equals("2") && odd.getSelection().equalsIgnoreCase("empate")) {
			
			Double cuotaBack=Double.valueOf(odd.getBackOdd());
			Double cuotaAumentada=cuotaBack * 1.04;
			cuotaAumentada=Math.round(cuotaAumentada * 100.0) / 100.0;
			
			odd.setBackOdd(cuotaAumentada.toString());
			
		}
		
	
	return odd;
}
    
    public static Odd pasarHijaMadre(Odd madre, Odd hija) {
    	
    	madre.setBookie(hija.getBookie());
    	madre.setRating(hija.getRating());
    	madre.setBackOdd(hija.getBackOdd());
    	madre.setLayOdd(hija.getLayOdd());
    	madre.setSelection(hija.getSelection());
    	madre.setSelectionId(hija.getSelectionId());
    	madre.setCompetition(hija.getCompetition());
    	madre.setUpdate_time(hija.getUpdate_time());
    	madre.setCountry(hija.getCountry());
    	madre.setTimeInMin(hija.getTimeInMin());
    	madre.setBackOddOriginal(hija.getBackOddOriginal());
    	madre.setRatingOriginal(hija.getRatingOriginal());
    	madre.setFechaPartido(hija.getFechaPartido());
    	madre.setsFechaPartido(hija.getsFechaPartido());
    	madre.setMarket_id(hija.getMarket_id());
    	madre.setNivelAlerta(1);
    	//madre.setEquipoHome(hija.getEquipoHome());
    	//madre.setEquipoAway(hija.getEquipoAway());
    	    	
    	return madre;
    }
	
    
    public static Long dameIdOdd() {
    	
    	 SecureRandom random = new SecureRandom();
         Long idUnico = Math.abs(random.nextLong() % 1_000_000_000L);
                 
         return idUnico;
    	
    }

}
