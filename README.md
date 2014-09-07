YoAppServer
===========

YoAppServer is a simple solution for creating server side applications that interact with the Yo developer API. It is written in Java utilizing many OpenSource technologies. The Server itself is not only a fully functional webserver to handle callbacks but also offers a API to interact with the Yo API servers.

To get started download the already compiled jar file:
[http://google.at][1]
or compile it from source using maven.


Once you have aquired the jar file put it into the folder where you want to run it from and execute the server using java: 

    java -jar YoAppServer.jar
The server will generate all needed configuration files and the apps folder. Once you have a working AppServer you can start developing your own apps.

Add the AppServer as a dependency for your Project and add a file called "app.json" in the root package of your project, the AppServer will read this configuration file when enabling your App.

    {
        "main": "your.package.app.App",
        "name": "MyAwesomeYo",
        "url": "/relativeCallbackUrl",
        "apiKey": "<yourApiKey>"
    }

 - The main parameter must be the full class name (including package) of the class in your project that extends YoApp.
 - The name parameter is the name of your project.
 - The url parameter is a relative callback url that the app should listen for. For example when you server has the address mydomain.com the appserver would listen on port 4242 (if you haven't changed that in the configuration file) resulting in the base url of: http://mydomain.com:4242/, when you now specify that you app listens for /relativeCallbackUrl your app will be notified when the YoApp servers accesses: http://mydomain.com:4242/relativeCallbackUrl
 - The apiKey parameter should be your API key, if you don't want to specify it in your app configuration you can also override the getApiKey method of YoApp and ommit this entry.
    
    public class App extends YoApp {

        private final YoCommunicator communicator;
      
        public App() {
            communicator = new YoCommunicator(this);
        }
  
        @Override
        public void onDisable() {
            System.out.println("BitcoinYo disable method called.");
        }
  
        @Override
        public void onEnable() {
            System.out.println("BitcoinYo enable method called.");
        }
      
        @Override
        public void onMessage(String username, String url, String userSuppliedUrl) {
            if(userSuppliedUrl != null) {
                System.out.println("The user has sent the following Url: " + userSuppliedUrl);
            }
            try {
                communicator.getSubscriberCount(new SuccessSubscriberCallback() {
                    @Override
                    public void run() {
                        if(getSuccess()) {
                            System.out.println("We have: " + getSubscribers() + " Subscribers!");
                        } else {
                            System.out.println("We have recveived an error: " + getError());
                        }
                    }
                });
                communicator.sendYo(new SuccessCallback() {
                    @Override
                    public void run() {
                        if(getSuccess()) {
                            System.out.println("The User has successfully received the Yo!");
                        } else {
                            System.out.println("An Error occured: " + getError());
                        }
                    }
                }, username);
            } catch (NoApiKeyException ex) {
                Logger.getLogger(BitcoinYo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
        

  [1]: http://google.at
