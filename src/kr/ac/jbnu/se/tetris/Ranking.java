package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Ranking extends JFrame {

	private static transient Ranking instance = new Ranking();

	private RankingList list;

	private transient boolean loaded;

	private transient JPanel panel;

	private transient JButton closeButton;

	private Ranking() {
		list = new RankingList();
		loaded = false;

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeFrame();
			}
		});

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public static Ranking getInstance() {
		return instance;
	}

	class SubmitPanel extends JPanel {

		public SubmitPanel(long score) {
			panel.removeAll();

			JLabel title = new JLabel("Submit your score to ranking:");
			JLabel scoreText = new JLabel("SCORE: " + score);
			JLabel infoText = new JLabel("");
			JTextField nameTextField = new JTextField("AAA");
			JButton btnYes = new JButton("Agree");
			JButton btnNo = new JButton("Disagree");

			infoText.setForeground(Color.RED);

			btnYes.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String text = nameTextField.getText();
					if (text.isEmpty() || text.length() < 3) {
						infoText.setText("NAME is too short.");
						return;
					}
					if (text.length() > 10) {
						infoText.setText("NAME is too long.");
						return;
					}
					addRanking(text, score);
					showRanking();
				}
			});

			btnNo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					closeFrame();
				}
			});

			panel.add(title);

			panel.add(scoreText);
			panel.add(nameTextField);
			panel.add(infoText);

			panel.add(btnYes);
			panel.add(btnNo);

			setContentPane(panel);
			setVisible(true);
		}
	}

	private void addRanking(String name, long score) {
		getInstance().list.insert(name, score);
		save();
	}

	public void showRanking() {
		panel.removeAll();

		int rows = 10, rankID = 1;
		for (RankingScore r : list.getList(rows)) {
			String dateText = new SimpleDateFormat("yyyy-MM-dd a hh:mm").format(r.getDate());
			panel.add(new JLabel(rankID + ". " + r.getName() + " (" + r.getScore() + "p) - " + dateText));
			rankID++;
		}
		for (; rankID <= rows; rankID++) {
			panel.add(new JLabel(rankID + ". ---"));
		}

		JPanel pClose = new JPanel();
		pClose.add(closeButton);
		panel.add(pClose);

		setSize(300, 600);
		setLocationRelativeTo(null);
		setContentPane(panel);
		setVisible(true);
	}

	public void requestSubmitScore(long score) {
		setTitle("Ranking");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setVisible(true);
		
		load();

		new SubmitPanel(score);
	}

	public void closeFrame() {
		setVisible(false);
		this.dispose();
	}

	synchronized public void save() {
		SerializationDemonstrator.serialize(getInstance().list, RankingList.FILENAME);

		loaded = false;
	}

	synchronized public void load() {
		if (isLoaded())
			return;

		File f = new File(RankingList.FILENAME);
		if (f.isFile() == false)
			return;

		try {
			getInstance().list = SerializationDemonstrator.deserialize(RankingList.FILENAME, RankingList.class);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		loaded = true;
	}

	public boolean isLoaded() {
		return loaded;
	}
}
