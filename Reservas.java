public class Reservas {
    private int id;
    private String data;
    private String escolhaRefeicao;
    

    public Reservas(int id, String data, String escolhaRefeicao) {
        this.id = id;
        this.data = data;
        this.escolhaRefeicao = escolhaRefeicao;
    }

    //DEBUG
    @Override
    public String toString() {
        return "A Reserva com [id=" + id + ", data=" + data + ", escolhaRefeicao=" + escolhaRefeicao + "]";
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getEscolhaRefeicao() {
        return escolhaRefeicao;
    }

    
    
}
