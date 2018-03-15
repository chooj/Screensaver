package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Button {
	
	private Rectangle button;
	private double x, y, width, height;
	private int r, g, b;
	private Text text;
	
	public Button(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		button = new Rectangle(x, y, width, height);
		double sizeParam = (width > height) ? height : width;
		button.setArcHeight(sizeParam/5);
		button.setArcWidth(sizeParam/5);
	}
	
	public void setColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public double textWidth() {
		return text.getLayoutBounds().getWidth();
	}
	
	public double textSize() {
		return text.getFont().getSize();
	}
	
	public void setTextLoc(double lxi, double lyi, double dx, double dy) {
		text.setLayoutX(lxi + dx);
		text.setLayoutY(lyi + dy);
	}
	
	public void setText(String str) {
		text = new Text(str);
		// determine size
		double size = height/2 + 1;
		do {
			size--;
			text.setFont(new Font("Verdana", size - 1));
		} while (textWidth() > 7*width/8 && size > 1);
		text.setX(x + width/2 - textWidth()/2);
		text.setY(y + height/2 + size/3);
		// determine color
		if (Color.rgb(r, g, b).getBrightness() > 0.5) {
			text.setFill(Color.grayRgb(30));
		} else {
			text.setFill(Color.grayRgb(220));
		}
	}
	
	public void setTextColor(int r, int g, int b) {
		text.setFill(Color.rgb(r,  g,  b));
	}
	
	public Rectangle getButton() {
		return button;
	}
	
	public Text getText() {
		return text;
	}
	
	public Group components() {
		Group g = new Group();
		g.getChildren().add(getButton());
		if (getText() != null) {
			g.getChildren().add(getText());
		}
		return g;
	}
	
	public boolean mouseInButton(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public int posNum(int a, int b) {
		return (a - b > 0) ? a - b : 0;
	}
	
	public void draw(int mouseX, int mouseY, boolean mousePressed) {
		if (mouseInButton(mouseX, mouseY)) {
			if (mousePressed) {
				button.setFill(Color.rgb(posNum(r,50),posNum(g,50),posNum(b,50)));
			} else {
				button.setFill(Color.rgb(posNum(r,25),posNum(g,25),posNum(b,25)));
			}
		} else {
			button.setFill(Color.rgb(r, g, b));
		}
	}
	
	public boolean clicked(int mouseX, int mouseY, boolean mouseReleased) {
		return mouseInButton(mouseX, mouseY) && mouseReleased;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
}
