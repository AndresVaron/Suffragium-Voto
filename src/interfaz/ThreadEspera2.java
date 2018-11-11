package interfaz;


public class ThreadEspera2 extends Thread{
	
	Principal principal;
	
	public ThreadEspera2(Principal principal) {
		this.principal = principal;
	}
	
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		principal.desHabilitar();
	}

}
