import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;


public class ServerImpl extends UnicastRemoteObject implements ServerIntf {

    ArrayList<Reservas> listaReservas = new ArrayList<>();
   
    //VARIAVEIS GLOBAIS PARA EFEITO DE LOGS CORRETOS NO SERVIDOR 
    public static int numeroDaLinha = 1;

    //Quando se liga o servidor
    public ServerImpl() throws RemoteException, Exception {

        System.out.println("Server turning ON\nGetting Data...");

        lerDados();

        for (int i = 0; i < listaReservas.size(); i++) {
            System.out.println(listaReservas.get(i));
        }

        System.out.println("Server Ready");
    }

    public void saveDados(String dataMarcacao, String escolhaRefeicao) throws RemoteException, Exception {

        //GUARDA DADOS NA BASE DE DADOS (Ficheiro TXT)
        try {  

            //SABER QUANTAS LINHAS TEM O FILE
            FileReader fr = new FileReader("BaseDeDados.txt");
            BufferedReader reader = new BufferedReader(fr);
    
            String linha = null;
            
            while ((linha = reader.readLine()) != null) {
                numeroDaLinha++;
            }

            reader.close();

            File file = new File("BaseDeDados.txt");
            //Argumento TRUE para que dê append no ficheiro e não apague registos antigos
            FileWriter writer = new FileWriter(file,true);
            PrintWriter write = new PrintWriter(writer);
            
            int idParaLogServer = 0;

            Reservas a = new Reservas(numeroDaLinha,dataMarcacao, escolhaRefeicao);
            listaReservas.add(a);
          
            int id = numeroDaLinha;
            write.println(id + "@" + dataMarcacao + "@" + escolhaRefeicao);

            /* /
            if(id == 1){
                write.println(id + "@" + dataMarcacao + "@" + escolhaRefeicao);
            }else{
                write.println(id + "@" + dataMarcacao + "@" + escolhaRefeicao);
            }
            
            
            //FORMA COMO GUARDAR: ID - DIA DA RESERVA - ALMOÇO/JANTAR
            /* 
            for (int id = numeroDaLinha+1; id < listaReservas.size()+1 ; id++) {
                if(verificaSeJaExiste(id)){
                    continue;
                }else{
                    write.append(id + "@" + dataMarcacao + "@" + escolhaRefeicao + "\n");                
                    idParaLogServer = id;
                }

                
                
            
                if(id < 1){
                    write.println(1 + "@" + dataMarcacao + "@" + escolhaRefeicao);
                    idParaLogServer = 1;
                }else{
                    write.println();
                    write.println(id + "@" + dataMarcacao + "@" + escolhaRefeicao);                
                    idParaLogServer = id;
                }
                
            }  
            */
 
            System.out.println("Nova Reserva registada no servidor/BD com ID = " + idParaLogServer);

            write.close();            
            
        } catch (Exception e) {
            throw new Exception("ERRO AO GRAVAR FICHEIRO"); 
        }

    }

    public void lerDados() throws RemoteException,Exception {


        try {   
            
            FileReader fr = new FileReader("BaseDeDados.txt");
            BufferedReader reader = new BufferedReader(fr);
    
            String linha = null;
            
            while ((linha = reader.readLine()) != null) {
    
                String[] dados = linha.split("@");

                int id = Integer.parseInt(dados[0]);
                String data = dados[1];
                String escolhaRefeicao  = dados[2];

                Reservas reserva = new Reservas(id, data, escolhaRefeicao);
                listaReservas.add(reserva);

            }

            reader.close();
            fr.close();
         

        } catch (Exception e) {
            throw new Exception("ERRO AO TRANSFERIR DADOS DA BASE DE DADOS"); 
  
        }
    }

}
