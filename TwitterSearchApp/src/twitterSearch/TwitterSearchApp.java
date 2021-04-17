package twitterSearch;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.MatteBorder;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.ComponentOrientation;
import javax.swing.JSeparator;
/**
 * Supplies an interface for the user to look up topics in Twitter.
 * User can visually see the data in a graph.
 * 
 * @author Charlotte Saethre
 * @author Briana Murdock
 *
 */
public class TwitterSearchApp extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TwitterSearchApp frame = new TwitterSearchApp();
					frame.setVisible(true);
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
	 * Creates the bottom panel that displays the authors
	 *  of the program.
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
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBackground(new Color(248, 248, 255));
		createComboBox(comboBox);
		panel_1.add(comboBox);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setPreferredSize(new Dimension(10, 10));
		textField.setMinimumSize(new Dimension(10, 20));
		textField.setBorder(new MatteBorder(0, 10, 0, 10, (Color) Color.BLUE));
		panel_1.add(textField);
		textField.setColumns(10);
		
		// Creates the search button.
		JButton btnNewButton = new JButton("Search");
		panel_1.add(btnNewButton);
		
		// Creates exit button.
		JButton btnNewButton_1 = new JButton("Exit");
		panel_1.add(btnNewButton_1);
		
		// Creates the option to view all the data.
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);
		
		// Creates the box for user to search a topic.
		JTextPane textPane = new JTextPane();
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
	private void createComboBox(JComboBox comboBox) {
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setPrototypeDisplayValue("Select...");
		comboBox.addItem("Select");
		comboBox.addItem("Person");
		comboBox.addItem("Query");
		comboBox.addActionListener(new ActionListener() {
			/**
			 * 
			 */
            public void actionPerformed(ActionEvent event) 
            {               
              
            	comboBox.removeItem("Select");
                
            }
        });
	}

}
