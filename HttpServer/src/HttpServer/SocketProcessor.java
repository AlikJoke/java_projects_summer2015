package HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class SocketProcessor implements Runnable {

	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public SocketProcessor(Socket s) {
		
		this.socket = s;
		try {
			this.inputStream = s.getInputStream();
			this.outputStream = s.getOutputStream();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		readInput();
		writeResponse("<html><body><H1>Lol, this page for fucking users!</H1><body><html>");
		try {
			socket.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	private void readInput() {
		
		BufferedReader keyboardStream = new BufferedReader(new InputStreamReader(inputStream));
		
		while (true) {
			
			try {
				String line = keyboardStream.readLine();
				if (line == null || line.trim().length() == 0) {
					
					break;
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}

	private void writeResponse(String line) {
		
		String response = "HTTP/1.1 200 OK\r\n" +
                "Server: YarServer/2009-09-09\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + line.length() + "\r\n" +
                "Connection: close\r\n\r\n";
		
		String result_request = response + line;
		try {
			outputStream.write(result_request.getBytes());
			outputStream.flush();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
