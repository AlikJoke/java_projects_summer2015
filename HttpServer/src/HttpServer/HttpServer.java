package HttpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

	public static void main(String[] args) {
		
		int port = 8082;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			
			while(true) {
				
				Socket socket = serverSocket.accept();
				
				System.out.println();
				new Thread(new SocketProcessor(socket)).start();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}
