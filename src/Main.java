public class Main {
    public static void main(String[] args) {
        /*for (int i = 100; i <= 2000; i += 100) {
            Client client = new Client();
            client.simpleThread(i);
        }*/
        for (int i = 100; i <= 2000; i += 100) {
            Client client = new Client();
            client.threadsPool(i, 100);
        }
    }
}