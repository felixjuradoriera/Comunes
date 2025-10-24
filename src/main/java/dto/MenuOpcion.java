package dto;

public class MenuOpcion {
	
	
	String texto;
	String callback;
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public MenuOpcion(String texto, String callback) {
		super();
		this.texto = texto;
		this.callback = callback;
	}
	
	
	
	

}
