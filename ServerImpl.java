import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.rmi.*;
import java.rmi.server.*;
import java.security.PublicKey;
import java.util.ArrayList;



public class ServerImpl extends UnicastRemoteObject implements ServerIntf {

    ArrayList<Reservas> listaReservas = new ArrayList<>();
    ArrayList<Reservas> listaReservasParaVerificacoes = new ArrayList<>();
   
    //VARIAVEIS GLOBAIS PARA EFEITO DE LOGS CORRETOS NO SERVIDOR 
    public static int idParaFile = 1;
    public static boolean erroMesas = false;
    public static boolean servidorReiniciou = false;
    public static int codigoID = 0;

    //Quando se liga o servidor
    public ServerImpl() throws RemoteException, Exception {

        System.out.println("Server turning ON\nGetting Data...");

        lerDados();
        servidorReiniciou = true;
        System.out.println("\n|DEBUG LISTA RESERVAS|");
        for (int i = 0; i < listaReservas.size(); i++) {
            System.out.println(listaReservas.get(i));
        }

        System.out.println("\n|DEBUG LISTA RESERVAS PARA VERIFICAÇÃO|");
        for (int i = 0; i < listaReservasParaVerificacoes.size(); i++) {
            System.out.println(listaReservasParaVerificacoes.get(i));
        }

        System.out.println("\nServer Ready");
    }

