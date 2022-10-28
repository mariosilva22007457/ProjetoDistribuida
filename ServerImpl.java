import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.rmi.*;
import java.rmi.server.*;

import java.util.ArrayList;

public class ServerImpl extends UnicastRemoteObject implements ServerIntf {

    ArrayList<Reservas> listaReservas = new ArrayList<>();


    //Quando se liga o servidor
    public ServerImpl() throws Exception {
        

        lerDados();
        //PARA DEBUG
        for (int i = 0; i < listaReservas.size(); i++) {
            System.out.println(listaReservas.get(i));
        }

    }

    public void saveDados() throws Exception {
        //GUARDA DADOS NA BASE DE DADOS (FIcheiro TXT)
        try {   
            
            FileWriter writer = new FileWriter("BaseDeDados");
            PrintWriter write = new PrintWriter(writer);
            
            //FORMA COMO GUARDAR: ID - DIA DA RESERVA - ALMOÇO/JANTAR
            write.println();


            write.close();
         

        } catch (Exception e) {
            throw new Exception("ERRO AO GRAVAR FICHEIRO"); 
        }

    }

    public void lerDados() throws Exception {


        try {   
            
            FileReader fr = new FileReader("BaseDeDados");
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
         

        } catch (Exception e) {
            throw new Exception("ERRO AO TRANSFERIR DADOS DA BASE DE DADOS"); 
  
        }
        


    }
    



}
