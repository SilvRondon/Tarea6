package consola;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import modelo.HamburguesaException;
import modelo.Pedido;
import modelo.Producto;
import modelo.ProductoAjustado;
import modelo.Restaurante;
import modelo.ValorTotalException;

public class Aplicacion {
	private static Restaurante restaurante = new Restaurante();
	private static Map<String, ProductoAjustado> productosAjustados  = new HashMap<String,ProductoAjustado>();
	public static String input(String mensaje)
	{
		try{
			System.out.print(mensaje + ": ");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			return reader.readLine();
		}
		catch (IOException e){
			System.out.println("Error leyendo de la consola");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException{
		File archivoMenu = new File("data/menu.txt");
		File archivoIngredientes= new File("data/ingredientes.txt");
		File archivoCombos = new File("data/combos.txt");
		restaurante.cargarInformacionRestaurante(archivoMenu, archivoIngredientes, archivoCombos)  ;
		
		boolean seguir = true;
		while(seguir){
			try{
				mostrarOpcionesDisponibles();
				int opcion_seleccionada = Integer.parseInt(input("\nPor favor digite la opción que desee realizar"));
				if (opcion_seleccionada == 1 && restaurante != null) 
					ejecutarMostrarMenu();
				else if (opcion_seleccionada == 2 && restaurante != null)
					ejecutarIniciarNuevoPedido();
				else if (opcion_seleccionada == 3 && restaurante != null)
					if (restaurante.getPedidoEnCurso()== null || restaurante.getPedidoEnCurso().getPedidoAbierto()== false){
						System.out.println("\nDebe iniciar un nuevo pedido antes de continuar. ");
					}
					else{
						ejecutarAgregarElementoAPedido();
					}
				else if (opcion_seleccionada == 4 && restaurante != null)
					ejecutarCerrarPedido();
				else if (opcion_seleccionada == 5 && restaurante != null)
					ejecutarConsultarPedidoConID();
				else if (opcion_seleccionada == 6 && restaurante != null) {
					System.out.println("Saliendo de la aplicación ...");
					seguir = false;}
				else{
					System.out.println("Por favor seleccione una opción válida.");
				}
			}
			catch (NumberFormatException e){
				System.out.println("Debe seleccionar uno de los números de las opciones.");
			}
		}
	}
	
	

	private static void mostrarOpcionesDisponibles() {
		System.out.println("\n Opciones disponibles");
		System.out.println("1) Mostrar menu");
		System.out.println("2) Iniciar un nuevo pedido");
		System.out.println("3) Ajustar pedido (agregar un elemento, adicionar/eliminar ingredientes)");
		System.out.println("4) Cerrar un perdido y guardar la factura");
		System.out.println("5) Consultar la informacion de un pedido dado su ID");
		System.out.println("6) Cerrar aplicacion");
	}
	
	private static void mostrarOpcionesMenu() {
		System.out.println("\n\t Opciones de menus");
		System.out.println("1) Menu Base");
		System.out.println("2) Menu Combos");
		System.out.println("3) Menu Ingredientes adicionales");
		System.out.println("4) Volver a las opciones anteriores");
	}
	
	private static void ejecutarMostrarMenu(){
		boolean seguirMenu = true;
		
		while(seguirMenu){
			try{
				mostrarOpcionesMenu();
				int opcion_seleccionada = Integer.parseInt(input("\nPor favor digite la opción que desee realizar"));
				if (opcion_seleccionada == 1){
					System.out.print("\n\tProductos Base\n\n");
					Set<String> llavesMenu = restaurante.getMenuBase().keySet();
			        ArrayList<String> menu = new ArrayList<>(llavesMenu);
					for (int i = 0; i < menu.size(); i++) 
					{
						int pos = i + 1;
						System.out.println(" --"+(pos)+ " "+ restaurante.getMenuBase().get(menu.get(i)).generarTextoFactura());
					}
				}
				else if (opcion_seleccionada == 2)
				{
					System.out.print("\n\tCombos\n\n");
					Set<String> llavesMenu = restaurante.getCombos().keySet();
			        ArrayList<String> menu = new ArrayList<>(llavesMenu);
					for (int i = 0; i < menu.size(); i++) 
					{
						int pos = i + 1;
						System.out.println(" --"+(pos)+ " "+ restaurante.getCombos().get(menu.get(i)).generarTextoFactura());
					}
				}
				
				else if (opcion_seleccionada == 3)
				{
					System.out.print("\n\tIngredientes\n\n");
					Set<String> llavesMenu = restaurante.getIngredientes().keySet();
			        ArrayList<String> menu = new ArrayList<>(llavesMenu);
					for (int i = 0; i < menu.size(); i++) 
					{
						int pos = i + 1;
						System.out.println(" --"+(pos)+ " " +restaurante.getIngredientes().get(menu.get(i)).getNombre() + " $"+ restaurante.getIngredientes().get(menu.get(i)).getCostoAdicional());
					}
				}
				else if (opcion_seleccionada == 4)
				{
					System.out.println("Volviendo a las opciones iniciales");
					
					seguirMenu = false;
				}
				else
				{
					System.out.println("Por favor seleccione una opción válida.");
				}

			}
			catch (NumberFormatException e)
			{
				System.out.println("Debe seleccionar uno de los números de las opciones.");
			}
		}
	}
	
	private static void ejecutarIniciarNuevoPedido() {
		boolean centinelaNombre = true;
		String nombreCliente = "";
		while(centinelaNombre) 
		{
			System.out.print("\nPor favor, ingrese el nombre del cliente: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try 
			{
				nombreCliente = br.readLine();
				centinelaNombre = false;
			} catch (IOException e) {
				System.out.print("\nLa entrada no puede estar vacía.");
			}
		}
		String direccionCliente = "";
		boolean centinelaDireccion = true;
		while(centinelaDireccion) 
		{
				System.out.print("\nPor favor, ingrese la direccion del cliente: ");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				try {
					direccionCliente = br.readLine();
					centinelaDireccion = false;
				} catch (IOException e) {
					System.out.print("\nLa entrada no puede estar vacía.");
				}
		}
		restaurante.iniciarPedido(nombreCliente, direccionCliente);
		System.out.println("Se ha iniciado un nuevo pedido con id " + restaurante.getPedidoEnCurso().getIdPedido()+".");
	}
	
	private static void agregarElementoPedido(int tipoElemento, String nombreElemento) throws HamburguesaException {
		Pedido pedido = restaurante.getPedidoEnCurso();
		
		int precioActual = pedido.getPrecioTotalPedido();
		Producto producto = null;
		
		if (tipoElemento == 1) {
			producto = restaurante.getMenuBase().get(nombreElemento);
		}
		else if (tipoElemento == 2) {
			producto =(restaurante.getCombos().get(nombreElemento));
		}
		
		int precioProducto = producto.getPrecio();
		
		if (precioProducto + precioActual > 150000) {
			throw new ValorTotalException("El total del pedido no puede ser mayor a 150K");
		}
		
		else {
			pedido.agregarProducto(producto);
		}
		
	}
	
	private static void ejecutarAgregarElementoAPedido() {
		boolean seguir = true;
		int opcion_seleccionada = 0;
		while (seguir){
			try{
				System.out.print("\n\tOpciones de Ajuste \n1 - Adicionar un elemento al pedido\n2 - Adicionar o eliminar un ingrediente");
				opcion_seleccionada = Integer.parseInt(input("\nPor favor digite la opción que desee realizar: "));
		        if (opcion_seleccionada > 2 || opcion_seleccionada < 1){
		        	System.out.print("\nEl valor debe ser 1 o 2.");
		        }
		        else{
	        	seguir = false;
		        }
			}
	        catch (NumberFormatException e){
				System.out.println("Debe seleccionar uno de los números de las opciones.");
			}
		}
		if (opcion_seleccionada == 1){
			boolean seguirPedido = true;
			int opcion_seleccionada2 = 0;
			while (seguirPedido){
				try{
					System.out.print("\n\tOpciones \n1 - Producto del menú\n2 - Combo");
					opcion_seleccionada2 = Integer.parseInt(input("\nPor favor digite la opción que desee realizar: "));
			        if (opcion_seleccionada2 > 2 || opcion_seleccionada2 < 1) 
			        {
			        	System.out.print("\nEl valor debe ser 1 o 2.");
			        }
			        else 
			        {
		        	seguirPedido = false;
			        }
				}
		        catch (NumberFormatException e)
				{
					System.out.println("Debe seleccionar uno de los números de las opciones.");
				}
			}
			if (opcion_seleccionada2 == 1)
			{
				boolean seguir2 = true;
				while(seguir2)
				{
					try 
					{
						Set<String> llavesMenu = restaurante.getMenuBase().keySet();
				        ArrayList<String> menu = new ArrayList<>(llavesMenu);
						for (int i = 0; i < menu.size(); i++) 
						{
							int pos = i + 1;
							System.out.println(" --"+(pos)+ " "+ restaurante.getMenuBase().get(menu.get(i)).getNombre());
						}
						int opcion_a_anadir = 0;
						opcion_a_anadir = Integer.parseInt(input("\nPor favor, digite el número del elemento del menú que desea añadir: "));
						if (opcion_a_anadir > menu.size()+1 || opcion_a_anadir < 1) 
				        {
				        	System.out.print("\nEl valor debe ser 1 o " + menu.size()+1);
				        }
				        else 
				        {
			        	seguir2 = false;
			        	agregarElementoPedido(opcion_seleccionada2, menu.get(opcion_a_anadir-1));
			        	System.out.println("\nSe ha agregado el nuevo elemento al menú.");
				        }
					}
				
			        catch (NumberFormatException e)
					{
						System.out.println("Debe seleccionar uno de los números de las opciones.");
					}
					
					catch (HamburguesaException e) {
						e.printStackTrace();
					}
				}
			}
			else if (opcion_seleccionada2 == 2)
			{
				boolean seguir2 = true;
				while(seguir2)
				{
					try 
					{
						Set<String> llavescombos = restaurante.getCombos().keySet();
				        ArrayList<String> combos = new ArrayList<>(llavescombos);
						for (int i = 0; i < combos.size(); i++) 
						{
							int pos = i + 1;
							System.out.println(" --"+(pos)+ " "+ restaurante.getCombos().get(combos.get(i)).getNombre());
						}
						int opcion_a_anadir = 0;
						opcion_a_anadir = Integer.parseInt(input("\nPor favor, digite el número del elemento del menú que desea añadir: "));
						if (opcion_a_anadir > combos.size()+1 || opcion_a_anadir < 1) 
				        {
				        	System.out.print("\nEl valor debe ser 1 o " + combos.size()+1);
				        }
				        else 
				        {
			        	seguir2 = false;
			        	agregarElementoPedido(opcion_seleccionada2, combos.get(opcion_a_anadir-1));
			        	System.out.println("\nSe ha agregado el nuevo elemento al menú.");
				        }
					}
				
			        catch (NumberFormatException e)
					{
						System.out.println("Debe seleccionar uno de los números de las opciones.");
					}
					
					catch (HamburguesaException e) {
						e.printStackTrace();
					}
				}
			}
		}
		else if (opcion_seleccionada == 2)
		{
			ArrayList<Producto> productosPedido = restaurante.getPedidoEnCurso().getItemsPedido();
			if (productosPedido.size() == 0) {
				System.out.println("\nAún no se han registrado productos en el pedido.");
			}
			else
			{
				System.out.println("\nEstos son los productos hasta el momento en su pedido.");
				ArrayList<String> productosModificables = new ArrayList<String>();
				for (int i = 0; i < productosPedido.size(); i++) 
				{
					String nombreProducto = productosPedido.get(i).getNombre();
					System.out.println("-- "+nombreProducto);
					if (!nombreProducto.contains("combo")) {
						productosModificables.add(nombreProducto);
					}
				}
				if (productosModificables.size() == 0)
				{
					System.out.println("\nNo hay productos que se puedan modificar ya que la orden solo tiene combos.");
				}
				else
				{
					System.out.println("Estos son los productos sobre los que puede realizar modificaciones en su pedido: ");
					for (int i = 0; i < productosModificables.size(); i++) 
					{
						int pos = i+1;
						System.out.println("\n\t"+pos+" "+productosModificables.get(i));
					}
					boolean continuar = true;
					int productoAModificar = 0;
					while(continuar)
					{
						try
						{
						productoAModificar = Integer.parseInt(input("\nPor favor, digite el número del elemento del menú en el que desea ajustar un ingrediente: "));
						}
						catch (NumberFormatException nfe) 
						{
							System.out.println("\nLa entrada debe ser un valor numérico.");
						}
						if (productoAModificar > productosModificables.size() || productoAModificar < 1) 
						{
							System.out.println("\nDebe seleccionar un valor válido.");
						}
						else
						{
							continuar = false;
						}
					}
					String nombreProducto = productosModificables.get(productoAModificar-1);
					System.out.print("\n\tIngredientes\n\n");
					Set<String> llavesMenu = restaurante.getIngredientes().keySet();
			        ArrayList<String> menu = new ArrayList<>(llavesMenu);
					for (int i = 0; i < menu.size(); i++) 
					{
						int pos = i + 1;
						System.out.println(" --"+(pos)+ " " +restaurante.getIngredientes().get(menu.get(i)).getNombre() + " $"+ restaurante.getIngredientes().get(menu.get(i)).getCostoAdicional());
					}
					boolean opcionValida = true;
					int numeroElemento = 0;
					while(opcionValida) 
					{
						try 
						{
							numeroElemento = Integer.parseInt(input("\nPor favor, digite el número del ingrediente que desea añadir o eliminar: "));
						}
						catch (NumberFormatException nfe) 
						{
							System.out.println("\nLa entrada debe ser un valor numérico.");
						}
						if (numeroElemento > restaurante.getIngredientes().size()+1 || numeroElemento < 1) 
						{
							System.out.println("\nLa entrada debe ser un número entre 1 y "+ restaurante.getIngredientes().size()+1 +".");
						}
						else
						{
							opcionValida = false;
						}
						
						   
					}
					opcionValida = true;
					int agregarEliminar = 0;
					while(opcionValida) 
					{
						System.out.print("\n\tOpciones \n0 - Añadir ingrediente\n1 - Eliminar ingrediente");
						try 
						{
							agregarEliminar = Integer.parseInt(input("\nPor favor, digite la opción que corresponde a aquello que desea hacer con el ingrediente: "));
						}
						catch (NumberFormatException nfe) 
						{
							System.out.println("\nLa entrada debe ser un valor numérico.");
						}
				        if (agregarEliminar > 1 || agregarEliminar < 0) {
				        	System.out.print("\nEl valor debe 0 o 1.");
				        }
				        else {
				        	opcionValida = false;
				        }
					}
					
					boolean agregadoEliminado = agregarEliminarIngrediente(nombreProducto,numeroElemento-1,agregarEliminar);
					if (!agregadoEliminado) {
						System.out.print("\nNo se ha podido encontrar el elemento del menú que ha ingresado por parámetro.");
					}
					else {
						if (agregarEliminar == 0) {
							System.out.println("\nSe ha agregado el ingrediente a "+ nombreProducto+".");
						}
						else {
							System.out.println("\nSe ha eliminado el ingrediente de "+ nombreProducto+".");
						}
					}
					

				}
			}
		}
	}
	
	public static boolean agregarEliminarIngrediente(String nombreElemento, int numeroIngrediente, int adicionarEliminar) 
	{
		Pedido pedido = restaurante.getPedidoEnCurso();
		boolean encontrado = false;
		int i = 0;
		while (!encontrado && i < pedido.getItemsPedido().size()) 
		{
			if (pedido.getItemsPedido().get(i).getNombre().equals(nombreElemento)) 
			{
				encontrado = true;
			}
			else 
			{
				i++;
			}
		}
		boolean encontradoProductoBase = false;
		int j = 0;
		Set<String> llavesMenu = restaurante.getMenuBase().keySet();
        ArrayList<String> menu = new ArrayList<>(llavesMenu);
		while (!encontradoProductoBase) 
		{
			if (restaurante.getMenuBase().get(menu.get(j)).getNombre().equals(nombreElemento)) 
			{
				encontradoProductoBase = true;
			}
			else 
			{
			j++;
			}
		}
		ProductoAjustado producto;
		String nombreProducto = restaurante.getMenuBase().get(menu.get(j)).getNombre();
		Set<String> llavesIngredientes = restaurante.getIngredientes().keySet();
        ArrayList<String> ingredientes = new ArrayList<>(llavesIngredientes);
		if (productosAjustados.containsKey(nombreProducto)) 
		{
			producto = productosAjustados.get(nombreProducto);
		}
		else 
		{
			producto = new ProductoAjustado(restaurante.getMenuBase().get(menu.get(j)));
		}
		if (adicionarEliminar == 0) 
		{
			producto.agregarIngrediente(restaurante.getIngredientes().get(ingredientes.get(numeroIngrediente)));
		}
		else 
		{
			producto.eliminarIngrediente(restaurante.getIngredientes().get(ingredientes.get(numeroIngrediente)));	
		}
		
		if (productosAjustados.containsKey(nombreProducto)) {
			productosAjustados.replace(nombreProducto,producto);
		}
		else {
			productosAjustados.put(nombreProducto, producto);
		}
		pedido.getItemsPedido().set(i,producto);
		return encontrado;
	}
	
	private static void ejecutarCerrarPedido() {
		restaurante.cerrarYGuardarPedido();
		System.out.println("\nSe ha cerrado el pedido y se ha guardado la factura.");
		
	}
	
	public static void ejecutarConsultarPedidoConID() throws IOException 
	{
		int maximoValor = restaurante.getPedidoEnCurso().getNumeroPedidos();
		
		if (maximoValor == 0) 
		{
			System.out.print("\nAún no se han completado pedidos.");
		}
		else if (restaurante.getPedidoEnCurso().getPedidoAbierto()) 
		{
			System.out.print("\nEl pedido actual aún se encuentra abierto.\nPor favor espere a que se cierre para solicitar consultar un pedido.");
		}
		else 
		{
			System.out.print("\nSe consultará la información de un pedido basado en su id.");
			boolean opcionValida = true;
			int idPedido = 0;
			System.out.print("\n\t Posibles pedidos que puede consultar");
			for (int i=1; i < maximoValor + 1; i++)
			{
				System.out.print("\n ID Pedido: " + i);
			}
			while(opcionValida) 
			{
				System.out.print("\nPor favor digite el id del pedido: ");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				try 
				{
					String input = br.readLine();
					try 
					{
				        idPedido = Integer.parseInt(input);
				        if (maximoValor == 1 && idPedido != 1) 
				        { 
				        	System.out.print("\nEl número debe ser 1.");
				        }
				        else if (idPedido > maximoValor || idPedido < 1) 
				        { 
				        	System.out.print("\nEl número debe encontrarse entre 1 y "+maximoValor +".");
				        }
				        else 
				        {
				        	opcionValida = false;
				        }
				    } catch (NumberFormatException nfe) 
					{
				    	if (maximoValor == 1) 
				    	{
				    		System.out.print("\nLa entrada debe ser un número entre 1.");
				    	}
				    	else 
				    	{
				    		System.out.print("\nLa entrada debe ser un número entre 1 y "+maximoValor +".");
				    	}
				    }
					} catch (IOException e) 
				{
						System.out.print("\nLa entrada no puede estar vacía.");
					}
			}
			System.out.println("\nInformación del Pedido "+idPedido);
			FileReader file;
			try 
			{
				file = new FileReader("FacturaPedido_"+ idPedido+ ".txt");
				BufferedReader br = new BufferedReader(file);
				String line = br.readLine();
				while (!(line == null)) {
					System.out.println(line);
					line = br.readLine();
				}
				br.close();
				System.out.println("\nSe ha presentado la información del pedido.");
			} catch (FileNotFoundException e) {
				System.out.println("No ha sido posible localizar el archivo.");
			}
		}
	}

	

	

	

	
	
}
