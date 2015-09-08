package mysummerproject;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

public class Download extends Observable implements Runnable {

	private static final int MAX_SIZE = 1024;
	
	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETED = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;
	
	public static final String[] STATUSES = { "Downloading", "Paused", "Completed", "Cancelled", "Error" };
	
	URL url;
	int size;
	int size_downloaded;
	int status;
	
	public Download(URL url) {
		
		this.url = url;
		size = -1;
		size_downloaded = 0;
		status = DOWNLOADING;
	}
	
	public int getStatus() {
		
		return status;
	}
	
	public int getSize() {
		
		return size;
	}
	
	public int getProgress() {
		
		return (size_downloaded/size) * 100;
	}
	
	public String getURL() {
		
		return url.toString();
	}
	
	public void pause() {
		
		status = PAUSED;
		stateChanged();
	}
	
	public void resume() {
		
		status = DOWNLOADING;
		download();
		stateChanged();
	}
	
	public void error() {
		
		status = ERROR;
		stateChanged();
	}
	
	public void cancell() {
		
		status = CANCELLED;
		stateChanged();
	}
	
	public void download() {
		
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public String getFileName(URL url) {
		
		String fileName = url.getFile();
		return fileName.substring(fileName.lastIndexOf("/") + 1);
	}

	@Override
	public void run() {
		
		RandomAccessFile file = null;
		InputStream stream = null;
		
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Range", "bytes" + size_downloaded + "-");
			connection.connect();
			
			if (connection.getResponseCode() / 100 != 2) error();
			
			int contentLength = connection.getContentLength();
			if (contentLength < 1) error();
			
			if (size == -1) {
				
				size = contentLength;
				stateChanged();
			}
			
			file = new RandomAccessFile(getFileName(url), "rw");
			stream = connection.getInputStream();
			
			file.seek(size_downloaded);
			
			while (status == DOWNLOADING) {
				
				byte[] buf;
				if (size-size_downloaded > MAX_SIZE) {
					
					buf = new byte[MAX_SIZE];
				}
				else {
					
					buf = new byte[size-size_downloaded];
				}
				
				int numb = stream.read(buf);
				if (numb == -1) break;
				
				file.write(buf, 0, numb);
				size_downloaded += numb;
				stateChanged();
			}
			
			if (status == DOWNLOADING) {
				
				status = COMPLETED;
				stateChanged();
			}
			
			
		} catch (IOException e) {
			
			error();
		} finally {
			
			if (file != null)
				try {
					file.close();
				} catch (IOException e) {}
			
			if (stream != null)
				try {
					stream.close();
				} catch (IOException e) {}
		}
	}
	
	public void stateChanged() {
		
		setChanged();
		notifyObservers();
	}
}
