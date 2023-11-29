package modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Pedido {
	//Atributos
	private static int numeroPedidos;
	private int idPedido;
	private boolean pedidoAbierto;
	private String nombreCliente;
	private String direccionCliente;
	private ArrayList<Producto> itemsPedido;
	
	//Metodos
	//Constructor
	public Pedido (String nombreCliente, String direccionCliente) {
		numeroPedidos ++;
		this.idPedido = numeroPedidos;
		this.direccionCliente = direccionCliente;
		this.nombreCliente = nombreCliente;
		this.itemsPedido = new ArrayList<>();
		this.pedidoAbierto = true;
		
		
	}
	
	//Getters y Setters
	
	public int getIdPedido(){
		return idPedido;
	}
	
	public void agregarProducto(Producto nuevoltem){
		itemsPedido.add(nuevoltem);
	}
	
	public void cerrarPedido(){
		pedidoAbierto = false;
	}
	
	private int getPrecioNetoPedido(){
		int precioNeto = 0; 
		for (int i=0; i < itemsPedido.size(); i++){
			precioNeto += itemsPedido.get(i).getPrecio();
		}
		return precioNeto;
	}
	private int getPrecioIVAPedido(){
		return (int) (getPrecioNetoPedido() * 0.19);
	}
	public int getPrecioTotalPedido(){
		return getPrecioNetoPedido() + getPrecioIVAPedido();
	}
	
	
	public ArrayList<Producto> getItemsPedido(){
		return itemsPedido;
	}
	// Otros
	
	private String generarTextoFactura() {
		String factura = "=".repeat(20)+"\nidPedido: " + idPedido + 
				"\nDatos cliente:\nNombre: " + nombreCliente + "\nDireccion: " + direccionCliente+
				"\n"+"=".repeat(20) + "\nLos items en el pedido son:";
		for (int i = 0; i < itemsPedido.size(); i++) {
			factura += "\n--" + itemsPedido.get(i).generarTextoFactura();
		}
		factura +="\n"+"=".repeat(20)+"\nSubtotal: " + getPrecioNetoPedido() +
				"\nIVA (19%): " + getPrecioIVAPedido() + 
				"\n"+"=".repeat(20)+"\nTotal: " + getPrecioTotalPedido()+
				"\n";
		return factura;
	}
	
	public void guardarFactura(File archivo){
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(archivo.getName()));
			writer.write(generarTextoFactura());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getNumeroPedidos() {
		return numeroPedidos;
	}
	public boolean getPedidoAbierto() {
		return pedidoAbierto;
	}
}
