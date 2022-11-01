import java.rmi.*;

public interface ServerIntf extends Remote {

  void guardarDadosProvenientesTXT() throws Exception,RemoteException;
  void saveDados(String dataMarcacao, String escolhaRefeicao, int numeroDePessoas, String nomeDaReserva) throws RemoteException, Exception;
  boolean mesaCodeErro() throws RemoteException;
  boolean cancelarMesa(String DataInserida, String jantarOUalmocoInserido, String nomeDaReserva) throws RemoteException;
  void listarMesasLivre(String DataInserida) throws RemoteException;
  String printMesasLivresAlmoco() throws RemoteException;
  String printMesasLivresJantar() throws RemoteException;
}
