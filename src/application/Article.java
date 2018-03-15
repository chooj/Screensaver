package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Article {
	
	private Text tText, dText;
	private String url;
	private double opacity = 1.0;
	
	public Article(double x, double y, double w, double h, String article, String url) {
		String title;
		String description;
		if (parse(article).split("~").length > 0) {
			String[] bits = parse(article).split("~");
			title = bits[0];
			description = bits[1];
		} else {
			title = parse(article);
			description = "";
		}
		tText = new Text(0, 0, title);
		dText = new Text(0, 0, description);
		setTextProperties(x, y, w, h);
		this.url = url;
	}
	
	public String parse(String article) {
		String str = article;
		if (str.substring(0, 13).equals("News Analysis")) {
			str = str.substring(14, str.length());
		} else if (str.substring(0, 16).equals("The Interpreter")) {
			str = str.substring(17, str.length());
		} else if (str.substring(0, 10).equals("The Daily")) {
			str = str.substring(11, str.length());
		} else if (str.substring(0, 15).equals("Washington Memo")) {
			str = str.substring(16, str.length());
		}
		if (str.indexOf("Share Article") > -1) {
			str = str.substring(0, str.indexOf("Share Article"));
		}
		if (str.indexOf("By") > -1) {
			String title = str.substring(0, str.indexOf("By")-1);
			String description = str.substring(str.indexOf("By"));
			return title + "~" + description;
		} else {
			return str;
		}
	}
	
	public void setTextProperties(double x, double y, double w, double h) {
		tText.setFont(new Font("Avenir", 20));
		dText.setFont(new Font("Avenir", 12));
		tText.setFill(Color.grayRgb(255));
		dText.setFill(Color.grayRgb(255));
		tText.setLayoutX(x);
		dText.setLayoutX(x);
		tText.setWrappingWidth(w);
		dText.setWrappingWidth(w);
		tText.setLayoutY(y + 20);
		dText.setLayoutY(tText.getLayoutY() + tText.getLayoutBounds().getHeight());
		boolean edited = false;
		while (tText.getLayoutBounds().getHeight() + dText.getLayoutBounds().getHeight() > h) {
			dText.setText(dText.getText().substring(0, dText.getText().length()-1));
			edited = true;
		}
		if (edited) {
			dText.setText(dText.getText().substring(0, dText.getText().length()-3) + "...");
		}
	}
	
	public Group components() {
		Group g = new Group();
		g.getChildren().addAll(tText, dText);
		return g;
	}
	
	public boolean clicked(int mouseX, int mouseY, boolean mouseReleased) {
		return mouseX >= tText.getLayoutX() && mouseX <= tText.getLayoutX() + tText.getLayoutBounds().getWidth()
				&& mouseY >= tText.getLayoutY() && mouseY <= tText.getLayoutY() + tText.getLayoutBounds().getHeight()
				&& mouseReleased;
	}
	
	public String url() {
		return url;
	}
	
	public void draw(int mouseX, int mouseY, boolean mousePressed) {
		tText.setOpacity(opacity);
		dText.setOpacity(opacity);
		if (mouseX >= tText.getLayoutX() && mouseX <= tText.getLayoutX() + tText.getLayoutBounds().getWidth()
				&& mouseY >= tText.getLayoutY() && mouseY <= tText.getLayoutY() + tText.getLayoutBounds().getHeight()
				&& mousePressed) {
			opacity = 0.85;
		} else {
			opacity = 1;
		}
	}
	
}
