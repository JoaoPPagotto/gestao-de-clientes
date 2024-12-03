package main.java.utils;

import main.java.models.Cliente;

import java.io.*;
import java.util.*;

public class OrdenarCliente {

    private static final int CHUNK_SIZE = 100000; // Tamanho de cada parte (ajuste conforme a memória disponível)

    public void sort(String inputFile, String outputFile) throws IOException, ClassNotFoundException {
        List<String> tempFiles = createSortedChunks(inputFile);
        mergeSortedChunks(tempFiles, outputFile);
    }

    private List<String> createSortedChunks(String inputFile) throws IOException, ClassNotFoundException {
        List<String> tempFiles = new ArrayList<>();

        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(inputFile)));

        boolean eof = false;
        int chunkIndex = 0;

        while (!eof) {
            List<Cliente> chunk = new ArrayList<>(CHUNK_SIZE);
            try {
                for (int i = 0; i < CHUNK_SIZE; i++) {
                    Cliente cliente = (Cliente) ois.readObject();
                    chunk.add(cliente);
                }
            } catch (EOFException e) {
                eof = true;
            }

            if (!chunk.isEmpty()) {
                // Ordena a parte
                Collections.sort(chunk);

                // Escreve a parte ordenada em um arquivo temporário
                String tempFileName = "tempfile_" + chunkIndex + ".dat";
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFileName)));
                for (Cliente cliente : chunk) {
                    oos.writeObject(cliente);
                }
                oos.close();

                tempFiles.add(tempFileName);
                chunkIndex++;
            }
        }

        ois.close();
        return tempFiles;
    }

    private void mergeSortedChunks(List<String> tempFiles, String outputFile) throws IOException, ClassNotFoundException {
        PriorityQueue<ClienteEntry> pq = new PriorityQueue<>();

        List<ObjectInputStream> inputStreams = new ArrayList<>();
        try {
            // Abre os streams de entrada para cada arquivo temporário
            for (int i = 0; i < tempFiles.size(); i++) {
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(tempFiles.get(i))));
                inputStreams.add(ois);
                try {
                    Cliente cliente = (Cliente) ois.readObject();
                    pq.add(new ClienteEntry(cliente, i));
                } catch (EOFException e) {
                    // Arquivo vazio
                }
            }

            // Abre o stream de saída para o arquivo ordenado final
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));

            // Mescla os objetos Cliente
            while (!pq.isEmpty()) {
                ClienteEntry entry = pq.poll();
                Cliente cliente = entry.cliente;
                int index = entry.fileIndex;

                // Escreve o cliente no arquivo de saída
                oos.writeObject(cliente);

                // Lê o próximo cliente do mesmo arquivo
                try {
                    Cliente nextCliente = (Cliente) inputStreams.get(index).readObject();
                    pq.add(new ClienteEntry(nextCliente, index));
                } catch (EOFException e) {
                    // Fim do arquivo
                }
            }

            oos.close();
        } finally {
            // Fecha todos os streams de entrada
            for (ObjectInputStream ois : inputStreams) {
                ois.close();
            }

            // Deleta os arquivos temporários
            for (String tempFileName : tempFiles) {
                File file = new File(tempFileName);
                file.delete();
            }
        }
    }

    private static class ClienteEntry implements Comparable<ClienteEntry> {
        Cliente cliente;
        int fileIndex;

        public ClienteEntry(Cliente cliente, int fileIndex) {
            this.cliente = cliente;
            this.fileIndex = fileIndex;
        }

        @Override
        public int compareTo(ClienteEntry other) {
            return this.cliente.compareTo(other.cliente);
        }
    }

    public static void main(String[] args){
        // Nome do arquivo gerado anteriormente pelo GeradorDeArquivosDeClientes
        String inputFile = "clientes";
        String outputFile = String.format("%s_ordenados", inputFile);

        OrdenarCliente sorter = new OrdenarCliente();
        try {
            sorter.sort(inputFile, outputFile);
            System.out.println("Ordenação concluída com sucesso!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
