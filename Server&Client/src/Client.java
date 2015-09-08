import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class Client {

	public static void main(String[] args) {
		
		int port = 12354;
		String url = "127.0.0.1";
		
		try {
			InetAddress address = InetAddress.getByName(url);
			
			Socket socket = new Socket(address, port);
			
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
			
			String line = null;
			
			BufferedReader keyboardStream = new BufferedReader(new InputStreamReader(System.in));
			
			while (true) {
				
				line = keyboardStream.readLine();
				dataOutputStream.writeUTF(line);
				dataOutputStream.flush();
				
				System.out.println(dataInputStream.readUTF());
				System.out.println("************************************************************");
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}
