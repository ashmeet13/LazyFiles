package clientEnd;
public class ClientTest {
	public static void main(String args[]) {
		Client charlie;
		charlie = new Client("127.0.0.1");
		charlie.startRunning();
	}
}