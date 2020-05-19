package zad1.Models;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ClientModel extends Thread {

	private String clientHostName;
	private int clientPortNumber;
	private String clientName;
	private Socket connectionSocket;
	private BufferedReader inBufferedReader;
	private PrintWriter outBufferedWriter;
	private boolean clientRunning;
	private static final int BSIZE = 1024;
	private static Charset charset; // Strona kodowa do kodowania/dekodowania buforów
	private StringBuffer requestString; // Tu bêdzie zlecenie do pezetworzenia
	private ByteBuffer byteBuffer; // Bufor bajtowy - do niego s¹ wczytywane dane z kana³u
	private SocketChannel socketChannel;

	public ClientModel(String host, int port, String clientName) {
		clientHostName = host;
		clientPortNumber = port;
		this.clientName = clientName;
		requestString = new StringBuffer();
		byteBuffer = ByteBuffer.allocate(BSIZE);
		charset = Charset.forName("ISO-8859-2");
		clientRunning = true;
		System.out.println("CLIENT: " + clientHostName + " on port " + clientPortNumber + " started.");
		start();
	}

	public void run() {
		while (clientRunning) {

		}
	}

	public void connectToServer(String serverHost, int serverPort) {
		try {
			System.out.println("CLIENT:  " + clientHostName + " on port " + clientPortNumber
					+ " is trying to connect server " + serverHost + " with port " + serverPort);
			connectionSocket = new Socket(serverHost, serverPort);
			inBufferedReader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

			outBufferedWriter = new PrintWriter(connectionSocket.getOutputStream(), true);
			System.out.println("CLIENT: Success - " + clientHostName + " on port " + clientPortNumber
					+ " connected to server " + serverHost + " with port " + serverPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeConnectionToServer() {
		try {
			outBufferedWriter.close();
			inBufferedReader.close();
			connectionSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeMsg(int messageNumber, String clientName, String clientHostName, int clientPortNumber,
			String msg) {
		outBufferedWriter
				.println(messageNumber + "-" + clientName + "-" + clientHostName + "-" + clientPortNumber + "-" + msg);

	}

	public String readMsg() {
		String message = "";
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(connectionSocket.getInputStream()));
			int count;
			byte[] bytesTable = new byte[8192];
			// while ((count = in.read(bytesTable)) > 0) {
			count = in.read(bytesTable); //dodane w miejsce powy¿szej pêtli
			System.out.println("Bytes: " + count);
			String text1 = new String(bytesTable, "UTF-8");
			String text2 = new String(bytesTable, "ISO-8859-1");
			char[] chars = text1.toCharArray();
			message = new String(chars);
			System.out.println("CLIENT: message from server: " + message);

			// }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;

	}

	public String viewAllCategories() throws InterruptedException {
		writeMsg(1, this.getClientName(), this.getClientHostName(), this.getClientPortNumber(), "categories");
		Thread.sleep(2000);
		String msg = readMsg();
		// this.closeConnectionToServer();
		return msg;
	}

	public void viewMyCategories() {

	}

	public void suscribeCategory(String categoryName) throws InterruptedException {
		Thread.sleep(2000);
		writeMsg(2, this.getClientName(), this.getClientHostName(), this.getClientPortNumber(), categoryName);
		Thread.sleep(2000);

	}

	public void unsuscribeTopic() {

	}

	public void showNews() throws InterruptedException {
		Thread.sleep(2000);
		writeMsg(3, this.getClientName(), this.getClientHostName(), this.getClientPortNumber(), "news");
		Thread.sleep(2000);
	}

	public String getClientHostName() {
		return clientHostName;
	}

	public int getClientPortNumber() {
		return clientPortNumber;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}
