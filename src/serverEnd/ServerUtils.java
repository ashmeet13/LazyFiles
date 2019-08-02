package serverEnd;
import FTPUtils.*;
import java.io.*;


public class ServerUtils {
	static Messenger chatbox = new Messenger();
	public static void printMenu(ObjectOutputStream output){
		chatbox.sendMessage(output,"Server - Choose the operation you wish to perform");
		chatbox.sendMessage(output,"1 - Download a file from the server directory");
		chatbox.sendMessage(output,"2 - Upload a file to the server directory");
		chatbox.sendMessage(output,"3 - List the File's in the server directory");
		chatbox.sendMessage(output,"Server - Client's Choice?");
	}
	
	public static File[] getFileList(String FilePath){
		File folder = new File(FilePath);
		return folder.listFiles();
	}

	public static void listFiles(ObjectOutputStream output, String FilePath){
		File[] files = getFileList(FilePath);
		for(int i=0; i<files.length; i++){
			if(files[i].isFile()){
				chatbox.sendMessage(output,"Index Number: "+(i+1)+"---- File Name: "+files[i].getName());
			}
		}
		chatbox.sendMessage(output,"\n");
	}
	
	public static File askFile(ObjectOutputStream output, ObjectInputStream input, String FilePath)throws IOException{
		chatbox.sendMessage(output,"Server - Choose File Index you wish to download: \n\n");
		listFiles(output,FilePath);
		String message = "";
		chatbox.sendMessage(output,"Server - Client's Choice?");
		try{
			message = (String) input.readObject();
		}catch(ClassNotFoundException classNotFoundException) {
			chatbox.showMessage("Idk what is that");
		}
		int i = Integer.parseInt(Character.toString(message.charAt(1)));
		File[] files = getFileList(FilePath);
		File myFile = files[i-1];
		return myFile;
	}
}
