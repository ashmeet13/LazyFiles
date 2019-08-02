package FTPUtils;
import java.io.*;

public class Messenger{
    public void sendMessage(ObjectOutputStream output,String message){
		try{
			output.writeObject(message);
			output.flush();
			showMessage(message);
		}catch(IOException ioException) {
			System.out.println("ERROR: Cant Send message.");
		}
    }
    
	public void showMessage(String message) {
		System.out.println(message);
	}
}