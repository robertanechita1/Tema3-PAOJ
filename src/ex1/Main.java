package ex1;

import java.io.*;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        File fisierProduse = new File("produse.dat");
        File fisierErori = new File("erori.log");
        File fisierEpuizate = new File("epuizate.txt");

        // scrierea produselor in fisier
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fisierProduse))) {
            IntStream.rangeClosed(1, 10).forEach(i -> {
                try {
                    Produs produs = new Produs("Produs" + i, Math.random() * 100, 200);
                    out.writeObject(produs);
                }
                catch (InvalidDataException e) {
                    try (PrintWriter pw = new PrintWriter(new FileWriter(fisierErori, true))) {
                        pw.println("Eroare la produs " + i + ": " + e.getMessage());
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // citirea produselor
        List<Produs> produse = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fisierProduse))) {
            while (true) {
                try {
                    Produs p = (Produs) in.readObject();
                    produse.add(p);
                }
                catch (EOFException e) {
                    break;
                }
                catch (ClassNotFoundException e) {
                    System.out.println("Clasa necunoscuta: " + e.getMessage());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // stream pt procesare
        Stream<Produs> streamProduse = produse.stream();

        // salvez produsele cu stoc 0
        List<Produs> epuizate = streamProduse.filter(p -> p.getStoc() == 0).toList();

        try (PrintWriter pw = new PrintWriter(new FileWriter(fisierEpuizate))) {
            epuizate.forEach(pw::println);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // reduc stocurile cu 10%
        UnaryOperator<Produs> reducereStoc = p -> {
            p.setStoc((int)(p.getStoc() * 0.9));
            return p;
        };

        produse = produse.stream().map(reducereStoc).collect(Collectors.toList());

        // produsul cu cel mai mare pret
        produse.stream()
                .max(Comparator.comparingDouble(Produs::getPret))
                .ifPresent(p -> System.out.println("Produsul cu cel mai mare pret: " + p));
    }
}
