package module;

import java.net.Socket;

public class ModuleSocket {
	
	public static void waitClosed(Socket socket) {
		try {
			socket.shutdownOutput();
			while(true) {
				try {
					if(socket.getInputStream().read()==-1) {
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
				Thread.sleep(1);
			}
			socket.shutdownInput();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}