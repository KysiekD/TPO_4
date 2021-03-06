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
	// Przygoyowanie bufor�w:
	private static Pattern requestPattern = Pattern.compile("-", 3); // " +",
	private static String msgCodesTable[] = { "Ok", "Invalid request", "Not found" };
	private static Charset charset = Charset.forName("ISO-8859-2"); // Strona kodowa do kodowania/dekodowania bufor�w
	private static final int BSIZE = 1024;
	private ByteBuffer byteBuffer = ByteBuffer.allocate(BSIZE); // Bufor bajtowy - do niego s� wczytywane dane z kana�u
	private StringBuffer requestString = new StringBuffer(); // Tu b�dzie zlecenie do pezetworzenia

	public ServerMain(String host, int port) {
		// categoriesList = new ArrayList<>();

		try {
			serverSocket = ServerSocketChannel.open(); // Utworzenie kana�u dla gniazda serwera
			serverSocket.configureBlocking(false); // Tryb nieblokuj�cy
			//
			serverSocket.socket().bind(new InetSocketAddress(host, port)); // Ustalenie adresu (host+port) gniazda
																			// kana�u
			selector = Selector.open(); // Utworzenie selektora

			serverSocket.register(selector, SelectionKey.OP_ACCEPT); // Zarejestrowanie kana�u do obs�ugi przez selektor
			// dla tego kana�u interesuje nas tylko
			// nawi�zywanie po��cze�

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
				Checker.check("ServerMain serviceConnections();...");
				selector.select(); // Wywo�anie blokuj�ce,czeka na zaj�cie zdarzenia zwi�zanego z kana�ami,
									// zarejestrowanymi do obslugi przez selektor
				Set<SelectionKey> keys = selector.selectedKeys(); // Co� si� wydarzy�o na kana�ach,Zbi�r kluczy opisuje
																	// zdarzenia
				Iterator iterator = keys.iterator();

				while (iterator.hasNext()) { // dla ka�dego klucza
					SelectionKey key = (SelectionKey) iterator.next(); // pobranie klucza
					iterator.remove();// usuwamy, bo ju� go zaraz obs�u�ymy
					if (key.isAcceptable()) { // jaki� klient chce si� po��czy�
						SocketChannel socketChannel = serverSocket.accept(); // Uzyskanie kana�u do komunikacji z
																				// klientem, accept jest nieblokuj�ce,
																				// bo ju� klient si� zg�osi�
						socketChannel.configureBlocking(false); // Komunikacja z klientem - nieblokuj�ce we/wy
						socketChannel.register(selector, SelectionKey.OP_READ); // rejestrujemy kana� komunikacji z
																				// klientem, do obs�ugi przez selektor,
																				// typ zdarzenia - dane gotowe do
																				// czytania przez serwer
						continue;
					}
					if (key.isReadable()) { // kt�ry� z kana��w gotowy do czytania
						SocketChannel socketChannel = (SocketChannel) key.channel();
						serviceRequest(socketChannel); // ..obs�uga zlecenia..
						continue;
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// Obs�uga (JEDNEGO) zlecania:
	private void serviceRequest(SocketChannel socketChannel) {
		if (!socketChannel.isOpen())
			return; // je�eli kana� zamkni�ty - nie ma nic do roboty
		requestString.setLength(0);
		byteBuffer.clear();
		try {
			readLoop: while (true) { // Czytanie jest nieblokuj�ce, kontynujemy je dop�ki nie natrafimy na koniec
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
				//show all categories:
				Checker.check("ServerMain serviceRequest(): ...");
				System.out.println("SERVER: received message: " + requestString);
				String allCategoriesString = this.showAllCategories();
				writeResponse(socketChannel, 1, allCategoriesString);
			} else if (command.equals("2")) {
				//add client to subscription of the category
				System.out.println("SERVER: received message: " + requestString);
				Boolean success = this.addClientToSubscribesList(clientHostName, Integer.parseInt(clientPortNumber), clientName,
						msgFromClient);
				
				if(success = true) {
				writeResponse(socketChannel, 1, "Success - added to category subscritpion.");
				}else
					writeResponse(socketChannel, 1, "Falied - no such a category.");
			} else if (command.equals("3")) {
				System.out.println("SERVER: received message: " + requestString);
				//show news
				String news = showNews(clientName);
				writeResponse(socketChannel, 1, news);
			} else if (command.equals("4")) {
				System.out.println("SERVER: received message: " + requestString);
				//show client's categories
				String clientCategories = showClientCategories(clientName);
				writeResponse(socketChannel, 1, clientCategories);
			}else if(command.equals("5")) {
				System.out.println("SERVER: received message: " + requestString);
				Boolean success = this.removeClientFromSubscribesList(clientName, msgFromClient);
				if(success = true) {
					writeResponse(socketChannel, 1, "Success - removed category subscritpion.");
					}else
						writeResponse(socketChannel, 1, "Falied - no such a category.");
				
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

	public boolean addClientToSubscribesList(String host, int port, String clientName, String categoryName)
			throws InterruptedException {
		if (Category.getCategoryByName(categoryName) == null) {
			return false;
		} else {

			Category.getCategoryByName(categoryName).addClientToSubscribesList(clientName, host, port);
			return true;
		}
	}
	
	public boolean removeClientFromSubscribesList(String clientName, String categoryName) {
		if (Category.getCategoryByName(categoryName) == null) {
			return false;
		}else {
		Category.getCategoryByName(categoryName).removeClientFromSubscribesList(clientName);
		return true;
		}
	}

	public String showAllCategories() throws InterruptedException {
		String msg = "All categories: ";
		for (Category category : Category.getTopicExtension()) {
			// msg = msg.join(",",msg, category.getCategoryName());
			msg = msg + category.getCategoryName() + ",";
		}
		// Thread.sleep(2000);
		if (msg != null && msg.length() > 0 && msg.charAt(msg.length() - 1) == ',')
			msg = msg.substring(0, msg.length() - 1);
		return msg;
	}

	public String showNews(String clientName) {
		String news = "News: \n";
		String header = "";
		String content = "";
		for (Category category : Category.getTopicExtension()) {
			// System.out.println(category.getCategoryName()); //TEST
			try {
				if (category.getSubscribedClientsList().contains(clientName)) {
					// System.out.println(clientName); //TEST
					for (String newsHeader : category.getNewsModelsFromList()) {
						NewsModel newsModel = category.getNewsModelByHeader(newsHeader);
						header = newsModel.getNewsHeader();
						// System.out.println(header); //TEST
						content = newsModel.getNewsContent();
						// System.out.println(content); //TEST
						news = news + header.toUpperCase() + "-" + content.toLowerCase() + "\n";
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return news;
	}

	public String showClientCategories(String clientName) {
		String categoriesString = "My categories: ";

		for (Category category : Category.getTopicExtension()) {
			// System.out.println(category.getCategoryName()); //TEST

			try {
				if (category.getSubscribedClientsList().contains(clientName)) {
					// System.out.println(clientName); //TEST
					categoriesString = categoriesString + category.getCategoryName() + ",";
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (categoriesString != null && categoriesString.length() > 0
				&& categoriesString.charAt(categoriesString.length() - 1) == ',')
			categoriesString = categoriesString.substring(0, categoriesString.length() - 1);
		return categoriesString;
	}
}
