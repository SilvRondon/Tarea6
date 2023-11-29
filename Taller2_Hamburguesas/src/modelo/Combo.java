package modelo;

import java.util.ArrayList;


public class Combo implements Producto{
	
	//Atributos
	
	private double descuento;
	private String nombreCombo;
	private ArrayList<ProductoMenu> itemsCombo;
	
	//Metodos
	
	//Constructor
	
	public Combo(String nombre, double descuento){
		this.nombreCombo = nombre;
		this.descuento = descuento;
		this.itemsCombo = new ArrayList<>();
	}
	
	// Getters y Setters
	
	public void agregarItemACombo(ProductoMenu itemCombo){
		itemsCombo.add(itemCombo);
	}
	
	public int getPrecio(){
		int precioNeto = 0;
		for (int i = 0; i < itemsCombo.size(); i++) {
		    precioNeto += itemsCombo.get(i).getPrecio();
		}
		return (int) (precioNeto * (1 - descuento));
	}
	
	public String generarTextoFactura(){
		String texto = nombreCombo + " $" + getPrecio() +
				"\n Items: ";
		for (int i=0; i < itemsCombo.size(); i++ )
		{
			texto += "\n" + itemsCombo.get(i).getNombre();
		}
		return texto;
	}
	
	public String getNombre(){
		return nombreCombo;
	}

	public double getDescuento(){
		return descuento;
	}
}
