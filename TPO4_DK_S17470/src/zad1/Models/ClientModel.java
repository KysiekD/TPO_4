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
	private Socket connectionSocket;
	private BufferedReader inBufferedReader;
	private PrintWriter outBufferedWriter;
	private boolean clientRunning;
	private static final int BSIZE = 1024;
	private static Charset charset = Charset.forName("ISO-8859-2"); // Strona kodowa do kodowania/dekodowania buforów
	private StringBuffer requestString = new StringBuffer(); // Tu bêdzie zlecenie do pezetworzenia
	private ByteBuffer byteBuffer = ByteBuffer.allocate(BSIZE); // Bufor bajtowy - do niego s¹ wczytywane dane z kana³u
	private SocketChannel socketChannel;
	
	public ClientModel(String host, int port) {
		clientHostName = host;
		clientPortNumber = port;
		clientRunning = true;
		System.out.println("Client " + clientHostName + " on port " + clientPortNumber + " started.");
		start();
	}

	public void run() {
		while (clientRunning) {

		}
	}

	public void connectToServer(String serverHost, int serverPort) {
		try {
			System.out.println("Client " + clientHostName + " on port " + clientPortNumber
					+ " is trying to connect server " + serverHost + " with port " + serverPort);
			connectionSocket = new Socket(serverHost, serverPort);
			//socketChannel = SocketChannel.open(); //??
			//socketChannel.socket().bind(new InetSocketAddress(serverHost,serverPort)); //??
			inBufferedReader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
			outBufferedWriter = new PrintWriter(connectionSocket.getOutputStream(), true);
			System.out.println("Success - client " + clientHostName + " on port " + clientPortNumber
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

	public void writeMsg(String msg) {
		outBufferedWriter.println(msg);

	}

	public String readMsg() {
		/*
		String msg = "empty";
		System.out.println("Client reads....");
		try {
			msg = inBufferedReader.readLine();
			System.out.println("Client reads...."+msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
		*/
		try {
			//https://stackoverflow.com/questions/9520911/java-sending-and-receiving-file-byte-over-sockets
			DataInputStream in = new DataInputStream(new BufferedInputStream(connectionSocket.getInputStream()));
			int count;
			byte[] bytesTable = new byte[8192]; // or 4096, or more
			while ((count = in.read(bytesTable)) > 0)
			{
				
				System.out.println("Bytes: "+count);
				String text1 = new String(bytesTable, "UTF-8");
				String text2 = new String(bytesTable, "ISO-8859-1");
				char[] chars = text1.toCharArray();
				System.out.println("CLIENT READS: "+new String(chars));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "null";
		
		//SocketChannel socketChannel = connectionSocket.getChannel();
		
		//if (!socketChannel.isOpen())
		//	return null; // je¿eli kana³ zamkniêty - nie ma nic do roboty
		/*
		requestString.setLength(0);
		byteBuffer.clear();
		try {
			System.out.println("KLIENT1");
			readLoop: 
			// String receivedMessage = inBufferedReader.readLine();
			while (true) { // Czytanie jest nieblokuj¹ce, kontynujemy je dopóki nie natrafimy na koniec
				// wiersza
				int n = connectionSocket.read(byteBuffer);
				if (n > 0) {
					byteBuffer.flip();
					CharBuffer charBuffer = charset.decode(byteBuffer);
					while (charBuffer.hasRemaining()) {
						char charTemp = charBuffer.get();
						if (charTemp == '\r' || charTemp == '\n')
							break readLoop; // tutaj moze byc blad
						requestString.append(charTemp);
					}
				}
			}

			System.out.println("KLIENT2");
			return requestString;
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return "Server doesn't respond to client " + clientHostName+" on port " +
		// clientPortNumber;
		return null;
		*/
	}

	public void viewMyTopics() {

	}

	public void viewServerTopics() {

	}

	public void suscribeTopic() {

	}

	public void unsuscribeTopic() {

	}

	public void showNews() {

	}
}
