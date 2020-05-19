package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import zad1.Models.Category;
import zad1.Models.ClientModel;
import zad1.Models.NewsModel;

public class ServerMain extends Thread {

	// private ArrayList<Category> categoriesList;

	private ServerSocketChannel serverSocket;
	private Selector selector;
	// Przygoyowanie buforów:
	private static Pattern requestPattern = Pattern.compile("-", 3); // " +",
	private static String msgCodesTable[] = { "Ok", "Invalid request", "Not found" };
	private static Charset charset = Charset.forName("ISO-8859-2"); // Strona kodowa do kodowania/dekodowania buforów
	private static final int BSIZE = 1024;
	private ByteBuffer byteBuffer = ByteBuffer.allocate(BSIZE); // Bufor bajtowy - do niego s¹ wczytywane dane z kana³u
	private StringBuffer requestString = new StringBuffer(); // Tu bêdzie zlecenie do pezetworzenia

	public ServerMain(String host, int port) {
		// categoriesList = new ArrayList<>();

		try {
			serverSocket = ServerSocketChannel.open(); // Utworzenie kana³u dla gniazda serwera
			serverSocket.configureBlocking(false); // Tryb nieblokuj¹cy
			//
			serverSocket.socket().bind(new InetSocketAddress(host, port)); // Ustalenie adresu (host+port) gniazda
																			// kana³u
			selector = Selector.open(); // Utworzenie selektora

			serverSocket.register(selector, SelectionKey.OP_ACCEPT); // Zarejestrowanie kana³u do obs³ugi przez selektor
			// dla tego kana³u interesuje nas tylko
			// nawi¹zywanie po³¹czeñ

			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("SERVER: started and ready for handling requests");
	}

	public void run() {
		serviceConnections();
	}

	private void serviceConnections() {
		boolean serverIsRunning = true;

		while (serverIsRunning) {

			try {

				selector.select(); // Wywo³anie blokuj¹ce,czeka na zajœcie zdarzenia zwi¹zanego z kana³ami,
									// zarejestrowanymi do obslugi przez selektor
				Set<SelectionKey> keys = selector.selectedKeys(); // Coœ siê wydarzy³o na kana³ach,Zbiór kluczy opisuje
																	// zdarzenia
				Iterator iterator = keys.iterator();

				while (iterator.hasNext()) { // dla ka¿dego klucza
					SelectionKey key = (SelectionKey) iterator.next(); // pobranie klucza
					iterator.remove();// usuwamy, bo ju¿ go zaraz obs³u¿ymy
					if (key.isAcceptable()) { // jakiœ klient chce siê po³¹czyæ
						SocketChannel socketChannel = serverSocket.accept(); // Uzyskanie kana³u do komunikacji z
																				// klientem, accept jest nieblokuj¹ce,
																				// bo ju¿ klient siê zg³osi³
						socketChannel.configureBlocking(false); // Komunikacja z klientem - nieblokuj¹ce we/wy
						socketChannel.register(selector, SelectionKey.OP_READ); // rejestrujemy kana³ komunikacji z
																				// klientem, do obs³ugi przez selektor,
																				// typ zdarzenia - dane gotowe do
																				// czytania przez serwer
						continue;
					}
					if (key.isReadable()) { // któryœ z kana³ów gotowy do czytania
						SocketChannel socketChannel = (SocketChannel) key.channel();
						serviceRequest(socketChannel); // ..obs³uga zlecenia..
						continue;
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// Obs³uga (JEDNEGO) zlecania:
	private void serviceRequest(SocketChannel socketChannel) {
		if (!socketChannel.isOpen())
			return; // je¿eli kana³ zamkniêty - nie ma nic do roboty
		requestString.setLength(0);
		byteBuffer.clear();
		try {
			readLoop: while (true) { // Czytanie jest nieblokuj¹ce, kontynujemy je dopóki nie natrafimy na koniec
										// wiersza
				int n = socketChannel.read(byteBuffer);
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
			String[] requestTable = requestPattern.split(requestString, 5);
			String command = requestTable[0];
			String clientName = requestTable[1];
			String clientHostName = requestTable[2];
			String clientPortNumber = requestTable[3];
			String msgFromClient = requestTable[4];

			if (command.equals("1")) {
				System.out.println("SERVER: received message: " + requestString);
				String allCategoriesString = this.showAllCategories();
				writeResponse(socketChannel, 1, allCategoriesString);
			} else if (command.equals("2")) {
				System.out.println("SERVER: received message: " + requestString);
				this.addClientToSubscribesList(clientHostName, Integer.parseInt(clientPortNumber), clientName,
						msgFromClient);
				writeResponse(socketChannel, 1, "Success - added to category subscritpion.");
			} else if (command.equals("3")) {
				String news = "";
				String header = "";
				String content = "";
				System.out.println("SERVER: received message: " + requestString);
				for (Category category : Category.getTopicExtension()) {
					//System.out.println(category.getCategoryName()); //TEST
					if (category.getSubscribedClientsList().contains(clientName)) {
						//System.out.println(clientName); //TEST
						for (String newsHeader : category.getNewsModelsFromList()) {
							NewsModel newsModel = category.getNewsModelByHeader(newsHeader);
							header = newsModel.getNewsHeader();
							//System.out.println(header); //TEST
							content = newsModel.getNewsContent();
							//System.out.println(content); //TEST
							news = news + header.toUpperCase() + "-" + content.toLowerCase() + "\n";
						}
					}
				}
				System.out.println(news);
				writeResponse(socketChannel, 1, news);
			} else {
				System.out.println("Bad request.");
			}
			// ..if..
			// ..if..
			// ..if..
			// ..if..
			// ..if..
			// ..if..
			// ..if..
			// ..if..
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	private StringBuffer answerStringBuffer = new StringBuffer(); // Odpowiedz

	private void writeResponse(SocketChannel socketChannel, int msgTypeCodeNumber, String message) throws IOException {
		answerStringBuffer.setLength(0);
		answerStringBuffer.append(msgTypeCodeNumber);
		answerStringBuffer.append("  ");
		answerStringBuffer.append(msgCodesTable[msgTypeCodeNumber]);
		answerStringBuffer.append('\n');
		if (message != null) {
			answerStringBuffer.append(message);
			answerStringBuffer.append('\n');
		}
		ByteBuffer byteBuffer = charset.encode(CharBuffer.wrap(message));
		socketChannel.write(byteBuffer);
		System.out.println("SERVER: writeResponse: > " + message + "<  -> client"); // TEST
	}
	/*
	 * public void addNewCategory(Category categoryName) {
	 * categoriesList.add(categoryName); }
	 */

	public void addClientToSubscribesList(String host, int port, String clientName, String categoryName)
			throws InterruptedException {
		Category.getCategoryByName(categoryName).addClientToSubscribesList(clientName, host, port);
	}

	public String showAllCategories() throws InterruptedException {
		String msg = "Categories: ";
		for (Category category : Category.getTopicExtension()) {
			// msg = msg.join(",",msg, category.getCategoryName());
			msg = msg + category.getCategoryName() + ",";
		}
		Thread.sleep(2000);
		return msg;
	}
}
