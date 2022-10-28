import java.rmi.*;

public interface ServerIntf extends Remote {

  void lerDados() throws Exception,RemoteException;
  void saveDados() throws Exception,RemoteException;

  
}
