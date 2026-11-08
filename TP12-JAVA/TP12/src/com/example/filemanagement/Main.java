package com.example.filemanagement;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== TP 12 : Gestion des fichiers en Java ===\n");

        writeFileWithBufferedWriter("data/output.txt", "Bonjour, ceci est un test avec BufferedWriter.");

        writeWithPrintWriter("data/report.txt");

        writeWithFilesApi("data/nio_output.txt", "Contenu écrit via l'API Files moderne.");
        System.out.println("\n--- Lecture avec l'API Files ---");
        readWithFilesApi("data/nio_output.txt");

        System.out.println();
        copyFile("data/nio_output.txt", "data/employees_backup.txt");
        moveFile("data/nio_output.txt", "data/moved_output.txt");

        System.out.println();
        deleteFile("data/moved_output.txt");

        System.out.println();
        getFileMetadata("data/report.txt");

        System.out.println("\n--- Tests Fichiers Binaires ---");
        byte[] testData = "Données binaires de test pour le flux d'octets.".getBytes();
        writeBinaryFile("data/copy_binary.bin", testData);
        readBinaryFile("data/copy_binary.bin");
    }

    // Exercice : BufferedWriter ---
    public static void writeFileWithBufferedWriter(String filePath, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(content);
            bw.newLine();
            bw.write("Ligne ajoutée avec succès.");
            System.out.println("Fichier écrit (BufferedWriter) : " + filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
    }

    // Exercice : PrintWriter ---
    public static void writeWithPrintWriter(String filePath) {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(filePath))) {
            pw.println("=== Rapport des employés ===");
            pw.printf("Nom: %s, Âge: %d, Poste: %s%n", "Alice", 25, "Engineer");
            pw.printf("Nom: %s, Âge: %d, Poste: %s%n", "Bob", 30, "Manager");
            pw.println("Fin du rapport.");
            System.out.println("Fichier écrit (PrintWriter) : " + filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture : " + e.getMessage());
        }
    }

    // Exercice : Lecture Files API ---
    public static void readWithFilesApi(String filePath) {
        try {
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture : " + e.getMessage());
        }
    }

    // Exercice : Écriture Files API ---
    public static void writeWithFilesApi(String filePath, String content) {
        try {
            Path path = Paths.get(filePath);
            Files.write(path, content.getBytes());
            System.out.println("Fichier écrit avec succès (Files API) : " + filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture : " + e.getMessage());
        }
    }

    // Exercice : Copie ---
    public static void copyFile(String source, String destination) {
        try {
            Path sourcePath = Paths.get(source);
            Path destPath = Paths.get(destination);
            Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Fichier copié de " + source + " vers " + destination);
        } catch (IOException e) {
            System.err.println("Erreur lors de la copie : " + e.getMessage());
        }
    }

    // Exercice : Déplacement ---
    public static void moveFile(String source, String destination) {
        try {
            Path sourcePath = Paths.get(source);
            Path destPath = Paths.get(destination);
            Files.move(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Fichier déplacé de " + source + " vers " + destination);
        } catch (IOException e) {
            System.err.println("Erreur lors du déplacement : " + e.getMessage());
        }
    }

    // Exercice : Suppression ---
    public static void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
                System.out.println("Fichier supprimé : " + filePath);
            } else {
                System.out.println("Le fichier n'existe pas : " + filePath);
            }
        } catch (NoSuchFileException e) {
            System.err.println("Le fichier a été supprimé entre-temps : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // Exercice : Lecture Binaire ---
    public static void readBinaryFile(String filePath) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytes = 0;
            while ((bytesRead = bis.read(buffer)) != -1) {
                totalBytes += bytesRead;
            }
            System.out.println("Fichier binaire lu : " + filePath);
            System.out.println("Taille totale : " + totalBytes + " octets");
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture binaire : " + e.getMessage());
        }
    }

    // Exercice : Écriture Binaire ---
    public static void writeBinaryFile(String filePath, byte[] data) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            bos.write(data);
            bos.flush();
            System.out.println("Fichier binaire écrit : " + filePath);
            System.out.println("Taille écrite : " + data.length + " octets");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture binaire : " + e.getMessage());
        }
    }

    // Exercice : Métadonnées ---
    public static void getFileMetadata(String filePath) {
        try {
            Path path = Paths.get(filePath);
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            System.out.println("=== Métadonnées du fichier : " + filePath + " ===");
            System.out.println("Taille : " + attrs.size() + " octets");
            System.out.println("Dernière modification : " + attrs.lastModifiedTime().toInstant());
            System.out.println("Est un répertoire : " + attrs.isDirectory());
            System.out.println("Est un fichier : " + attrs.isRegularFile());
        } catch (IOException e) {
            System.err.println("Erreur lors de la récupération des métadonnées : " + e.getMessage());
        }
    }
}
