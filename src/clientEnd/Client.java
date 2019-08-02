package clientEnd;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.Scanner;

import FTPUtils.FileTransferProtocol;
import FTPUtils.Messenger;

public class Client {
	/*
	Variable Declarations
	*/


	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private String FilePath = "/media/ashmeet13/Data/Projects/Java/JavaFTP/FileSystem/ClientFiles";
	private Socket connection;
	FileTransferProtocol bridge;
	Messenger clientChat;
	Scanner in;





	public Client(String host) {
		serverIP = host;
	}
	
	public void startRunning() {
		while(true) {
			try {
				connectToServer();
				setupStreams();
				clientChat.showMessage("Connected to:"+ connection.getInetAddress().getHostName());
				whileChatting();
			}catch(EOFException eofException) {
				clientChat.showMessage("Client terminated connection");
			}catch(IOException ioException) {
				ioException.printStackTrace();
			}finally {
				closeServer();
			}
		}
	}
	
	private void connectToServer() throws IOException{
		connection = new Socket(InetAddress.getByName(serverIP), 1111);
	}
	
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		bridge = new  FileTransferProtocol();
		clientChat = new Messenger();
		in = new Scanner(System.in);
		clientChat.showMessage("Streams are connected");
	}
	
	private void closeServer() {
		clientChat.showMessage("Closing Server Down... ");
		try {
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}

	private void decisionMenu(String choice)throws IOException{
		System.out.println(choice.length());
		int caseValue = Integer.parseInt(choice);
		switch (caseValue){
			case 1:
				recieveFile();
				break;
			case 2:
				uploadFile();
				break;
			case 3:
				listFiles();
				break;
			default:

				//TODO: ADD THIS SECTION ON THE SERVER
				//Client enters a wrong choice and the server has already received it.
				//Here the client is adding a second choice. Keep the server ready to receive.

				System.out.println("Invalid Choice. Re-Enter your choice.");
				choice = getChoice();
				decisionMenu(choice);
		}
	}


	private String getChoice(){
		String choice = in.next();
		clientChat.sendMessage(output,choice);
		return choice;
	}

	
	private void whileChatting() throws IOException{
		do{
			try {
				message = (String) input.readObject();
				clientChat.showMessage(message);
				if(message.equals("Server - Client's Choice?")){
					System.out.print("Your Choice: ");
					String choice = getChoice();
					decisionMenu(choice);
				}
			}catch(ClassNotFoundException classNotFoundException) {
				clientChat.showMessage("Error: Bad Object Type");
			}
		}while(!message.equals("SERVER - END"));
	}

	private void listFiles(){
		System.out.println("\n\nThe server has these files:\n");
	}

	private void recieveFile() throws IOException{
		String message ="";
		while(true){
			try {
				message = (String) input.readObject();
				clientChat.showMessage(message);
				if(message.equals("Server - Client's Choice?")){
					System.out.print("Your Choice: ");
					String _ = getChoice();
					break;
				}
			}catch(ClassNotFoundException classNotFoundException) {
				clientChat.showMessage("Error: Bad Object Type");
			}
		}
		bridge.download(input, FilePath);
	}

	
	// TODO: Add the upload file code to select the file.
	private void uploadFile() throws IOException{
		File myFile = new File("/media/ashmeet13/Data/JavaRuns/check.tif");
		bridge.upload(output, myFile);
		clientChat.showMessage("File Sent");
	}

}