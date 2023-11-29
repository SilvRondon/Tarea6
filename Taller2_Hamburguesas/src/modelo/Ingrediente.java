package modelo;

public class Ingrediente {
	//Atributos
	private String nombre;
	private int costoAdicional;

	//Metodos
	
	//Constructor
	
	public Ingrediente(String nombre, int costoAdicional){
		this.nombre = nombre;
		this.costoAdicional = costoAdicional;
	}
	
	//Getter y Setters
	
	public String getNombre(){
		return nombre;
	}
	
	public int getCostoAdicional(){
		return costoAdicional;
	}
	
}
