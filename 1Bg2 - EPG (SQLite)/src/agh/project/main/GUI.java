package agh.project.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.*; 
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import agh.project.sqlite.Programs;


public class GUI {
	static MySQL my = new MySQL();
	private static final Logger log = Logger.getLogger(Main.class);
	public static ArrayList<String> filePaths = new ArrayList<String>();
	public static ArrayList<String> fileNames = new ArrayList<String>();
	
	private static void createAndShowGUI() throws IOException {
		String menu_buttons[] = {"Update Files", "Update Base", "XXX", "Exit"};
		String main_app_elements[] = {"prev", "now", "next"};
		
		JButton menu_button;
		
		JFrame frame = new JFrame("TV - Parser");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		JTextField status = new JTextField("Witaj w aplikacji - TV Parser!", 30);
        status.setEditable(false);
        frame.getContentPane().add(status, BorderLayout.NORTH);
        JTextField indi_now;
        JTextField indi_prev;
        JTextField indi_next;
        
        
        // create object of table and table model
        JTable table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        String header[] = new String[] { "Hour", "Min",
        		"Ch_ID", "Title", "Description" };
    	    	
    	// add header in table model     
    	 dtm.setColumnIdentifiers(header);
    	//set model into the table object
    	table.setModel(dtm);
    	//get model
    	DefaultTableModel model = (DefaultTableModel) table.getModel();
        // Make the table vertically scrollable
    	
    	Dimension tableSize = table.getSize();
    	//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	table.getColumnModel().getColumn(0).setPreferredWidth(40);
    	table.getColumnModel().getColumn(1).setPreferredWidth(40);
    	table.getColumnModel().getColumn(2).setPreferredWidth(40);
    	table.getColumnModel().getColumn(3).setPreferredWidth(170);
    	table.getColumnModel().getColumn(4).setPreferredWidth(400);
    	
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        frame.getContentPane().add(scrollPane, BorderLayout.SOUTH);
        
        JPanel menu = new JPanel(new GridLayout(4, 1, 5, 5));
        frame.getContentPane().add(menu, BorderLayout.WEST);
        JPanel main_app_container = new JPanel(new GridLayout(2, 2, 5, 5));
        frame.getContentPane().add(main_app_container, BorderLayout.EAST);
        JPanel main_app = new JPanel(new GridLayout(1, 3, 5, 5));
        
        JComboBox<String> selector = new JComboBox<>();
        
        //fill menu panel (WEST)
        for (String s1 : menu_buttons){
        	menu_button = new JButton(s1);
        	menu.add(menu_button);
        	menu_button.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){               
	                if(s1==menu_buttons[0]){
	                	status.setText("File Updating...");
	                	new PropertiesAll();
	            		Properties prop = PropertiesAll.setAll();
	            		try {
	            			DownloaderPool.startDownload(prop);
	            			
						} catch (IOException e1) {
							status.setText("ERROR, TRY AGAIN!");
							e1.printStackTrace();
							log.error(e1, e1);
						}
	                }
	                else if(s1==menu_buttons[1]){
	                	status.setText("Parsing webpage...");
	                	GetPageData reader = new GetPageData();				//Class to parse webpage.
	            		filePaths = reader.searchDir();						//Find HTML-files on HardDisc and return list of them.
	            		fileNames = reader.getPrograms(filePaths);			//Get All names of channels.
	            		reader.getTV(filePaths);							//Parse webpage !
	            		for (String src : fileNames){
	            			selector.addItem(src);
	            		}
	            		status.setText("DONE !");
	                }
	                else if(s1==menu_buttons[3]){
	                	status.setText("Exit!");
	                	System.exit(0);
	                }
	                else{
	                	status.setText("JButton: "+s1);
	                }
		        	}
		    	}
        	);
        }
        //fill main application (EAST)
        selector.setVisible(true);
        main_app_container.add(selector);
        main_app_container.add(main_app);
        
        indi_prev = new JTextField(main_app_elements[0], 30);
    	indi_prev.setVisible(true);
    	indi_prev.setEditable(false);
    	main_app.add(indi_prev);
    	
    	indi_now = new JTextField(main_app_elements[1], 30);
    	indi_now.setVisible(true);
    	indi_now.setEditable(false);
    	main_app.add(indi_now);
    	
    	indi_next = new JTextField(main_app_elements[2], 30);
    	indi_next.setVisible(true);
    	indi_next.setEditable(false);
    	main_app.add(indi_next);
        
        selector.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
            	int channel_id = selector.getSelectedIndex();
            	status.setText("Change Program to: " + channel_id);
            	model.setRowCount(0);
            	List<Programs> programs = my.getOneChannel(channel_id);
        		for (Programs src : programs){
                	dtm.addRow(new Object[] { src.getHour(), src.getMinutes(),
        	                src.getChId(), src.getTitle(), src.getDescription() });
        			
        		}
        		List<Programs> current_prog = my.getCurrent(channel_id);
        		indi_prev.setText(" [" + current_prog.get(0).getHour() + ":" + current_prog.get(0).getMinutes() + "] " + current_prog.get(0).getTitle());
        		indi_now.setText(" [" + current_prog.get(1).getHour() + ":" + current_prog.get(1).getMinutes() + "] " + current_prog.get(1).getTitle());
        		indi_next.setText(" [" + current_prog.get(2).getHour() + ":" + current_prog.get(2).getMinutes() + "] " + current_prog.get(2).getTitle());
        		
            }
        });
        
        
        frame.pack();
        frame.setVisible(true);
	}

	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (IOException e) {
					log.error(e, e);
					e.printStackTrace();
				}
            }
        });
	}

}
