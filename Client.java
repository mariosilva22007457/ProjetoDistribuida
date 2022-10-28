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
            int numeroInput = 0;
            Scanner input;
            
            do {

                menu();
         
                input = new Scanner(System.in);
                numeroInput = input.nextInt();

                if(numeroInput == 1){
                    System.out.println("Proceda à reserva de mesa...\n");
    
                    System.out.println("Introduza a data na qual deseja marcar mesa, do seguinte modo: DD/MM/YY\n");
                    input = new Scanner(System.in);
                    String data = input.nextLine();
                    
                    System.out.println("Vai desejar marcar mesa para jantar ou almoço? Responda de forma: A -> Almoço / J -> Jantar");
                    input = new Scanner(System.in);
                    String escolhaMarcacao = input.nextLine();
                    
                    
                    System.out.println("A sua reserva foi marcada com sucesso!");
    
                    addServerIntf.saveDados(data,escolhaMarcacao);
                    
                    
                }else{
                    System.exit(0);
                }
                
            } while (numeroInput!=1 || numeroInput!=2 || numeroInput!=3 || numeroInput!=4);


        }catch(Exception e) {
            System.out.println("Obrigado, volte sempre!");
        }
    }
}