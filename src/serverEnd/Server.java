package serverEnd;
import java.io.*;
import java.net.*;
import FTPUtils.*;


public class Server {
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private String FilePath = "/media/ashmeet13/Data/Projects/Java/JavaFTP/FileSystem/ServerFiles/";
	FileTransferProtocol bridge;
	Messenger chatbox;
	
	public void startRunning(){
		try {
			server = new ServerSocket(1111, 100);
			while(true) {
				try{
					System.out.println("Waiting ...");
					waitForConnection();
					setupStreams();
					chatbox.sendMessage(output, "Server - Client Connected. \n\n");
					chatbox.showMessage("Host Address: "+connection.getInetAddress().getHostAddress());
					getCommandRequest();
				}catch(EOFException eofException) {
					chatbox.showMessage("Server ended the connection!");
				}finally {
					closeServer();
				}
			}
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	private void waitForConnection() throws IOException{
		connection = server.accept();
	}
	
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		bridge = new  FileTransferProtocol();
		chatbox = new Messenger();
	}

	private void recieveFile() throws IOException{
		bridge.download(input, FilePath);
		chatbox.showMessage("File Recieved");
	}	

	private void uploadFile()throws IOException{
		File myFile = ServerUtils.askFile(output, input, FilePath);
		bridge.upload(output, myFile);
	}



	public void decisionMenu(String choice)throws IOException{
		int caseValue = Integer.parseInt(choice);
		switch (caseValue){
			case 1:
				uploadFile();
				break;
			case 2:
				recieveFile();
				break;
			case 3:
				ServerUtils.listFiles(output, FilePath);
				break;
			default:
				chatbox.showMessage("Invalid Choice");
				try {
					choice = (String) input.readObject();
				}catch(ClassNotFoundException classNotFoundException) {
					chatbox.showMessage("Idk what is that");
				}
				decisionMenu(choice);
				break;
		}
	}

	
	private void getCommandRequest() throws IOException{
		String message = "";
		do{
			try {
				ServerUtils.printMenu(output);
				message = (String) input.readObject();
				decisionMenu(message);
			}catch(ClassNotFoundException classNotFoundException) {
				chatbox.showMessage("Idk what is that");
			}
		}while(!message.equals("CLIENT - END"));	
	}
	
	private void closeServer(){
		chatbox.showMessage("Clossing Connections... ");
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
		
}