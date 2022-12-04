import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.rmi.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.ArrayList;

public class Client {


    public static ArrayList<utilizadores> ListUtilizadores = new ArrayList<>();
    

    public static void main(String args[]) {
        
        try {

            String ServerURL = "rmi://" + args[0] + "/Server";
            ServerIntf ServerIntf = (ServerIntf)Naming.lookup(ServerURL);
           
            guardarDadosProvenientesDoTxtUtilizadores();

            boolean logado = false;

            if(logado == false){
                int numeroInputLogin = 0;
                Scanner inputLogin;

                do {
                    
                    menuLogin();

                    inputLogin = new Scanner(System.in);
                    numeroInputLogin = inputLogin.nextInt();

                    

                    if(numeroInputLogin == 1){
                    
                        String email1 = "";
                        String password1 = "";

                        do {

                            System.out.println("\nIntroduza o seu email:");
                            inputLogin = new Scanner(System.in);
                            email1 = inputLogin.nextLine();
        
                            System.out.println("\nIntroduza a sua password:");
                            inputLogin = new Scanner(System.in);
                            password1 = inputLogin.nextLine();

                        } while (jaExisteUtilizador(email1));

                        String passwordDecryptada = MD5Encryption(password1);
                        
                        utilizadores user = new utilizadores(email1, passwordDecryptada);
                        ListUtilizadores.add(user);
    
                        guardarUserNoTXT();
                        


                    }else if(numeroInputLogin == 2){
                    
                        String email2 = "";
                        String password2 = "";
                       
                        do {
                            
                            System.out.println("\nIntroduza o seu email:");
                            inputLogin = new Scanner(System.in);
                            email2 = inputLogin.nextLine();

                            System.out.println("\nIntroduza a sua password:");
                            inputLogin = new Scanner(System.in);
                            password2 = inputLogin.nextLine();

                        } while (!loginValido(email2, password2));
                        
                        logado = true;
                        break;
                    }
                    
                } while ((numeroInputLogin!=1) || (numeroInputLogin!=2));
            }

            if(logado){

                int numeroInput = 0;
                Scanner input;
                
                do {
    
                    menu();
             
                    input = new Scanner(System.in);
                    numeroInput = input.nextInt();
    
                    if(numeroInput == 1){
    
                        System.out.println("Proceda à reserva de mesa...\n");
                        
    
                        input = new Scanner(System.in);
                        String data = "";
                        do {
                            System.out.println("\nIntroduza a data na qual deseja marcar mesa, do seguinte modo: DD/MM/YY\n");
                            input = new Scanner(System.in);
                            data = input.nextLine();
                        } while (!validaData(data));
                       
    
    
                        input = new Scanner(System.in);
                        String escolhaMarcacao = "";
                        do {
                            System.out.println("\nVai desejar marcar mesa para jantar ou almoço? Responda de forma: A -> Almoço / J -> Jantar");
                            input = new Scanner(System.in);
                            escolhaMarcacao = input.nextLine().toUpperCase();
                        } while (!validaRefeicao(escolhaMarcacao));
    
                        
                        input = new Scanner(System.in);
                        int numeroDePessoas = 0; 
                        do {
                            System.out.println("\nPor favor, indique-nos para quantas pessoas será a mesa (2,4,8,12 Pessoas)");
                            input = new Scanner(System.in);
                            numeroDePessoas = input.nextInt();
                        } while (!validaNumeroPessoas(numeroDePessoas));
                      
    
    
                        input = new Scanner(System.in);
                        String nomeDaReserva = "";
                        do {
                            System.out.println("\nPor favor, indique-nos o nome da reserva");
                            input = new Scanner(System.in);
                            nomeDaReserva = input.nextLine();
                        } while (!validaNomePessoas(nomeDaReserva));
    
                        ServerIntf.saveDados(data,escolhaMarcacao,numeroDePessoas,nomeDaReserva);
                
                        if(ServerIntf.mesaCodeErro()){
                            System.out.println("\nSem mesas disponiveis para pessoas na categoria escolhida por si!\nPedimos desculpa, tente inserir outra data válida\n");
                            continue;
                        }else{
                            System.out.println("\nA sua reserva foi marcada com sucesso!\n");
                        }                   
                    }else if(numeroInput == 2){
    
                        System.out.println("Proceda ao cancelamento da reserva...\n");
                        
    
                        input = new Scanner(System.in);
                        String data = "";
                        do {
                            System.out.println("\nIntroduza a data na qual deseja desmarcar a reserva, do seguinte modo: DD/MM/YY\n");
                            input = new Scanner(System.in);
                            data = input.nextLine();
                        } while (!validaData(data));
                       
    
    
                        input = new Scanner(System.in);
                        String escolhaMarcacao = "";
                        do {
                            System.out.println("\nIndique qual a refeitção para a qual a reserva está marcada, Responda de forma: A -> Almoço / J -> Jantar");
                            input = new Scanner(System.in);
                            escolhaMarcacao = input.nextLine().toUpperCase();
                        } while (!validaRefeicao(escolhaMarcacao));
    
    
                        input = new Scanner(System.in);
                        String nomeDaReserva = "";
                        do {
                            System.out.println("\nPor favor, indique-nos qual nome da pessoa que marcou a reserva");
                            input = new Scanner(System.in);
                            nomeDaReserva = input.nextLine();
                        } while (!validaNomePessoas(nomeDaReserva));                    
                        
                        //CORRE O CANCELAMENTO DA RESERVA E INDICA AO UTILIZADOR SE FOI BEM SUCEDIDA OU NAO
                        if( ServerIntf.cancelarMesa(data, escolhaMarcacao,nomeDaReserva) ){
                            System.out.println("Cancelamento de reserva COM SUCESSO");
                        }else{
                            System.out.println("Cancelamento de reserva SEM SUCESSO\n");
                            System.out.println("Marcação inexistente OU Dados inseridos incorretos");
                        }
                        
                    }else if(numeroInput == 3){
                        
                        System.out.println("Proceda à visualização de mesas disponiveis...\n");
                        
    
                        input = new Scanner(System.in);
                        String data = "";
                        do {
                            System.out.println("\nIntroduza a data na qual deseja desmarcar a reserva, do seguinte modo: DD/MM/YY\n");
                            input = new Scanner(System.in);
                            data = input.nextLine();
                        } while (!validaData(data));
    
                        //PREPARA MENSAGEM DE OUTPUT
                        ServerIntf.listarMesasLivre(data);
                        
                        //ESCRITA DA MENSAGEM
                        System.out.println("\n" + ServerIntf.printMesasLivresAlmoco() + "\n" ); 
                        System.out.println("\n" + ServerIntf.printMesasLivresJantar() + "\n" ); 
    
                    }
                    
                } while ( (numeroInput!=1) || (numeroInput!=2) || (numeroInput!=3) );
    
                input.close();
            }

           
        }catch(Exception e) {
            System.out.println("Obrigado, volte sempre!");
        }
    }


