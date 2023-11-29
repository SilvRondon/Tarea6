package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Restaurante {
	//Atributos
	
	private Map<String, Combo> combos;
	private Map<String, ProductoMenu> menuBase;
	private Map<String, Ingrediente> ingredientes;
	private Map<Integer, Pedido> pedidos;
	private Pedido pedidoEnCurso;
	
	//Metodos
	
	//Constructor
	public Restaurante(){
		this.combos = new HashMap<>();
		this.menuBase = new HashMap<>();
		this.ingredientes = new HashMap<>();
		this.pedidos = new HashMap<>();
	}
	
	public void iniciarPedido(String nombreCliente, String direccionCliente){
		pedidoEnCurso = new Pedido(nombreCliente, direccionCliente);
	}
	
	public void cerrarYGuardarPedido(){
		pedidos.put(pedidoEnCurso.getIdPedido(), pedidoEnCurso);
		File archivo = new File("FacturaPedido_"+pedidoEnCurso.getIdPedido()+".txt");
		pedidoEnCurso.guardarFactura(archivo);
		pedidoEnCurso.cerrarPedido();
	}
	
	public Pedido getPedidoEnCurso() {
		return pedidoEnCurso;
	}
	
	public Map<String, ProductoMenu> getMenuBase(){
		return menuBase;
	}
	
	public Map<String, Ingrediente> getIngredientes(){
		return ingredientes;
	}
	public Map<String, Combo> getCombos(){
		return combos;
	}
		
	//Carga datos 
	
	public void cargarInformacionRestaurante(File archivoMenu, File archivoIngredientes, File archivoCombo) throws IOException{
		
		try {
			cargarMenu(archivoMenu);
			
		}catch(HamburguesaException e){
			e.printStackTrace();
		}
		
		try {
			cargarIngredientes(archivoIngredientes);
		}catch(HamburguesaException e){
			e.printStackTrace();
		}
			
			cargarCombos(archivoCombo);
	}

	private void cargarIngredientes(File archivoIngredientes) throws IOException, HamburguesaException{
		BufferedReader br = new BufferedReader(new FileReader(archivoIngredientes));
		String linea = br.readLine(); 
		ArrayList<String> listaIngredientes = new ArrayList<String>();
		boolean hayExcepcion = false;
		
		while (linea != null){
			String[] partes = linea.split(";");
			if(listaIngredientes.contains(partes[0])) {
				hayExcepcion = true;
			}
			
			else {
				Ingrediente nuevoIngrediente = new Ingrediente(partes[0], Integer.parseInt(partes[1]));
				ingredientes.put(partes[0], nuevoIngrediente);
			}
			listaIngredientes.add(partes[0]);
			linea = br.readLine();
		}
		br.close();
		
		if(hayExcepcion) {
			throw new IngredienteRepetidoException("Hay uno o más ingredientes repetidos en los datos cargados");
		}
	}
	
	private void cargarMenu(File archivoMenu) throws IOException, HamburguesaException{
		BufferedReader br = new BufferedReader(new FileReader(archivoMenu));
		String linea = br.readLine();   
		ArrayList<String> listaProductos = new ArrayList<String>();
		boolean hayExcepcion = false;
		while (linea != null) {
			String[] partes = linea.split(";");
			
			if(listaProductos.contains(partes[0])) {
				hayExcepcion = true;
			}
			
			else {
				ProductoMenu nuevoProducto = new ProductoMenu(partes[0], Integer.parseInt(partes[1]));   
				menuBase.put(partes[0] , nuevoProducto ); 
			}
			listaProductos.add(partes[0]);
			linea = br.readLine();	
		}

		br.close();
		
		if(hayExcepcion) {
			throw new ProductoRepetidoException("Hay uno o más productos repetidos en los datos cargados");
		}
	}
	
	private void cargarCombos(File archivoCombo) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(archivoCombo));
		String linea = br.readLine();      
		while (linea != null) {
			String[] partes = linea.split(";");
			double porcentaje = Double.parseDouble(partes[1].substring(0, partes[1].length()-1))/100; // eliminar el caracter '%' y convertir el porcentaje a un valor numerico double

			Combo nuevoCombo = new Combo(partes[0], porcentaje);   
			for (int i = 2; i < partes.length; i++){
				int precio = menuBase.get(partes[i]).getPrecio();
				ProductoMenu item =  new ProductoMenu(partes[i], precio);
				nuevoCombo.agregarItemACombo(item);
			}
			combos.put(partes[0], nuevoCombo);
			linea = br.readLine(); 
		}
		br.close();
	}
	
}
