package ex2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        File file = new File("comenzi.dat");

        // scrierea comenzilor
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            java.util.stream.IntStream.rangeClosed(1, 15)
                    .mapToObj(i -> new Comanda(i, "Client" + (i % 5), 1000 * i, false))
                    .forEach(c -> {
                        try {
                            out.writeObject(c);
                        }
                        catch (IOException e) {
                            System.err.println("Eroare la scriere: " + e.getMessage());
                        }
                    });
        }
        catch (IOException e) {
            System.err.println("Eroare la deschidere: " + e.getMessage());
        }


        // actualizarea comenzilor cu valoare > 5000
        List<Comanda> actualizate = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            while (true) {
                long pozitie = raf.getFilePointer();
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    actualizate.clear();
                    while (true) {
                        Comanda c = (Comanda) ois.readObject();
                        if (c.getValoare() > 5000) {
                            c.setFinalizata(true);
                        }
                        actualizate.add(c);
                    }
                }
                catch (EOFException ignored) {
                    break;
                }
                catch (Exception e) {
                    System.err.println("Eroare la citire pt actualizare: " + e.getMessage());
                    break;
                }
            }
        }
        catch (IOException | SecurityException e) {
            System.err.println("Eroare la RandomAccessFile: " + e.getMessage());
        }

        // recitirea comenzilor
        List<Comanda> comenzi = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    Comanda c = (Comanda) in.readObject();
                    comenzi.add(c);
                }
                catch (EOFException e) {
                    break;
                }
                catch (ClassNotFoundException e) {
                    System.err.println("Clasa necunoscuta: " + e.getMessage());
                }
            }
        }
        catch (IOException e) {
            System.err.println("Eroare la recitire: " + e.getMessage());
        }

        List<Comanda> finalizate = comenzi.stream().filter(Comanda::isFinalizata).toList();

        double totalFinalizate = finalizate.stream().mapToDouble(Comanda::getValoare).sum();

        System.out.println("Comenzi finalizate:");
        finalizate.forEach(System.out::println);

        System.out.println("\nTotal valoare comenzi finalizate: " + totalFinalizate);

        System.out.println("\nComenzile fiecarui client:");
        finalizate.stream().collect(Collectors.groupingBy(Comanda::getNumeClient,Collectors.toList()))
                .forEach((client, lista) -> {
                    System.out.println(client + ":");
                    lista.forEach(System.out::println);
                });
    }
}
