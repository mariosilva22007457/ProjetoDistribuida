public class Reservas {
    private int id;
    private String data;
    private String escolhaRefeicao;
    private int numeroDePessoas;
    

    public Reservas(int id, String data, String escolhaRefeicao, int numeroDePessoas) {
        this.id = id;
        this.data = data;
        this.escolhaRefeicao = escolhaRefeicao;
        this.numeroDePessoas = numeroDePessoas;
    }

    //DEBUG
    @Override
    public String toString() {
        return "Reserva com [id=" + id + ", data=" + data + ", escolhaRefeicao=" + escolhaRefeicao + ", numeroDePessoas="
                + numeroDePessoas + "]";
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

    public int getNumeroDePessoas() {
        return numeroDePessoas; 
    }

    
    
}