    public void saveDados(String dataMarcacao, String escolhaRefeicao, int numeroDePessoas, String nomeDaReserva) 
        throws RemoteException, Exception {

        //GUARDA DADOS NA BASE DE DADOS (Ficheiro TXT)
        try {  
            /* 
            //SABER QUANTAS LINHAS TEM O FILE
            FileReader fr = new FileReader("BaseDeDados.txt");
            BufferedReader reader = new BufferedReader(fr);
    
            String linha = null;
            
            while ((linha = reader.readLine()) != null) {
                numeroDaLinha++;
            }

            reader.close();
            */
            File file = new File("BaseDeDados.txt");
            //Argumento TRUE para que dê append no ficheiro e não apague registos antigos
            FileWriter writer = new FileWriter(file,true);
            PrintWriter write = new PrintWriter(writer);


            Reservas a = new Reservas(idParaFile,dataMarcacao, escolhaRefeicao, numeroDePessoas,nomeDaReserva);
            
            //listaReservas = listaReservasParaVerificacoes;
            listaReservasParaVerificacoes.add(a); 
            
            System.out.println("CHEGUEI 1");
            //CHAMADA FUNCAO QUE VERIFICA SE PODE REALIZAR A MARCACAO OU NAO
            marcarMesa(dataMarcacao,escolhaRefeicao, numeroDePessoas, nomeDaReserva);
            System.out.println("CHEGUEI 2");
            
            if(!mesaCodeErro()){

                //QUANDO REINICIAVA O SERVIDOR, A BASE DE DADOS FICAVA COM UM PROBLEMA NA PRIMEIRA RESERVA
                //ATRAVES DESTA IMPLEMENTAÇÃO, ERRO CORRIGIDO
                if(servidorReiniciou){

                    write.println("\n" + idParaFile + "@" + dataMarcacao + "@" + escolhaRefeicao + "@" + numeroDePessoas
                        + "@" + nomeDaReserva);
                    servidorReiniciou = false;

                }else{

                    write.println(idParaFile + "@" + dataMarcacao + "@" + escolhaRefeicao + "@" + numeroDePessoas
                        + "@" + nomeDaReserva);

                }
                
            }
            
           
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
 
            System.out.println("Nova Reserva registada no servidor/BD com ID = " + idParaFile);

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
                int  numeroDePessoas  = Integer.parseInt(dados[3]);
                String nomeDaReserva = dados [4];

                Reservas reserva = new Reservas(id, data, escolhaRefeicao,numeroDePessoas,nomeDaReserva);
                
                //Adiciona verdadeiramente no Arraylist de reservas
                listaReservas.add(reserva);
                
                //Adiciona também  no Arraylist de verificações para validação de marcações
                //Chamamos a funcao de marcar mesa, para guardar em memória as marcações antigas
                listaReservasParaVerificacoes.add(reserva);
                //marcarMesa(data,escolhaRefeicao, numeroDePessoas);

            }

            reader.close();
            fr.close();
         

        } catch (Exception e) {
            throw new Exception("ERRO AO TRANSFERIR DADOS DA BASE DE DADOS"); 
        }
    }

    public void marcarMesa(String DataInserida, String jantarOUalmocoInserido, int quantidadeDePessoas, String nomeDaReserva) 
        throws RemoteException {
    
        int contadorDeMesas2Pessoas = 0;
        int contadorDeMesas4Pessoas = 10;
        int contadorDeMesas8Pessoas = 15;
        int contadorDeMesas12Pessoas = 20;
        
        for (int i = 0; i < listaReservasParaVerificacoes.size(); i++) {
           
            if(listaReservasParaVerificacoes.get(i).getData().equals(DataInserida) 
                && listaReservasParaVerificacoes.get(i).getEscolhaRefeicao().equals(jantarOUalmocoInserido)
                && listaReservasParaVerificacoes.get(i).getNumeroDePessoas() == 2){

                erroMesas = false;

                contadorDeMesas2Pessoas++;
                idParaFile = contadorDeMesas2Pessoas; 

                System.out.println("cnt2:" + contadorDeMesas2Pessoas);

                if(contadorDeMesas2Pessoas > 10){

                    erroMesas = true;
                    mesaCodeErro();

                } else{

                    Reservas reservaValida = 
                    new Reservas(idParaFile, DataInserida, jantarOUalmocoInserido, quantidadeDePessoas,nomeDaReserva);
                    listaReservas.add(reservaValida);
                    //CODIGO ID DA MESA PARA O CLIENTE SABER
                    codigoID = idParaFile;

                }
            }

            if(listaReservasParaVerificacoes.get(i).getData().equals(DataInserida) 
                && listaReservasParaVerificacoes.get(i).getEscolhaRefeicao().equals(jantarOUalmocoInserido)
                && listaReservasParaVerificacoes.get(i).getNumeroDePessoas() == 4){
                    
                erroMesas = false;    

                contadorDeMesas4Pessoas++;
                idParaFile = contadorDeMesas4Pessoas; 

                System.out.println("cnt4:" + contadorDeMesas4Pessoas);

                if(contadorDeMesas4Pessoas > 15){

                    erroMesas = true;
                    mesaCodeErro();

                } else{

                    Reservas reservaValida = 
                    new Reservas(idParaFile, DataInserida, jantarOUalmocoInserido, quantidadeDePessoas,nomeDaReserva);
                    listaReservas.add(reservaValida);
                    //CODIGO ID DA MESA PARA O CLIENTE SABER
                    codigoID = idParaFile;                    

                }  
            }

            if(listaReservasParaVerificacoes.get(i).getData().equals(DataInserida) 
                && listaReservasParaVerificacoes.get(i).getEscolhaRefeicao().equals(jantarOUalmocoInserido)
                && listaReservasParaVerificacoes.get(i).getNumeroDePessoas() == 8){

                erroMesas = false;    

                contadorDeMesas8Pessoas++;
                idParaFile = contadorDeMesas8Pessoas; 

                System.out.println("cnt8:" + contadorDeMesas8Pessoas);

                if(contadorDeMesas8Pessoas > 20){

                    erroMesas = true;
                    mesaCodeErro();

                } else{

                    Reservas reservaValida =
                     new Reservas(idParaFile, DataInserida, jantarOUalmocoInserido, quantidadeDePessoas,nomeDaReserva);
                    listaReservas.add(reservaValida);
                    //CODIGO ID DA MESA PARA O CLIENTE SABER
                    codigoID = idParaFile;

                }  
            } 

            if(listaReservasParaVerificacoes.get(i).getData().equals(DataInserida) 
                && listaReservasParaVerificacoes.get(i).getEscolhaRefeicao().equals(jantarOUalmocoInserido)
                && listaReservasParaVerificacoes.get(i).getNumeroDePessoas() == 12){
                    
                erroMesas = false;

                contadorDeMesas12Pessoas++;
                idParaFile = contadorDeMesas12Pessoas; 

                System.out.println("cnt12:" + contadorDeMesas12Pessoas);

                if(contadorDeMesas12Pessoas > 25){

                    erroMesas = true;
                    mesaCodeErro();

                } else{

                    Reservas reservaValida =
                     new Reservas(idParaFile, DataInserida, jantarOUalmocoInserido, quantidadeDePessoas,nomeDaReserva);
                    listaReservas.add(reservaValida);
                    //CODIGO ID DA MESA PARA O CLIENTE SABER
                    codigoID = idParaFile;

                }  
            }

        }

    }

    public boolean mesaCodeErro() throws RemoteException {
        return erroMesas;
    }

    public int codigoIDmesa() throws RemoteException {
        return codigoID;
    }

        
    public void cancelarMesa(int idEntrada,String DataInserida, String jantarOUalmocoInserido) throws RemoteException {

        for (int i = 0; i < listaReservasParaVerificacoes.size(); i++) {

            if(listaReservasParaVerificacoes.get(i).getId() == idEntrada
                && listaReservasParaVerificacoes.get(i).getData().equals(DataInserida) 
                && listaReservasParaVerificacoes.get(i).getEscolhaRefeicao().equals(jantarOUalmocoInserido)){
                
                listaReservasParaVerificacoes.remove(i);
                listaReservas.remove(i);
                System.out.println("ENTREI NO CANCELAR");

                try {   
            
                    FileReader fr = new FileReader("BaseDeDados.txt");
                    BufferedReader reader = new BufferedReader(fr);
            
                    String linha = null;
                    
                    while ((linha = reader.readLine()) != null) {
                        System.out.println("ENTREI NO FILE");
                        
                        
                        String[] dados = linha.split("@");

                        int id = Integer.parseInt(dados[0]);
                        String data = dados[1];
                        String escolhaRefeicao  = dados[2];

                        if( (id==idEntrada) && (data == DataInserida) && (escolhaRefeicao == jantarOUalmocoInserido)){
                            linha = null;
                        }
                        linha.trim();
                        
        
                    }
        
                    reader.close();
                    fr.close();
                 
        
                } catch (Exception e) {}



            }
        }
    }

}
