package Modelo;

public class Persona {

	private String nombre;
	private String apellido;
	private String email;
	private char sexo;
	private boolean estado;
	
	
	

	public Persona(String nombre, String apellido, String email, char sexo, boolean estado) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.sexo = sexo;
		this.estado = estado;
		
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public char getSexo() {
		return sexo;
	}
	public void setSexo(char sexo) {
		this.sexo = sexo;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	
	
	
	
}
