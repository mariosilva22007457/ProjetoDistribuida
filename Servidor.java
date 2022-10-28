import java.net.*;
import java.rmi.*;

public class Servidor
{    
    public static void main(String[] args) {

        try {

            ServerImpl ServerImpl = new ServerImpl();
            Naming.rebind("AddServer", ServerImpl);

        }catch(Exception e) {

            System.out.println("Exception: " + e);

        }
    }
}