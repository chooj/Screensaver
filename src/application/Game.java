package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Game {
	
	private Text t1, t2;
	
	public Game(double x, double y, String str) {
		str = str.substring(1, str.length()-1);
		String[] bits = str.split(" ");
		t1 = new Text(x, y, "");
		t2 = new Text(x, y+18, "");
		t1.setFill(Color.WHITE);
		t2.setFill(Color.WHITE);
		t1.setFont(new Font("Avenir", 18));
		t2.setFont(new Font("Avenir", 15));
		parse(str, bits);
	}
	
	public void parse(String str, String[] bits) {
		String team = (str.substring(0, 5).equals("Trail")) ? "Trail Blazers": bits[0];
		boolean played = !(str.charAt(str.length()-1) == 'M');
		int aInd = (team.equals("Trail Blazers")) ? 1 : 0;
		t1.setText("v. " + team);
		if (played) {
			t2.setText(bits[1+aInd] + " " + bits[2+aInd]);
		} else {
			t2.setText(bits[1+aInd] + " @ " + bits[2+aInd] + " " + bits[3+aInd]);
		}
	}
	
	public Group components() {
		Group g = new Group();
		g.getChildren().addAll(t1, t2);
		return g;
	}
}
