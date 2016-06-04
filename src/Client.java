import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {

	private static Scanner s;
	private static DatagramSocket clientSocket;
	private static byte[] sendData;
	private static DatagramPacket sendPacket;
	private static DatagramPacket receivePacket;

	public static void main(String[] args) throws Exception {

		s = new Scanner(System.in);

		System.out.println("Input Server IP:");
		String sip = s.nextLine();

		System.out.println("Port:");
		String sport = s.nextLine();

		// prepare Socket
		clientSocket = new DatagramSocket();

		// prepare Data
		sendData = "Hello".getBytes();

		// send Data to Server with fix IP (X.X.X.X)
		// Client1 uses port 7070, Client2 uses port 7071
		sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(sip), Integer.parseInt(sport));
		clientSocket.send(sendPacket);

		// receive Data ==> Format:"<IP of other Client>-<Port of other Client>"
		receivePacket = new DatagramPacket(new byte[1024], 1024);
		clientSocket.receive(receivePacket);

		// Convert Response to IP and Port
		String response = new String(receivePacket.getData());
		String[] splitResponse = response.split("-");
		final InetAddress ip = InetAddress.getByName(splitResponse[0].substring(1));

		final int port = Integer.parseInt(splitResponse[1]);

		// output converted Data for check
		System.out.println("IP: " + ip + " PORT: " + port);

		// close socket and open new socket with SAME localport
		int localPort = clientSocket.getLocalPort();
		clientSocket.close();
		clientSocket = new DatagramSocket(localPort);

		// set Timeout for receiving Data
		clientSocket.setSoTimeout(1000);
		
		Thread getT = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						String in = s.nextLine();

						// send Message to other client
						sendData = in.getBytes();
						sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
						clientSocket.send(sendPacket);

						if (in.equals("exit")) {
							clientSocket.close();
							System.out.println("Closed");
							break;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		getT.start();

		Thread sendT = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						receivePacket.setData(new byte[1024]);
						clientSocket.receive(receivePacket);
						String ini = new String(receivePacket.getData());
						System.out.println("REC: " + ini);
						
						if (ini.equals("exit")) {
							clientSocket.close();
							System.out.println("Closed");
							break;
						}
					} catch (IOException e) {
//						e.printStackTrace();
					}
				}
			}
		});
		sendT.start();
	}
}
