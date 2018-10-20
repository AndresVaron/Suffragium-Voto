package interfaz;

import java.util.Scanner;

import main.Main;

public class Principal {

	private Main main;
	private Scanner reader;

	public Principal() {
		reader = new Scanner(System.in);
		System.out.println("Ingrese la ruta del archivo a cargar: ");
		// String ruta = reader.next();
		main = new Main(this);
		System.out.println("Ingrese la direccion de la maquina de jurado que le pertenece: ");
		String url = reader.nextLine();
		while (!url.contains(":")) {
			System.out.println("Direccion ingresada invalida!");
			url = reader.nextLine();
		}
		System.out.println(url);
		main.setUrl(url);
		System.out.println("Ingrese la clave local");
		String k  =reader.nextLine();
		main.setKey(k);
		try {
			main.iniciar();
		} catch (Exception e) {
			msgError("Error al crear la conexion.");
		}
	}

	public void msgError(String err) {
		System.out.println(err);
	}
	
	public String votar() {
		System.out.println("Ingrese su voto");
		return reader.nextLine();
	}

	public static void main(String[] args) {
		new Principal();

	}
}
