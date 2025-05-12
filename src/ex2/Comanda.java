package ex2;

import java.io.Serializable;

public class Comanda implements Serializable {
    private int id;
    private String numeClient;
    private double valoare;
    private boolean finalizata;

    public Comanda(int id, String numeClient, double valoare, boolean finalizata) {
        this.id = id;
        this.numeClient = numeClient;
        this.valoare = valoare;
        this.finalizata = finalizata;
    }

    public int getId() {
        return id;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public double getValoare() {
        return valoare;
    }

    public boolean isFinalizata() {
        return finalizata;
    }

    public void setFinalizata(boolean finalizata) {
        this.finalizata = finalizata;
    }

    @Override
    public String toString() {
        return "Comanda cu id-ul " + id +
                ", client ='" + numeClient + '\'' +
                ", in valoare de " + valoare +
                ", finalizata = " + finalizata;
    }
}
