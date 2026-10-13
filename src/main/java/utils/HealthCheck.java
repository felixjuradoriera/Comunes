package utils;

import java.net.HttpURLConnection;
import java.net.URL;

import conf.Configuracion;

/**
 * Avisos a HealthChecks.io (o similar). Cada ping es un GET a la URL configurada
 * en bot.properties. Si la URL no está configurada, el ping no hace nada.
 * Nunca lanza excepción: un fallo del monitor jamás debe afectar al proceso.
 */
public class HealthCheck {

	private static void ping(String url) {
		if (url == null || url.trim().isEmpty()) {
			return; // monitor desactivado para este caso
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url.trim()).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			int code = conn.getResponseCode();
			try { conn.getInputStream().close(); } catch (Exception ignore) { }
			System.out.println("HealthCheck ping " + url + " -> " + code);
		} catch (Exception e) {
			System.out.println("HealthCheck ping FALLÓ (" + url + "): " + e.getMessage());
		}
	}

	/** La tarea ha comenzado (controla que se lance cada 5 min). */
	public static void pingStart() {
		ping(Configuracion.HEALTHCHECK_START);
	}

	/** La tarea ha terminado correctamente. */
	public static void pingSuccess() {
		ping(Configuracion.HEALTHCHECK_SUCCESS);
	}

	/** Las peticiones a Ninjabet han fallado. */
	public static void pingNinjaFail() {
		ping(Configuracion.HEALTHCHECK_NINJA_FAIL);
	}

	/** Las peticiones a Ninjabet han ido bien. */
	public static void pingNinjaOk() {
		ping(Configuracion.HEALTHCHECK_NINJA_OK);
	}

	/** Las peticiones a Vilibets han fallado. */
	public static void pingViliFail() {
		ping(Configuracion.HEALTHCHECK_VILI_FAIL);
	}

	/** Las peticiones a Vilibets han ido bien. */
	public static void pingViliOk() {
		ping(Configuracion.HEALTHCHECK_VILI_OK);
	}

	/** TelBot está vivo y en condiciones de responder. */
	public static void pingTelbotOk() {
		ping(Configuracion.HEALTHCHECK_TELBOT_OK);
	}

	/** TelBot está vivo pero NO podría responder bien (token/red/ficheros). */
	public static void pingTelbotFail() {
		ping(Configuracion.HEALTHCHECK_TELBOT_FAIL);
	}
}
