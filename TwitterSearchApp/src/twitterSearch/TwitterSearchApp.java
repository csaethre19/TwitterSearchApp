package twitterSearch;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.MatteBorder;

import twitter4j.Twitter;

import java.awt.Dimension;

/**
 * Supplies an interface for the user to look up topics in Twitter. User can
 * visually see the data in a graph.
 * 
 * @author Charlotte Saethre
 * @author Briana Murdock
 *
 */
public class TwitterSearchApp extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JComboBox<String> comboBox;
	private Twitter twitter;
	private JTextPane textPane;
	private JButton btnGraphButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TwitterSearchApp frame = new TwitterSearchApp();
					frame.setVisible(true);
					Image icon = Toolkit.getDefaultToolkit().getImage("src/twitterSearch/Resources/twitter_icon.png");
					frame.setIconImage(icon);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TwitterSearchApp() {
		twitter = TwitterAuth.getTwitterInstance();
		setForeground(new Color(135, 206, 250));
		setBackground(new Color(135, 206, 250));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		createAuthorPanel();

	}

	/**
	 * Creates the App title.
	 * 
	 * @param titleLabel
	 */
	private void createTitle(JLabel titleLabel) {
		titleLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}

	/**
	 * Creates the bottom panel that displays the authors of the program.
	 */
	private void createAuthorPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);

		JLabel titleLabel = new JLabel("Twitter Search App");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setOpaque(true);
		titleLabel.setBackground(Color.BLUE);
		createTitle(titleLabel);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		contentPane.add(titleLabel);

		JPanel searchPanel = new JPanel();
		contentPane.add(searchPanel);
		searchPanel.setLayout(new GridLayout(1, 0, 0, 0));

		comboBox = new JComboBox<>();
		comboBox.setBackground(new Color(248, 248, 255));
		createComboBox(comboBox);
		searchPanel.add(comboBox);

		textField = new JTextField();
		searchPanel.add(textField);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setPreferredSize(new Dimension(10, 10));
		textField.setMinimumSize(new Dimension(10, 20));
		textField.setBorder(new MatteBorder(0, 10, 0, 10, (Color) Color.BLUE));
		textField.setColumns(10);

		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel);
		buttonPanel.setLayout(new GridLayout(0, 3, 0, 0));

		// Creates the search button.
		JButton btnSearchButton = new JButton("Search");
		buttonPanel.add(btnSearchButton);
		btnSearchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (textField.getText().equals("")) {
					JOptionPane.showInternalMessageDialog(null, "Please enter a query before clicking search.");
				} else if (comboBox.getSelectedItem().equals("Person")) {
					btnGraphButton.setEnabled(true);
					PersonSearch person = new PersonSearch(twitter, textField.getText());
					String timeLine = "";
					for (String tweet : person.getTimeline()) {
						timeLine += tweet + "\n";
					}
					textPane.setText(timeLine);
				} else if (comboBox.getSelectedItem().equals("Query")) {
					QuerySearch qs = new QuerySearch(twitter, textField.getText());
					String tweets = "";
					for (String tweet : qs.getTweetsInformation()) {
						tweets += tweet + "\n";
					}
					textPane.setText(tweets);

				}
			}

		});

		btnGraphButton = new JButton("Graph");
		buttonPanel.add(btnGraphButton);
		btnGraphButton.setEnabled(false);
		btnGraphButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (textField.getText().equals("")) {
					JOptionPane.showInternalMessageDialog(null, "Please enter a query before clicking graph.");
				} else {
					PersonSearch person = new PersonSearch(twitter, textField.getText());
					GraphTool graphTool = new GraphTool(person.getEdges(), person.getFollowers());
					graphTool.drawGraph();
				}

			}

		});

		JButton trendsButton = new JButton("Trends");
		trendsButton.setEnabled(true);
		trendsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
		buttonPanel.add(trendsButton);

		// Creates the option to view all the data.
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);

		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);

		// Results label.
		JLabel resultsLabel = new JLabel("Results");
		resultsLabel.setForeground(Color.WHITE);
		resultsLabel.setOpaque(true);
		resultsLabel.setBackground(Color.BLUE);
		resultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(resultsLabel);
	}

	/**
	 * Creates the drop-down box.
	 * 
	 * @param comboBox
	 */
	private void createComboBox(JComboBox<String> comboBox) {
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setPrototypeDisplayValue("Select...");
		comboBox.addItem("Select");
		comboBox.addItem("Person");
		comboBox.addItem("Query");
		comboBox.addActionListener(new ActionListener() {
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent event) {

				comboBox.removeItem("Select");

			}
		});
	}

}
