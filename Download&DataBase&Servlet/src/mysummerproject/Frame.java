package mysummerproject;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Frame extends JFrame implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Download download;
	
	boolean flag;
	
	JPanel panel, panelButtons, panelDownloads;
	DownloadTableModel tableModel;
	JTable table;
	JButton btnAdd, btnPause, btnClear, btnResume, btnCancell, btnRemove, btnExit;
	JTextField textField, nameFile;
	Thread thread;
	
	public Frame() {
		
		setTitle("Download Manager");
		setSize(1000, 600);
	
		panel = new JPanel();
		panelButtons = new JPanel();
		panelDownloads = new JPanel();
		
		tableModel = new DownloadTableModel();
		table = new JTable(tableModel);
		
		textField = new JTextField(35);
		nameFile = new JTextField(10);
		
		btnRemove = new JButton( "Remove" );
		btnExit = new JButton( "Exit" );
		btnAdd = new JButton( "Download" );
		btnPause = new JButton("Pause");
		btnResume = new JButton("Resume");
		btnCancell = new JButton("Cancell");
		btnClear = new JButton("Clear");
		
		
		panelDownloads.setLayout(new BorderLayout());
		panelDownloads.setBorder(BorderFactory.createTitledBorder("Downloads"));
		panelDownloads.add(new JScrollPane(table), BorderLayout.CENTER);
		panel.add(textField);

		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
				actionAdd();
				
			}
		});
		
		panel.add(btnAdd);
		
		btnExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
			}
		});
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				tableSelectionRowChange();
			}
		});
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		panel.add(btnExit);
		
		btnRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				textField.setText("");
			}
		});
		
		panel.add(btnRemove);
		
		btnPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				actionPause();
			}
		});
		
		btnPause.setEnabled(false);
		panelButtons.add(btnPause);
		
		btnResume.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				actionResume();
			}
		});
		
		btnResume.setEnabled(false);
		panelButtons.add(btnResume);
		
		btnClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				actionClear();
			}
		});
		
		btnClear.setEnabled(false);
		panelButtons.add(btnClear);
		
		btnCancell.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				actionCancell();
			}
		});
		
		btnCancell.setEnabled(false);
		panelButtons.add(btnCancell);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.NORTH);
		getContentPane().add(panelDownloads, BorderLayout.CENTER);
		getContentPane().add(panelButtons, BorderLayout.SOUTH);
	}
	
	private void actionCancell() {
		
		download.cancell();
		updateButtons();
	}
	
	private void actionClear() {
		
		flag = true;
		tableModel.removeDownload(table.getSelectedRow());
		flag = false;
		
		download = null;
		updateButtons();
	}
	
	private void tableSelectionRowChange() {
		
		if (download != null) download.deleteObserver(Frame.this);
		
		if (!flag) {
			
			download = tableModel.getDownload(table.getSelectedRow());
			download.addObserver(Frame.this);
			
			updateButtons();
		}
	} 
	
	private void actionResume() {
		
		download.resume();
		updateButtons();
	}
	
	private void actionPause() {
		
		download.pause();
		updateButtons();
	}
	
	private void updateButtons() {
		
		if (download != null) {
			
			int status = download.getStatus();
			
			switch (status) {
			
				case Download.DOWNLOADING:
					btnPause.setEnabled(true);
					btnResume.setEnabled(false);
					btnCancell.setEnabled(true);
					btnClear.setEnabled(false);
					break;
				case Download.PAUSED:
					btnPause.setEnabled(false);
					btnResume.setEnabled(true);
					btnCancell.setEnabled(true);
					btnClear.setEnabled(false);
					break;
				case Download.ERROR:
					btnPause.setEnabled(false);
					btnResume.setEnabled(true);
					btnCancell.setEnabled(false);
					btnClear.setEnabled(true);
					break;
				case Download.CANCELLED:
					btnPause.setEnabled(false);
					btnResume.setEnabled(true);
					btnCancell.setEnabled(false);
					btnClear.setEnabled(true);
					break;
				default:
					btnPause.setEnabled(false);
					btnResume.setEnabled(false);
					btnCancell.setEnabled(false);
					btnClear.setEnabled(true);
					break;
			}
		}
		else {
			
			btnPause.setEnabled(false);
			btnResume.setEnabled(false);
			btnCancell.setEnabled(false);
			btnClear.setEnabled(false);
		}
	}
	
	private void actionAdd() {
		
		String text = textField.getText();
		URL url = rightURL(text);
		
		if (url != null) {
			
			tableModel.addDownload(new Download(url));
			textField.setText("");
		}
		else {
			
			JOptionPane.showMessageDialog(this, "Invalid URL", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private URL rightURL(String text) {
		
		if (!text.toLowerCase().startsWith("http://")) {
			
			return null;
		}
		
		URL url;
		
		try {
			url = new URL(text);
		} catch (MalformedURLException e) {
			
			return null;
		}
		
		if (url.getFile().length() < 2) {
			
			return null;
		}
		return url;
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		
		if (download != null && download.equals(o)) {

			updateButtons();
		}
	}
}
