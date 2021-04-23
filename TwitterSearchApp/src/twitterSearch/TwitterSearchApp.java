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

import twitter4j.Twitter;

import java.awt.Dimension;
import java.awt.Rectangle;

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
	private JButton btnSearchButton;
	private JButton btnTrendsButton;

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
					frame.setResizable(false);

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
		setFont(new Font("Tahoma", Font.PLAIN, 20));
		setTitle("Twitter Search App");
		twitter = TwitterAuth.getTwitterInstance();
		setForeground(new Color(135, 206, 250));
		setBackground(new Color(135, 206, 250));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 650);
		createAuthorPanel();

	}

	/**
	 * Creates the App title.
	 * 
	 * @param titleLabel
	 */
	private void createTitle(JLabel titleLabel) {
		titleLabel.setFont(new Font("Segoe Print", Font.BOLD, 25));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}

	/**
	 * Creates the bottom panel that displays the authors of the program.
	 */
	private void createAuthorPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);

		createAppTitle();

		JPanel searchPanel = createSearchPanel();

		createComboBox(searchPanel);

		createSearchField(searchPanel);

		createSearchButton(searchPanel);

		createGraphButton(searchPanel);

		createTrendsButton(searchPanel);

		createResultsLabel(searchPanel);

		JPanel buttonPanel = createButtonPanel();

		createResultsPane(buttonPanel);
	}

	private void createResultsPane(JPanel searchPanel) {
		// Creates the option to view all the data.
		JScrollPane scrollPane = new JScrollPane();
		searchPanel.add(scrollPane);
		scrollPane.setBorder(null);

		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 20));
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(null);
		buttonPanel.setPreferredSize(new Dimension(20, 20));
		buttonPanel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		buttonPanel.setBackground(new Color(30, 144, 255));
		contentPane.add(buttonPanel);
		buttonPanel.setLayout(new GridLayout(0, 1, 0, 0));
		return buttonPanel;
	}

	private void createResultsLabel(JPanel searchPanel) {
		// Results label.
		JLabel resultsLabel = new JLabel("Results");
		resultsLabel.setForeground(Color.WHITE);
		resultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		resultsLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		resultsLabel.setBounds(10, 144, 636, 31);
		searchPanel.add(resultsLabel);
		resultsLabel.setFont(new Font("Segoe Print", Font.BOLD, 25));
	}

	private void createTrendsButton(JPanel searchPanel) {
		// Creates the trends button.
		btnTrendsButton = new JButton("Trends");
		btnTrendsButton.setBackground(new Color(224, 255, 255));
		btnTrendsButton.setForeground(new Color(0, 0, 128));
		btnTrendsButton.setBounds(472, 66, 97, 35);
		searchPanel.add(btnTrendsButton);
		btnTrendsButton.setBorder(new EmptyBorder(5, 10, 5, 10));
		btnTrendsButton.setFont(new Font("Tahoma", Font.PLAIN, 20));

		btnTrendsButton.setEnabled(true);
		btnTrendsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textField.getText().equals("")) {
					JOptionPane.showInternalMessageDialog(null, "Please enter a query before clicking search.");
				} else if (comboBox.getSelectedItem().equals("Location")) {
					TrendsSearch trend = new TrendsSearch(twitter, textField.getText());
					String location = "";
					for (String tweet : trend.getTrendsInformation()) {
						location += tweet + "\n";
					}
					textPane.setText(location);
				}
			}
		});
		//buttonPanel.add(trendsButton);
	}

	private void createGraphButton(JPanel searchPanel) {
		btnGraphButton = new JButton("Graph");
		btnGraphButton.setBackground(new Color(224, 255, 255));
		btnGraphButton.setForeground(new Color(0, 0, 128));
		btnGraphButton.setBounds(372, 66, 90, 35);
		searchPanel.add(btnGraphButton);
		btnGraphButton.setBorder(new EmptyBorder(5, 10, 5, 10));
		btnGraphButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
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
	}

	private void createSearchButton(JPanel searchPanel) {
		// Creates the search button.
		btnSearchButton = new JButton("Search");
		btnSearchButton.setForeground(new Color(0, 0, 128));
		btnSearchButton.setBackground(new Color(224, 255, 255));
		btnSearchButton.setBounds(281, 66, 81, 35);
		searchPanel.add(btnSearchButton);
		btnSearchButton.setBorder(new EmptyBorder(5, 10, 5, 10));
		btnSearchButton.setFont(new Font("Tahoma", Font.PLAIN, 20));

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

	}

	private void createSearchField(JPanel searchPanel) {
		textField = new JTextField();
		textField.setBounds(229, 6, 340, 50);
		textField.setBackground(new Color(224, 255, 255));
		searchPanel.add(textField);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setPreferredSize(new Dimension(80, 50));
		textField.setMinimumSize(new Dimension(10, 20));
		textField.setBorder(null);
		textField.setColumns(20);
	}

	private void createAppTitle() {
		// Creates title for app.
		JLabel titleLabel = new JLabel("Twitter Search App");
		titleLabel.setBounds(new Rectangle(0, 0, 100, 100));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setOpaque(true);
		titleLabel.setBackground(new Color(30, 144, 255));
		createTitle(titleLabel);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		contentPane.add(titleLabel);
	}

	private JPanel createSearchPanel() {
		// Creates search panel.
		JPanel searchPanel = new JPanel();
		searchPanel.setBorder(null);
		searchPanel.setSize(new Dimension(0, 20000));
		searchPanel.setMaximumSize(new Dimension(32767, 20000));
		searchPanel.setBackground(new Color(30, 144, 255));
		contentPane.add(searchPanel);
		searchPanel.setLayout(null);
		return searchPanel;
	}

	/**
	 * Creates the drop-down box.
	 * 
	 * @param comboBox
	 */
	private void createComboBox(JPanel searchPanel) {
		// Creates combo box.
		comboBox = new JComboBox<>();
		comboBox.setBounds(103, 5, 100, 50);
		comboBox.setPreferredSize(new Dimension(100, 50));
		comboBox.setBorder(null);
		comboBox.setBackground(new Color(248, 248, 255));
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		comboBox.setPrototypeDisplayValue("Select...");
		comboBox.addItem("Select");
		comboBox.addItem("Person");
		comboBox.addItem("Query");
		comboBox.addItem("Location");
		searchPanel.add(comboBox);
		comboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				comboBox.removeItem("Select");
			}
		});
	}

}
