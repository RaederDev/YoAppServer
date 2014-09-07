package technology.raeder.yoappserver;

public class ServerConfiguration {
    
    private int port = -1;

    public ServerConfiguration(int port) {
        this.port = port;
    }
    
    public ServerConfiguration() {}
    
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
}
