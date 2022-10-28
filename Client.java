import java.rmi.*;
import java.util.Scanner;
public class Client {

    public static void menu(){
        System.out.println("=================================================");
        System.out.println("Seja muito bem vindo ao restaurante Gourmet!");
        System.out.println("O que deseja ?");
        System.out.println("1 - Reservar mesa");
        System.out.println("2 - Cancelar mesa");
        System.out.println("3 - Listar mesas");
        System.out.println("4 - Registar Utilizador");
        System.out.println("QualquerTecla - Sair");
        System.out.println("=================================================");
    }

    public static void main(String[] args) {
        
        try {
            String addServerURL = "rmi://" + args[0] + "/AddServer";
            ServerIntf addServerIntf = (ServerIntf)Naming.lookup(addServerURL);
            int numeroInput;
            Scanner input;
            
            do {

                menu();
         
                input = new Scanner(System.in);
                numeroInput = input.nextInt();

                if(numeroInput == 1) {
                    System.out.println("Proceda à reserva de mesa...\n");

                    System.out.println("Introduza a data na qual deseja marcar mesa, do seguinte modo: DD/MM/YY\n");
                    input = new Scanner(System.in);
                    String data = input.nextLine();
                    //do{
                    System.out.println("Vai desejar marcar mesa para jantar ou almoço? Responda de forma: A -> Almoço / J -> Jantar");
                    input = new Scanner(System.in);
                    String escolhaMarcacao = input.nextLine();
                    if (escolhaMarcacao.equals("A")) {
                        System.out.println("A sua reserva para almoçar foi marcada com sucesso!");
                    } else if (escolhaMarcacao.equals("J")) {
                        System.out.println("A sua reserva para jantar foi marcada com sucesso!");
                    } else {
                        System.out.println("Por favor selecione uma das opções corretas para reservar mesa.");
                    }
               // }while(!(escolhaMarcacao.equals("A")) || !(escolhaMarcacao.equals("J")))



    
                    addServerIntf.saveDados(data,escolhaMarcacao);
                    
                    
                } else if (numeroInput == 2) {
                    System.out.println();
                } else if (numeroInput == 3) {
                    System.out.println();
                } else if (numeroInput == 4) {
                    System.out.println();
                } else{
                    System.exit(0);
                }
                
            } while (true);


        }catch(Exception e) {
            System.out.println("Obrigado, volte sempre!");
        }
    }
}