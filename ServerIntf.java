import java.rmi.*;

public interface ServerIntf extends Remote {

  void lerDados() throws Exception,RemoteException;
  void saveDados(String dataMarcacao, String escolhaRefeicao, int numeroDePessoas, String nomeDaReserva) throws Exception,RemoteException;
  void marcarMesa(String DataInserida, String jantarOUalmocoInserido, int quantidadeDePessoas, String nomeDaReserva) throws RemoteException;
  boolean mesaCodeErro() throws RemoteException;





}
