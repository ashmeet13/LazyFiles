package FTPUtils;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.*;

public class FileTransferProtocol{
    public void upload(ObjectOutputStream output, File uploadFile)throws IOException{
        output.writeObject(uploadFile.getName().toString());
		output.flush();
		byte[] content = Files.readAllBytes(uploadFile.toPath());
		output.writeObject(content);
		output.flush();
    }

    public void download(ObjectInputStream input, String path)throws IOException{
        String fileName;
		File savingFile = null;
		byte[] content = null;
		try{
			fileName = input.readObject().toString();
			savingFile = new File(path,fileName);
			content = (byte[]) input.readObject();
		}catch(ClassNotFoundException classNotFoundException) {
			Messenger sender = new Messenger();
			sender.showMessage("Error");
		}
		Files.write(savingFile.toPath(), content);
    }
}