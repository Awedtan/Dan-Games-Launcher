import javax.swing.*;

public class Launch {
	
	static JFrame frame = new JFrame("Dan Games Launcher");
	
	public static void main(String[] args) throws Exception {
		JPanel panel = new Panel();
		
		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}