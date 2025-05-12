package ex4;

public class Persoana {
    private String nume;
    private int varsta;
    private String oras;

    public Persoana(String nume, int varsta, String oras) {
        this.nume = nume;
        this.varsta = varsta;
        this.oras = oras;
    }

    public String getNume() {
        return nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public String getOras() {
        return oras;
    }

    @Override
    public String toString() {
        return nume + " cu varsta de " + varsta + " ani din " + oras;
    }
}
