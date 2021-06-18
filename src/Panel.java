import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Panel extends JPanel {
	
	public Panel() {
		
		setLayout(null);
		setPreferredSize(new Dimension(1500, 800));
		setBackground(Color.BLACK);
		
		ImageIcon aIcon = new ImageIcon("assets/img/out.png");
		JLabel aImg = new JLabel(aIcon) {
			{
				setBounds(12, -75, 500, 400);
			}
		};
		JButton aPlay = new JButton("Play") {
			{
				setBounds(175, 275, 150, 100);
				setFont(rocketracer.RMain.uiTextMediumHighlight);
				setForeground(Color.WHITE);
				setBackground(Color.BLACK);
				setBorder(rocketracer.RMain.buttonBorder);
				setFocusable(false);
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						rocketracer.RMain.run();
					}
				});
			}
		};
		
		ImageIcon bIcon = new ImageIcon("images/out.png");
		JLabel bImg = new JLabel(bIcon) {
			{
				setBounds(500, 400, 500, 400);
			}
		};
		JLabel bPlay = new JLabel("PLAY", SwingConstants.CENTER) {
			{
				setBounds(680, 700, 120, 50);
				setFont(new Font("HYWenHei-85W", Font.PLAIN, 28));
				setBackground(Color.BLACK);
				setForeground(Color.RED);
				addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						helletbull.HMain.run();
					}
					
					public void mouseEntered(MouseEvent e) {
						// setOpaque(true);
						setForeground(Color.WHITE);
						repaint();
					}
					
					public void mouseExited(MouseEvent e) {
						// setOpaque(false);
						setForeground(Color.RED);
						repaint();
					}
				});
				
			}
		};
		
		ImageIcon cIcon = new ImageIcon("images/out3.png");
		JLabel cImg = new JLabel(cIcon) {
			{
				setBounds(870, -50, 800, 400);
			}
		};
		ImageIcon cPIcon = new ImageIcon("images/Play2.png");
		JButton cPlay = new JButton(cPIcon) {
			{
				setBounds(1175, 275, 150, 100);
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						com.game.game.MainMenu.jhee();
					}
				});
			}
		};
		
		ImageIcon dIcon = new ImageIcon("images/outed.png");
		JLabel dImg = new JLabel(dIcon) {
			{
				setBounds(-150, 325, 800, 400);
			}
		};
		ImageIcon dPIcon = new ImageIcon("images/go3.png");
		JLabel dPlay = new JLabel(dPIcon) {
			{
				setBounds(150, 690, 179, 100);
				addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						speedrunners.Driver.run();
					}
				});
			}
		};
		
		ImageIcon eIcon = new ImageIcon("images/axout.png");
		JLabel eImg = new JLabel(eIcon) {
			{
				setBounds(860, 325, 800, 400);
			}
		};
		ImageIcon ePIcon = new ImageIcon("images/maxout.png");
		JLabel ePlay = new JLabel(ePIcon) {
			{
				setBounds(1125, 625, 250, 71);
				addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						touhou.Main.run();
					}
				});
			}
		};
		
		ImageIcon fPIcon = new ImageIcon("images/zhout.png");
		JLabel fPlay = new JLabel(fPIcon) {
			{
				setBounds(0, 0, 500, 400);
			}
		};
		
		ImageIcon gPIcon = new ImageIcon("images/outx.png");
		JLabel gPlay = new JLabel(gPIcon) {
			{
				setBounds(1000, 400, 500, 400);
			}
		};
		ImageIcon hPIcon = new ImageIcon("images/sout.png");
		JLabel hPlay = new JLabel(hPIcon) {
			{
				setBounds(1000, 0, 500, 400);
			}
		};
		ImageIcon iPIcon = new ImageIcon("images/bout.png");
		JLabel iPlay = new JLabel(iPIcon) {
			{
				setBounds(525, 500, 450, 177);
			}
		};
		ImageIcon jPIcon = new ImageIcon("images/dangames.png");
		JLabel jPlay = new JLabel(jPIcon) {
			{
				setBounds(500, 0, 500, 400);
			}
		};
		ImageIcon kPIcon = new ImageIcon("images/oute.png");
		JLabel kPlay = new JLabel(kPIcon) {
			{
				setBounds(0, 400, 500, 400);
			}
		};
		
		add(bPlay);
		add(iPlay);
		add(aImg);
		add(aPlay);
		add(bImg);
		add(cPlay);
		add(cImg);
		add(dPlay);
		add(dImg);
		add(ePlay);
		add(eImg);
		add(fPlay);
		add(gPlay);
		add(hPlay);
		add(jPlay);
		add(kPlay);
	}
}
