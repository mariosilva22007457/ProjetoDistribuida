import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;


public class ServerImpl extends UnicastRemoteObject implements ServerIntf {

    //LISTA QUE GUARDA TODAS AS RESERVAS
    ArrayList<Reservas> listaReservas = new ArrayList<>();
   
    //VARIAVEIS GLOBAIS PARA EFEITO DE ERROS/LOGS/REINICIO SERVIDOR CORRETOS NO SERVIDOR 
    public static boolean servidorReiniciou = false;
    public static boolean erroMesas = false;


    //Quando se liga o servidor
    public ServerImpl() throws RemoteException, Exception {

        System.out.println("Server turning ON\nGetting Data...");

        guardarDadosProvenientesTXT();

        if(listaReservas.size() > 1){
            servidorReiniciou = true;
        }
        
        //DEBUG -> MOSTRA NO SERVIDOR, OS DADOS IMPORTADOS DO FILE, QUANDO SE LIGA O SERVIDOR
        System.out.println("\n|DEBUG LISTA RESERVAS|");
        for (int i = 0; i < listaReservas.size(); i++) {
            System.out.println(listaReservas.get(i));
        }

        System.out.println("\nServer Ready");
    }

    public void guardarDadosNoTXT(){
        try {

            File file = new File("BaseDeDados.txt");
            //Argumento TRUE para que dê append no ficheiro e não apague registos antigos
            FileWriter writer = new FileWriter(file);
            PrintWriter write = new PrintWriter(writer);

            System.out.println("tam " + listaReservas.size());
            for (int i = 0; i < listaReservas.size(); i++) {
                
               if(servidorReiniciou){
                    write.println("\n" + listaReservas.get(i).getId() + "@" + listaReservas.get(i).getData() 
                    + "@" + listaReservas.get(i).getEscolhaRefeicao() + "@" + listaReservas.get(i).getNumeroDePessoas() 
                    + "@" + listaReservas.get(i).getNomeDaReserva() );
                }else{
                    write.println(listaReservas.get(i).getId() + "@" + listaReservas.get(i).getData() 
                    + "@" + listaReservas.get(i).getEscolhaRefeicao() + "@" + listaReservas.get(i).getNumeroDePessoas() 
                    + "@" + listaReservas.get(i).getNomeDaReserva() );
                }    

            }
           
            write.close();   

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void saveDados(String dataMarcacao, String escolhaRefeicao, int numeroDePessoas, String nomeDaReserva) 
        throws RemoteException, Exception {


        int contadorDeMesas2PessoasAlmoco = 0;
        int contadorDeMesas4PessoasAlmoco = 10;
        int contadorDeMesas8PessoasAlmoco = 15;
        int contadorDeMesas12PessoasAlmoco = 20;
    
        int contadorDeMesas2PessoasJantar = 0;
        int contadorDeMesas4PessoasJantar = 10;
        int contadorDeMesas8PessoasJantar = 15;
        int contadorDeMesas12PessoasJantar = 20;
        

        //GUARDA DADOS NA BASE DE DADOS (Ficheiro TXT)
        if(listaReservas.size() == 0){
            Reservas dadosRecebidosAlmoco = new Reservas(1,dataMarcacao, escolhaRefeicao, numeroDePessoas,nomeDaReserva);
            listaReservas.add(dadosRecebidosAlmoco);
            
           
        }else{
            System.out.println("tamanho array: " + listaReservas.size());
            for (int i = 0; i < listaReservas.size(); i++) {

                System.out.println("ENTREI NO FOR");
    
    
    
                if(listaReservas.get(i).getData().equals(dataMarcacao) 
                    && listaReservas.get(i).getEscolhaRefeicao().equals(escolhaRefeicao)
                    && listaReservas.get(i).getNumeroDePessoas() == 2 
                    ){
                    
                    if(listaReservas.get(i).getEscolhaRefeicao().equals("A")){
                        contadorDeMesas2PessoasAlmoco++; 
                    }else{
                        contadorDeMesas2PessoasJantar++;
                    }
                    
                }

                if(listaReservas.get(i).getData().equals(dataMarcacao) 
                && listaReservas.get(i).getEscolhaRefeicao().equals(escolhaRefeicao)
                && listaReservas.get(i).getNumeroDePessoas() == 4 
                ){
                
                    if(listaReservas.get(i).getEscolhaRefeicao().equals("A")){
                        contadorDeMesas4PessoasAlmoco++;
                    }else{
                        contadorDeMesas4PessoasJantar++;
                    }
                    
                }

                if(listaReservas.get(i).getData().equals(dataMarcacao) 
                    && listaReservas.get(i).getEscolhaRefeicao().equals(escolhaRefeicao)
                    && listaReservas.get(i).getNumeroDePessoas() == 8 
                    ){
                    
                    if(listaReservas.get(i).getEscolhaRefeicao().equals("A")){
                        contadorDeMesas8PessoasAlmoco++;
                    }else{
                        contadorDeMesas8PessoasJantar++;
                    }
                    
                }

                if(listaReservas.get(i).getData().equals(dataMarcacao) 
                    && listaReservas.get(i).getEscolhaRefeicao().equals(escolhaRefeicao)
                    && listaReservas.get(i).getNumeroDePessoas() == 12 
                    ){
                    
                    if(listaReservas.get(i).getEscolhaRefeicao().equals("A")){
                        contadorDeMesas12PessoasAlmoco++;
                    }else{
                        contadorDeMesas12PessoasJantar++;
                    }
                    
                }

            }


            if(contadorDeMesas2PessoasAlmoco > 10 || contadorDeMesas2PessoasJantar > 10
                || contadorDeMesas4PessoasJantar > 15  || contadorDeMesas4PessoasJantar > 15
                || contadorDeMesas8PessoasJantar > 20  || contadorDeMesas8PessoasJantar > 20
                || contadorDeMesas12PessoasJantar > 25 || contadorDeMesas12PessoasJantar > 25){

                erroMesas = true;
                mesaCodeErro();

            }else{

                if(numeroDePessoas==2){
                    if(escolhaRefeicao.equals("A")){
                        Reservas dadosRecebidosAlmoco = new Reservas(contadorDeMesas2PessoasAlmoco+1,dataMarcacao, escolhaRefeicao, numeroDePessoas,nomeDaReserva);
                        listaReservas.add(dadosRecebidosAlmoco);
                    }else if(escolhaRefeicao.equals("J")){
                        Reservas dadosRecebidosJantar = new Reservas(contadorDeMesas2PessoasJantar+1,dataMarcacao, escolhaRefeicao, numeroDePessoas,nomeDaReserva);
                        listaReservas.add(dadosRecebidosJantar);
                    }
                }

                if(numeroDePessoas==4){
                    if(escolhaRefeicao.equals("A")){
                        Reservas dadosRecebidosAlmoco = new Reservas(contadorDeMesas4PessoasAlmoco+1,dataMarcacao, escolhaRefeicao, numeroDePessoas,nomeDaReserva);
                        listaReservas.add(dadosRecebidosAlmoco);
                    }else if(escolhaRefeicao.equals("J")){
                        Reservas dadosRecebidosJantar = new Reservas(contadorDeMesas4PessoasJantar+1,dataMarcacao, escolhaRefeicao, numeroDePessoas,nomeDaReserva);
                        listaReservas.add(dadosRecebidosJantar);
                    }
                }

                if(numeroDePessoas==8){
                    if(escolhaRefeicao.equals("A")){
                        Reservas dadosRecebidosAlmoco = new Reservas(contadorDeMesas8PessoasAlmoco+1,dataMarcacao, escolhaRefeicao, numeroDePessoas,nomeDaReserva);
                        listaReservas.add(dadosRecebidosAlmoco);
                    }else if(escolhaRefeicao.equals("J")){
                        Reservas dadosRecebidosJantar = new Reservas(contadorDeMesas8PessoasJantar+1,dataMarcacao, escolhaRefeicao, numeroDePessoas,nomeDaReserva);
                        listaReservas.add(dadosRecebidosJantar);
                    }
                }

                if(numeroDePessoas==12){
                    if(escolhaRefeicao.equals("A")){
                        Reservas dadosRecebidosAlmoco = new Reservas(contadorDeMesas12PessoasAlmoco+1,dataMarcacao, escolhaRefeicao, numeroDePessoas,nomeDaReserva);
                        listaReservas.add(dadosRecebidosAlmoco);
                    }else if(escolhaRefeicao.equals("J")){
                        Reservas dadosRecebidosJantar = new Reservas(contadorDeMesas12PessoasJantar+1,dataMarcacao, escolhaRefeicao, numeroDePessoas,nomeDaReserva);
                        listaReservas.add(dadosRecebidosJantar);
                    }
                }
            }
        }
        
        try {  
            
            //VAI IMPRIMIR NO SERVIDOR O REGISTO DE RESERVAS
            System.out.println("\n|DEBUG LISTA RESERVAS|");
            for (int h = 0; h < listaReservas.size(); h++) {
                System.out.println(listaReservas.get(h));
            }

            guardarDadosNoTXT();

        } catch (Exception e) {
            throw new Exception("ERRO AO GRAVAR FICHEIRO"); 
        }

    }

    public void guardarDadosProvenientesTXT() throws RemoteException,Exception {


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
            
                listaReservas.add(reserva);

            }

            reader.close();
            fr.close();
         

        } catch (Exception e) {
            System.out.println(e.toString());
            throw new Exception("ERRO AO TRANSFERIR DADOS DA BASE DE DADOS"); 
        }
    }
 
    public boolean mesaCodeErro() throws RemoteException {
        return erroMesas;
    }

        
    public void cancelarMesa(String DataInserida, String jantarOUalmocoInserido, String nomeDaReserva) 
        throws RemoteException {

        for (int i = 0; i < listaReservas.size(); i++) {
            if(listaReservas.get(i).getData().equals(DataInserida)
                && listaReservas.get(i).getEscolhaRefeicao().equals(jantarOUalmocoInserido)
                && listaReservas.get(i).getNomeDaReserva().equals(nomeDaReserva)){

                listaReservas.remove(i);
                guardarDadosNoTXT();        
            }
        }
    }

}
