package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@SuppressWarnings("deprecation")
	public void draw(Stage stage) {
		// update utilities
		updateMouse();
		updateKey();
		// update date
		date = new Date();
		// update time text
		timeText.setText(timeString(date.getHours(), date.getMinutes()));
		// update date text
		dateText.setText(dateString(date.getDay(), date.getDate(), date.getMonth(), date.getYear()));
		// search
		if (searchBar.string().length() > 0 && ((keyPressed && key.equals("ENTER") && searchBar.active())
				|| searchButton.clicked(mouseX, mouseY, mouseReleased))) {
			getHostServices().showDocument("https://www.google.com/search?q=" + searchBar.string());
		}
		searchBar.draw(key, keyPressed, keyReleased, shiftDown, mouseX, mouseY, mousePressed);
		searchButton.draw(mouseX, mouseY, mousePressed);
		// set coordinates
		if (centered) {
			timeText.setLayoutX(-timeText.getLayoutBounds().getWidth()/2);
			dateText.setLayoutX(-dateText.getLayoutBounds().getWidth()/2);
		} else {
			timeText.setLayoutX(-640);
			dateText.setLayoutX(-640);
		}
		// nytimes
		for (int i = 0; i < nya.length; i++) {
			nya[i].draw(mouseX, mouseY, mousePressed);
			if (nya[i].clicked(mouseX, mouseY, mouseReleased)) {
				getHostServices().showDocument(nya[i].url());
			}
		}
		// refresh
		try {
			refresh();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		resetReleased();
	}
	
	
	
	
	
	
	
	public static boolean mousePressed, mouseReleased, keyPressed, keyReleased, shiftDown;
	public static int mouseX, mouseY;
	public static String key;
    public static boolean centered = false;
	private static Scene scene;
	private static Pane pane;
	private static Date date;
	private static Text timeText, dateText;
	private static TextBox searchBar;
	private static Button searchButton;
    private static Article[] nya = new Article[2];
    private static int temp, high, low;
    private static String forecast;
    private static Text tText, hiloText, fText;
    private static Game[] games;
	
	public void initBackground() {
		scene.setFill(Color.rgb(0, 202, 191));
		Polygon t1 = triangle(0, 900, 520, 560, 770, 900);
		t1.setFill(Color.rgb(247,  210, 0));
		Polygon t2 = triangle(0, 900, 660, 620, 720, 900);
		t2.setFill(Color.rgb(168, 227, 235));
		Polygon s1 = triangle(-300, 900, 660, 620, 560, 900);
		s1.setFill(Color.BLACK);
		s1.setOpacity(0.1);
		Polygon t3 = triangle(-150, 900, 30, 735, 280, 900);
		t3.setFill(Color.rgb(175,  223,  63));
		Polygon s2 = triangle(30, 735, 280, 900, 140, 900);
		s2.setFill(Color.NAVY);
		s2.setOpacity(0.2);
		Polygon t4 = triangle(350, 815, 460, 900, 110, 900);
		t4.setFill(Color.rgb(211, 0, 111));
		Polygon s3 = triangle(350, 815, 240, 900, 110, 900);
		s3.setFill(Color.NAVY);
		s3.setOpacity(0.2);
		pane.getChildren().addAll(t1, t2, s1, t3, s2, t4, s3);
	}
	
	public Polygon triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		Polygon t = new Polygon();
		t.getPoints().addAll(new Double[]{x1, y1, x2, y2, x3, y3});
		return t;
	}
	
	public void initTimeText() {
		timeText = new Text(720, 250, "");
		dateText = new Text(720, 330, "");
		timeText.setFill(Color.WHITE);
		dateText.setFill(Color.WHITE);
		timeText.setFont(new Font("Avenir", 200));
		dateText.setFont(new Font("Avenir", 50));
		pane.getChildren().addAll(timeText, dateText);
	}
	
	public String timeString(int h, int m) {
		return (((h-1)%12)+1) + ":" + ((m > 9) ? m : ("0" + m));
	}
	
	public String dateString(int day, int date, int month, int year) {
		String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December"};
		return days[day] + ", " + months[month] + " " + date + ", " + (year+1900);
	}
	
	public void initSearch() {
		int addX = (centered) ? 0 : -375;
		searchBar = new TextBox(455 + addX, 400, 440, 50);
		searchButton = new Button(905 + addX, 400, 50, 50);
		searchButton.setColor(60, 122, 252);
		Group searchIcon = new Group();
		Circle circle = new Circle(927 + addX, 422, 10);
		Line line = new Line(934 + addX, 429, 942 + addX, 437);
		circle.setFill(Color.TRANSPARENT);
		circle.setStroke(Color.grayRgb(255));
		circle.setStrokeWidth(2);
		line.setStroke(Color.grayRgb(255));
		line.setStrokeWidth(2);
		searchIcon.getChildren().addAll(circle, line);
		pane.getChildren().addAll(searchBar.components(), searchButton.components(), searchIcon);
	}
	
	public void initNYTimes() throws IOException {
		try {
			Document doc = Jsoup.connect("http://www.nytimes.com").get();
			// find articles
			Elements links = doc.getElementsByTag("article");
			int numArticles = 0;
			String[] articles = new String[nya.length];
			for (int i = 0; i < links.size() && numArticles < nya.length; i++) {
				String linkText = links.get(i).text();
				// find good articles
				if (linkText.split(" ").length > 25) {
					articles[numArticles] = linkText;
					numArticles++;
				}
			}
			// find article urls
			String[] urls = new String[nya.length];
			Elements urlLinks = doc.select("a[href]");
			for (int i = 0; i < nya.length; i++) {
				// match url to article title
				for (int j = 0; j < urlLinks.size(); j++) {
					String articleTitle = urlLinks.get(j).text();
					if (articleTitle.length() > 0 && articleTitle.equals(articles[i].substring(0, articleTitle.length()))) {
						urls[i] = urlLinks.get(j).attr("href");
					}
				}
				nya[i] = new Article(910 + i*170, 630, 150, 230, articles[i], urls[i]);
			}
		} catch (IOException e) {
		}
	}
	
	public void initWeather() throws IOException {
		try {
			// retrieve data
			Document doc = Jsoup.connect("https://www.accuweather.com/en/us/pittsburgh-pa/15219/hourly-weather-forecast/1310").get();
			String page = doc.text();
			int tIndex = page.indexOf("Pittsburgh, PA") + 14;
			temp = Integer.valueOf(page.substring(tIndex, page.indexOf("°", tIndex)));
			int hIndex = page.indexOf("High") + 5;
			high = Integer.valueOf(page.substring(hIndex, page.indexOf("°", hIndex)));
			int lIndex = page.indexOf("Low", hIndex) + 4;
			low = Integer.valueOf(page.substring(lIndex, page.indexOf("°", lIndex)));
			Elements fcasts = doc.getElementsByClass("cond");
			forecast = fcasts.get(0).text();
			// draw
			tText = new Text(910, 600, String.valueOf(temp) + "°");
			tText.setFont(new Font("Avenir", 100));
			tText.setFill(Color.WHITE);
			double smallTextX = 920 + tText.getLayoutBounds().getWidth();
			hiloText = new Text(smallTextX, 600, String.valueOf(high) + "° / " + String.valueOf(low) + "° – Pittsburgh, PA");
			hiloText.setFont(new Font("Avenir", 20));
			hiloText.setFill(Color.WHITE);
			fText = new Text(smallTextX, 570, forecast);
			fText.setFont(new Font("Avenir", 20));
			fText.setFill(Color.WHITE);
		} catch (IOException e) {
		}
	}
	
	@SuppressWarnings("unchecked")
	public void initKnicks() throws IOException {
		try {
			// retrieve stuff
			Document doc = Jsoup.connect("http://www.espn.com/nba/team/_/name/ny/new-york-knicks").get();
			String page = doc.text();
			// parse stuff
			String p2 = page.substring(page.indexOf("Regular Season") + 15, page.length()/2);
			String[] split1 = p2.split("vs");
			ArrayList<String>[] split2 = new ArrayList[5];
			for (int i = 0; i < split2.length; i++) {
				split2[i] = new ArrayList<String>(Arrays.asList(split1[i+1].split("@")));
			}
			// instantiate stuff
			games = new Game[5];
			for (int i = 0, x = 0; i < split2.length && x < games.length; i++) {
				for (int j = 0; j < split2[i].size() && x < games.length; j++, x++) {
					games[x] = new Game(1250, 650 + x*46, split2[i].get(j));
				}
			}
		} catch (IOException e) {
		}
	}
	
	@SuppressWarnings("deprecation")
	public void refresh() throws IOException {
		if (date.getMinutes() % 5 == 0 && date.getSeconds() == 0) {
			initNYTimes();
			initWeather();
		}
	}
	
	@Override
	public void start(Stage stage) throws IOException {

		pane = new Pane();
		scene = new Scene(pane, 1440, 900, Color.WHITE);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();

		initBackground();
		initTimeText();
		initSearch();
		initNYTimes();
		initWeather();
		initKnicks();

		for (int i = 0; i < nya.length; i++) {
			pane.getChildren().add(nya[i].components());
		}
		pane.getChildren().addAll(tText, hiloText, fText);
		for (int i = 0; i < games.length; i++) {
			pane.getChildren().add(games[i].components());
		}
		
		
		// draw
		EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				draw(stage);
			}
		};
		Timeline timeline = new Timeline(new KeyFrame(Duration.ONE, eh));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
	
	/**
	 * Updates mousePressed, mouseReleased, mouseX, and mouseY variables.
	 */
	public void updateMouse() {
		// mousePressed
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mousePressed = true;
			}
		});
		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mousePressed = false;
				mouseReleased = true;
			}
		});
		
		// mouseX, mouseY
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mouseX = (int) t.getSceneX();
				mouseY = (int) t.getSceneY();
			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				mouseX = (int) t.getSceneX();
				mouseY = (int) t.getSceneY();
			}
		});
	}
	
	/**
	 * Resets values of mouseReleased and keyReleased to false at the end of the loop.
	 * Necessary for mouseReleased and keyReleased to function correctly.
	 */
	public void resetReleased() {
		mouseReleased = false;
		keyReleased = false;
	}
	
	/**
	 * Updates the value of keyPressed, key, and keyCode.
	 */
	public void updateKey() {
		// keyPressed + key
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				keyPressed = true;
				key = t.getCode().toString();
				if (key.equals("SHIFT")) {
					shiftDown = true;
				}
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				keyPressed = false;
				keyReleased = true;
				if (t.getCode().toString().equals("SHIFT")) {
					shiftDown = false;
				}
			}
		});
	}
	
	/*public void initFacts() throws IOException {
		// retrieve data
		Document doc = Jsoup.connect("https://www.factslides.com/s-Fact-Of-The-Day").get();
		String page = doc.text();
		String p2 = page.substring(page.indexOf("Logout")+7, page.length());
		String fact = p2.substring(0, p2.indexOf(". ")+1);
		Text fText = new Text(910, 550, "Fun Fact: " + fact);
		fText.setFont(new Font("Avenir", 18));
		fText.setFill(Color.WHITE);
		fText.setWrappingWidth(490);
		pane.getChildren().add(fText);
	}*/
}