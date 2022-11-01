import java.rmi.*;

public interface ServerIntf extends Remote {

  void guardarDadosProvenientesTXT() throws Exception,RemoteException;
  void saveDados(String dataMarcacao, String escolhaRefeicao, int numeroDePessoas, String nomeDaReserva) throws RemoteException, Exception;
  //void marcarMesa(String DataInserida, String jantarOUalmocoInserido, int quantidadeDePessoas, String nomeDaReserva) throws RemoteException;
  boolean mesaCodeErro() throws RemoteException;
  void cancelarMesa(String DataInserida, String jantarOUalmocoInserido, String nomeDaReserva) throws RemoteException;
  //int codigoIDmesa() throws RemoteException;




}
