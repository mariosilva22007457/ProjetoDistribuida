import java.rmi.*;

public interface ServerIntf extends Remote {

  void lerDados() throws Exception,RemoteException;
  void saveDados(String dataMarcacao, String escolhaRefeicao) throws Exception,RemoteException;


}
