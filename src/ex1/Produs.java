package ex1;

import java.io.Serializable;

public class Produs implements Serializable {
    private String nume;
    private double pret;
    private int stoc;

    public Produs(String nume, double pret, int stoc) throws InvalidDataException {
        if (pret < 0 || stoc < 0) {
            throw new InvalidDataException("Pretul si stocul trebuie sa fie numere pozitive");
        }
        this.nume = nume;
        this.pret = pret;
        this.stoc = stoc;
    }

    public String getNume() {
        return nume;
    }

    public double getPret() {
        return pret;
    }

    public int getStoc() {
        return stoc;
    }

    public void setStoc(int stoc) {
        this.stoc = stoc;
    }

    @Override
    public String toString() {
        return nume + " : " + pret + " lei, " + stoc + " bucati.\n";
    }
}