    public static boolean validaData(String dataInput) throws RemoteException {

        try {

            String[] dados = dataInput.split("/");

            int dia  = Integer.parseInt(dados[0]);
            int mes  = Integer.parseInt(dados[1]);
            int ano  = Integer.parseInt(dados[2]);
           
            if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12){
                if(dia < 0 || dia > 31){
                    System.out.println("Data Inválida");
                    return false;
                }
            }

            if (mes == 4 || mes == 6 || mes == 9 || mes == 11){
                if(dia < 0 || dia > 30){
                    System.out.println("Data Inválida, o mês introduzido:" + mes + "tem entre 1 a 30 dias");
                    return false;
                }
            }

            if( (ano%100 != 0 && ano%4 == 0) || (ano%400 == 0) ){
                //Ano Bissexto
                if(mes == 2){
                    if(dia < 0 || dia > 29){
                        System.out.println("Data Inválida, o mês introduzido: 2 " + "tem entre 1 a 29 dias");
                        return false;
                    }   
                }
            }else{
                //Ano não Bissexto
                if(mes == 2){
                    if(dia < 0 || dia > 28){
                        System.out.println("Data Inválida, o mês introduzido: 2"+ "tem entre 1 a 28 dias");
                        return false;
                    }   
                }
            }

            return true;

        } catch (Exception e) {

            System.out.println("Data Inválida");
            return false;

        }
    }

    public static boolean validaRefeicao(String refeicaoInput) throws RemoteException {
       
        if ( (refeicaoInput.equals("A")) || (refeicaoInput.equals("J")) ){

            return true;

        }else{

            System.out.println("Escolha uma opção válida!");
            return false;

        }

        
    }

    public static boolean validaNumeroPessoas(int NumeroInput) throws RemoteException {
      
        if ( (NumeroInput == 2) || (NumeroInput == 4) || (NumeroInput == 8) || (NumeroInput == 12) ){

            return true;

        }else{

            System.out.println("Escolha uma opção válida!");
            return false;

        }

    }

    public static boolean validaNomePessoas(String nomeInput) throws RemoteException {
       
        Boolean var = nomeInput.matches("[a-zA-Z]+");


        if ( var ){

            return true;

        }else{

            System.out.println("Introduza um nome válido!");
            return false;

        }
    }


    public static void menu(){

        System.out.println("\n\n=================================================");
        System.out.println("Seja muito bem vindo ao restaurante Gourmet!");
        System.out.println("O que deseja ?");
        System.out.println("1 - Reservar mesa");
        System.out.println("2 - Cancelar mesa");
        System.out.println("3 - Listar mesas");
        System.out.println("QualquerTecla - Sair");
        System.out.println("=================================================\n\n");

    }

    public static void menuLogin(){

        System.out.println("\n\n=================================================");
        System.out.println("Seja muito bem vindo ao restaurante Gourmet!");
        System.out.println("Efetue login ou registo");
        System.out.println("1 - Registe-se");
        System.out.println("2 - Login");
        System.out.println("QualquerTecla - Sair");
        System.out.println("=================================================\n\n");

    }

    public static boolean jaExisteUtilizador(String emailInput){

        if(ListUtilizadores.size()==0){
            System.out.println("\n>>>Conta criada com sucesso!<<<");
            return false;
        }
       
        for (int i = 0; i < ListUtilizadores.size(); i++) {

            if(ListUtilizadores.get(i).getEmail().equals(emailInput)){
                System.out.println("\n>>>Registo inválido, já existe conta com este email<<<");
                return true;
            }
        }
        System.out.println("\n>>>Conta criada com sucesso!<<<");
        return false;
    }


    public static boolean loginValido(String emailInput, String passwordInput){

        if(ListUtilizadores.size()==0){
            System.out.println("\n >>>Login Incorreto, insira credenciais novamente!<<<");
            return false;
        }
       
        for (int i = 0; i < ListUtilizadores.size(); i++) {
            //String passwordDecryptada = EncryptDecrypt(passwordInput.toCharArray());
            String passwordDecryptada = MD5Encryption(passwordInput);
            
            if(ListUtilizadores.get(i).getEmail().equals(emailInput) 
                && ListUtilizadores.get(i).getPassword().equals(passwordDecryptada)){
                System.out.println("\n >>>Login Efetuado<<<");
                return true;
            }
        }
        System.out.println("\n >>>Login Incorreto, insira credenciais novamente!<<<");
        return false;
    }


    public static void guardarUserNoTXT(){
        try {

            File file = new File("Utilizadores.txt");
        
            FileWriter writer = new FileWriter(file);
            PrintWriter write = new PrintWriter(writer);

            

            
            for (int i = 0; i < ListUtilizadores.size(); i++) {
                write.println(ListUtilizadores.get(i).getEmail() + "/" + ListUtilizadores.get(i).getPassword());
            }
           
            write.close();   

        } catch (Exception e) {
        }
    }

    public static void guardarDadosProvenientesDoTxtUtilizadores() throws RemoteException,Exception {
        try {   
            
            FileReader fr = new FileReader("Utilizadores.txt");
            BufferedReader reader = new BufferedReader(fr);
    
            String linha = null;
            
            while ((linha = reader.readLine()) != null) {
    
                String[] dados = linha.split("/");


                String email = dados[0];
                String password  = dados[1];

                utilizadores user = new utilizadores(email, password);
            
                ListUtilizadores.add(user);

            }

            reader.close();
            fr.close();
         

        } catch (Exception e) {
        }
    }


    public static String MD5Encryption(String password){

        String encryptedpassword = null;
        try
        {

            MessageDigest m = MessageDigest.getInstance("MD5");

            m.update(password.getBytes());


            byte[] bytes = m.digest();


            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }


            encryptedpassword = s.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return encryptedpassword;
    }


}