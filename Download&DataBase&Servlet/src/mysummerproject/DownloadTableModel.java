package mysummerproject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

public class DownloadTableModel extends AbstractTableModel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final String[] columnNames = { "URL", "Size", "Progress", "Status" };
	@SuppressWarnings("rawtypes")
	public final Class[] columnClasses = { String.class, String.class, String.class, String.class };
	
	private ArrayList<Download> downloadList = new ArrayList<Download>();
	
	public void addDownload(Download download) {
		
		download.addObserver(this);
		downloadList.add(download);
		fireTableRowsInserted(getRowCount() - 1, getColumnCount() - 1 );
	}

	public Download getDownload(int row) {
		
		return (Download) downloadList.get(row);
	}
	
	public String getColumnName(int col) {
		
		return columnNames[col];
	}
	
	public Class<?> getColumnClass(int col) {
		
		return columnClasses[col];
	}
	
	public void removeDownload(int row) {
		
		
		downloadList.remove(row);
		fireTableRowsDeleted(row, row);
	}
	
	@Override
	public int getRowCount() {
		
		return downloadList.size();
	}

	@Override
	public int getColumnCount() {
		
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Download download = (Download) downloadList.get(rowIndex);
		
		switch (columnIndex) {
		
			case 0:
				return download.getURL();
			case 1:
				return (download.getSize() == -1) ? "" : Integer.toString(download.getSize());
			case 2:
				return Float.toString(download.getProgress());
			case 3:
				return Download.STATUSES[download.getStatus()];
		}
		
		return "";
	}

	@Override
	public void update(Observable o, Object arg) {
		
		int index = downloadList.indexOf(o);
		fireTableRowsUpdated(index, index);
	}

	
	
}
