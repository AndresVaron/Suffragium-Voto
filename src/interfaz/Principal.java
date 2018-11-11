package interfaz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Main;

public class Principal extends Application {

	private Main main;
	private Stage stage;
	private double x, y;
	private boolean habilitado;
	private Scene votos, espera;
	private StackPane centro, centroProg;
	String voto;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		main = new Main(this);
		this.stage = stage;
		try {
			stage.getIcons().add(new Image(new FileInputStream("./data/Icono.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		x = primaryScreenBounds.getWidth();
		y = primaryScreenBounds.getHeight();
		stage.setTitle("Suffragium - Votos");
		stage.centerOnScreen();

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				// main.finalizarVotos();
				System.exit(0);
			}
		});

		stage.show();
		load();

	}

	public void load() {
		stage.setWidth(x * 0.25);
		stage.setHeight(y * 0.5);
		stage.centerOnScreen();
		stage.setResizable(false);

		BorderPane border = new BorderPane();
		// Padding
		border.setPadding(new Insets(y * 0.05, 0, y * 0.025, 0));

		// Fondo
		BackgroundSize bSize = new BackgroundSize(0, 0, true, true, true, true);
		Background background = null;
		try {
			background = new Background(new BackgroundImage(new Image(new FileInputStream("./data/FondoCarga.jpg")),
					BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		border.setBackground(background);

		// Arriba
		ImageView logo = null;
		try {
			logo = new ImageView(new Image(new FileInputStream("./data/Logo.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		logo.setFitWidth(x * 0.225);
		logo.setFitHeight(y * 0.07);
		border.setTop(logo);
		BorderPane.setAlignment(logo, Pos.BOTTOM_CENTER);

		// Centro

		ImageView gifCarga = null;
		try {
			gifCarga = new ImageView(new Image(new FileInputStream("./data/LoaderInicio.gif")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		gifCarga.setFitWidth(x * 0.2);
		gifCarga.setFitHeight(y * 0.15);
		border.setCenter(gifCarga);
		BorderPane.setAlignment(gifCarga, Pos.CENTER);

		// Abajo
		ImageView loading = null;
		try {
			loading = new ImageView(new Image(new FileInputStream("./data/Loading.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		loading.setFitWidth(x * 0.15);
		border.setBottom(loading);
		BorderPane.setAlignment(loading, Pos.CENTER);

		Scene carga = new Scene(border);
		stage.setScene(carga);

		// Inicializa Espera
		ThreadEspera t = new ThreadEspera(this);
		t.start();
	}

	public void threadEmpezar() {
		Platform.runLater(() -> empezar());
	}

	public void empezar() {
		stage.setWidth(x * 0.4);
		stage.setHeight(y * 0.4);
		stage.centerOnScreen();
		stage.setResizable(false);

		BorderPane border = new BorderPane();
		// Padding
		border.setPadding(new Insets(y * 0.05, 0, y * 0.02, 0));

		// Arriba
		ImageView logo = null;
		try {
			logo = new ImageView(new Image(new FileInputStream("./data/Logo.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		logo.setFitWidth(x * 0.225);
		logo.setFitHeight(y * 0.07);
		border.setTop(logo);
		BorderPane.setAlignment(logo, Pos.BOTTOM_CENTER);

		// Centro
		VBox vertical = new VBox();
		vertical.setPadding(new Insets(y * 0.03, x * 0.03, 0, x * 0.03));
		vertical.setSpacing(y * 0.02);

		// Direccion
		HBox direc = new HBox();
		direc.setAlignment(Pos.CENTER_LEFT);
		direc.setSpacing(x * 0.025);

		Label direcLbl = new Label("Direccion:");
		direcLbl.setFont(new Font(x / 60));
		direc.getChildren().add(direcLbl);

		TextField direccionInput = new TextField();
		direccionInput.setPromptText("Direccion del jurado");
		direccionInput.setText("172.24.99.21:8085");
		direccionInput.setFont(new Font(x / 80));
		direc.getChildren().add(direccionInput);

		// Clave

		HBox clave = new HBox();
		clave.setAlignment(Pos.CENTER_LEFT);
		clave.setSpacing(x * 0.054);

		Label claveLbl = new Label("Clave:");
		claveLbl.setFont(new Font(x / 60));
		clave.getChildren().add(claveLbl);

		TextField claveInput = new TextField();
		claveInput.setPromptText("Valor de la llave");
		claveInput.setText("1234567890123456");
		claveInput.setFont(new Font(x / 80));
		clave.getChildren().add(claveInput);

		// Boton de iniciar votaciones

		StackPane panelBoton = new StackPane();
		panelBoton.setPadding(new Insets(y * 0.005, 0, 0, 0));
		panelBoton.setAlignment(Pos.BOTTOM_CENTER);
		Button submit = new Button("Empezar Votaciones");
		submit.setFont(Font.font("Vederna", FontWeight.BOLD, x / 70));
		submit.setOnAction(e -> {
			main.setUrl(direccionInput.getText());
			main.setKey(claveInput.getText());
			main.start();
			try {
				iniciar();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		panelBoton.getChildren().add(submit);

		vertical.getChildren().add(direc);
		vertical.getChildren().add(clave);
		vertical.getChildren().add(panelBoton);

		border.setCenter(vertical);
		BorderPane.setAlignment(vertical, Pos.CENTER);
		Scene llave = new Scene(border);
		stage.setScene(llave);
	}

	public void iniciar() throws Exception {
		stage.setWidth(x * 0.75);
		stage.setHeight(y * 0.75);
		stage.centerOnScreen();
		BorderPane borderEspera = new BorderPane();

		BackgroundSize bSize = new BackgroundSize(0, 0, true, true, true, true);
		Background background = null;
		try {
			background = new Background(new BackgroundImage(new Image(new FileInputStream("./data/FondoCarga.jpg")),
					BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		borderEspera.setBackground(background);

		// Centro
		ImageView logo = null;
		try {
			logo = new ImageView(new Image(new FileInputStream("./data/Logo.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		logo.setFitWidth(x * 0.4);
		logo.setFitHeight(y * 0.1);
		borderEspera.setCenter(logo);
		BorderPane.setAlignment(logo, Pos.CENTER);

		centro = new StackPane();
		centroProg = new StackPane();
		ProgressIndicator prog = new ProgressIndicator();
		prog.setScaleX(x * 0.0002);
		prog.setScaleY(x * 0.0002);
		prog.setStyle(" -fx-progress-color: black;");
		centroProg.getChildren().add(prog);
		centro.getChildren().add(borderEspera);
		espera = new Scene(centro);
		stage.setScene(espera);

		// Votos

		BorderPane borderVotos = new BorderPane();

		// Titulo
		Label titulo = new Label("Elecciones Presidenciales 2018");
		titulo.setTextFill(Color.WHITE);
		titulo.setFont(Font.font(30));
		titulo.setAlignment(Pos.CENTER);
		borderVotos.setTop(titulo);
		BorderPane.setAlignment(titulo, Pos.CENTER);

		// Candidatos
		GridPane grid = new GridPane();
		int id = 0;
		BufferedReader in = new BufferedReader(new FileReader(new File("./data/InfoCandidatos.txt")));
		String linea = in.readLine();
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				String[] d = linea.split(";");
				linea = in.readLine();

				// Informacion del candidato
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeight(500);
				alert.setWidth(500);
				alert.setTitle("Informacion del candidato");
				alert.setHeaderText(d[0] + "\n" + d[1]);
				String propuestas = "Propuestas:\n";
				String[] sp = d[2].split(",");
				for (int k = 0; k < sp.length; k++) {
					propuestas += "-" + sp[k] + "\n";
				}
				alert.setContentText(propuestas);

				// Confirmar Voto
				Alert confirmacion = new Alert(AlertType.CONFIRMATION);
				confirmacion.setTitle("Confirmacion");
				confirmacion.setHeaderText("Esta seguro que desea votar por: " + d[0]);
				confirmacion.setContentText("Elija una opcion.");
				ButtonType buttonTypeOne = new ButtonType("Si");
				ButtonType buttonTypeTwo = new ButtonType("No");
				confirmacion.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

				GridPane gr = new GridPane();
				Button b = new Button();
				b.setId("" + id);
				Button info = new Button();
				
				ImageView image = new ImageView(new Image(new FileInputStream("./data/" + (id + 1) + ".jpg")));
				
				image.setFitHeight(180);
				image.setFitWidth(180);
				image.setPreserveRatio(true);
				b.setGraphic(image);
				ImageView infoIcon = new ImageView(new Image(new FileInputStream("./data/info.jpg")));
				infoIcon.setFitHeight(15);
				infoIcon.setFitWidth(15);
				info.setGraphic(infoIcon);
				info.setOnAction(e -> {
					alert.showAndWait();
				});
				b.setOnAction(e -> {
					Optional<ButtonType> result = confirmacion.showAndWait();
					if (result.get() == buttonTypeOne) {
						contarVoto(b.getId());
					}
				});

				gr.setHgap(1);
				GridPane.setConstraints(b, 0, 0);
				GridPane.setConstraints(info, 1, 0);
				GridPane.setConstraints(gr, j, i);
				gr.getChildren().addAll(b, info);
				grid.getChildren().add(gr);
				id++;
			}
		}
		in.close();

		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(12);
		grid.setHgap(15);

		// Fondo
		Image fondo = new Image(
				"http://blogs.eltiempo.com/pineda-le-cuenta/wp-content/uploads/sites/722/2018/07/bandera-colombia.jpg");
		BackgroundSize bacSize = new BackgroundSize(0, 0, true, true, true, true);
		Background background1 = new Background(new BackgroundImage(fondo, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bacSize));
		borderVotos.setBackground(background1);
		borderVotos.setCenter(grid);
		votos = new Scene(borderVotos);

	}

	public synchronized boolean habilitado() {
		return habilitado;
	}

	public synchronized void contarVoto(String id) {
		voto = id;
		stage.setScene(espera);
		centro.getChildren().add(centroProg);
		new ThreadEspera2(this).start();
	}

	public String habilitar() {
		habilitado = true;
		Platform.runLater(() -> stage.setScene(votos));
		while (habilitado()) {
			Thread.yield();
		}
		return voto;
	}

	public synchronized void desHabilitar() {

		habilitado = false;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Platform.runLater(() -> {
			centro.getChildren().remove(centroProg);
		});
	}

	public void error(String msg) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("msg");
			alert.show();
		});
	}

}