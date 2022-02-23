package com.company;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.*;
import static java.nio.file.StandardOpenOption.*;

public class Main {

    public static void main(String[] args) {
        Path tstDir = Paths.get("tst");
        Path tmpFile = Paths.get("tst/tmp.txt");
        Path mainFile = Paths.get("tst/main.txt");
        try {
            if (!Files.exists(tstDir))
                Files.createDirectory(tstDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        String line;
        int capacity = 0;
        try (FileChannel channel = FileChannel.open(tmpFile, CREATE, WRITE, DELETE_ON_CLOSE)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
            while (!scanner.hasNext("/")) {
                line = scanner.nextLine();
                //
                //Задается буфер реального размера исходя из длины введенных данных
                //
                if (line.length() > byteBuffer.capacity()) {
                    byteBuffer = ByteBuffer.allocate(line.length());
                }
                byteBuffer.put(line.getBytes(UTF_8));
                byteBuffer.flip();
                channel.write(byteBuffer);
                byteBuffer.clear();
            }
            byte[] bytes = Files.readAllBytes(tmpFile);
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            line = "<" + localDateTime.format(formatter) + ">\t";
            Files.write(mainFile, line.getBytes(), CREATE, APPEND);
            Files.write(mainFile, bytes, CREATE, APPEND);
            Files.write(mainFile, "\n".getBytes(), CREATE, APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
