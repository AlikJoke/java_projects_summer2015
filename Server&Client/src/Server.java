import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

	public static void main(String[] args) {
		
		int port = 12354;
		
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			Socket socket = serverSocket.accept();
			System.out.println("Client connected");
			
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
			
			String line = null;
			
			while (true) {
				
				line = dataInputStream.readUTF();
				System.out.println("Message: " + line);
				System.out.println("********************************************************");
				dataOutputStream.writeUTF("Hey!Why you send me this message:" + line);
				dataOutputStream.flush();
				System.out.println("********************************************************");
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
