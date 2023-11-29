package modelo;

public class ProductoMenu implements Producto{
	//Atributos
	
	private String nombre;
	private int precioBase;
	
	//Metodos
	
	//Constructor
	
	public ProductoMenu(String nombre, int precioBase){
		this.nombre = nombre;
		this.precioBase = precioBase;
	}
	
	//Getters y Setters
	public String getNombre(){
		return nombre;
	}
	
	public int getPrecio(){
		return precioBase;
	}

	public String generarTextoFactura(){
		return nombre + " $" + precioBase;
	}
}
