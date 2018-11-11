package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import interfaz.Principal;

public class Main extends Thread {

	private byte[] llave;
	private String url;
	private Principal interfaz;
	private PrintWriter out;
	private BufferedReader in;

	private Socket con;

	public Main(Principal interfaz) {
		llave = null;
		url = null;
		this.interfaz = interfaz;

	}

	public void setKey(String key) {
		llave = (key).getBytes();
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void run() {
		try {
			con = new Socket(url.split(":")[0], Integer.parseInt(url.split(":")[1]));
			out = new PrintWriter(con.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			out.println("INICIAR:" + encriptar(new String(llave)));
			String m = in.readLine();
			String info = "CONFIRMACION:" + encriptar("" + con.getPort());
			if (!m.equals(info)) {
				interfaz.error("Falla en la confirmacion de los credenciales.");
			}
			String msg;
			try {
				msg = in.readLine();
				while (msg != "FIN") {
					if (msg.startsWith("HABILITAR:")) {
						String ced = desEncriptar(msg.replaceFirst("HABILITAR:", ""));
						String voto = interfaz.habilitar();
						voto = ced + "" + voto;
						out.println("VOTO:" + encriptar(voto));
					}
					msg = in.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String encriptar(String msg) {
		return msg;
	}

	public String desEncriptar(String msg) {
		return msg;
	}
}
