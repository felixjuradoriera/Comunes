package utils;

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
	

}
