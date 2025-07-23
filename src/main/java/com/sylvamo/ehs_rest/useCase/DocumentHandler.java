package com.sylvamo.ehs_rest.useCase;

import com.sylvamo.ehs_rest.Model.Document;
import com.sylvamo.ehs_rest.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

@Component
public class DocumentHandler {

    @Autowired
    private DocumentRepository documentRepository;
    private boolean chave = false;

    private static final Path pastaOrigem = Paths.get("E:/fusion/files/000");
    private static final Path pastaDestino = Paths.get("E:/teste/NewArchive");

    private static Path buscarArquivoNeo(Path pasta, String nomeArquivo) throws IOException {
        try (Stream<Path> stream = Files.walk(pasta)) {
            return stream
                    .filter(p -> Files.isRegularFile(p) && p.getFileName().toString().equalsIgnoreCase(nomeArquivo))
                    .findFirst()
                    .orElse(null);
        }
    }

    private static String limparNomeArquivo(String nome) {
        return nome.replaceAll("[\\\\/:*?\"<>|]", " ").trim();
    }


    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void processarNovosDocumentos() {
        List<Document> novosDocs = documentRepository.getAllRegisteredDocuments();

        for (Document doc : novosDocs) {
            String nome = doc.getNome();
            String nomeDocumento = doc.getNomeDocumento();
            String neoid = doc.getNeoid();

            if (nome == null || nomeDocumento == null || neoid == null) {
                System.out.printf("❗ Dados incompletos: %s%n", doc);
                continue;
            }

            String extensao = nome.contains(".") ? nome.substring(nome.lastIndexOf(".")) : ".neo";
            String nomeLimpo = limparNomeArquivo(nomeDocumento);
            String novoNome = nomeLimpo + extensao;

            try {
                Path arquivoOrigem = buscarArquivoNeo(pastaOrigem, neoid + ".neo");

                if (arquivoOrigem != null) {
                    Path destinoFinal = pastaDestino.resolve(novoNome);
                    Files.copy(arquivoOrigem, destinoFinal, StandardCopyOption.REPLACE_EXISTING);
                    System.out.printf("Copiado: %s para %s%n", arquivoOrigem, destinoFinal);
                    chave = true;
                    //documentRepository.saveProcessedDocument(nomeDocumento, neoid, name);


                } else {
                    System.out.printf("Arquivo não encontrado para NEOID: %s%n", neoid);
                }

            } catch (IOException e) {
                System.err.printf("Erro ao copiar arquivo para NEOID %s: %s%n", neoid, e.getMessage());
            }




        }
        if (chave) {
            try {

                String scriptPath = "E:\\teste\\uploadArquivosNeomind.ps1";
                String pwshPath = "C:\\Program Files\\PowerShell\\7\\pwsh.exe";

                ProcessBuilder pb = new ProcessBuilder(
                        pwshPath,
                        "-ExecutionPolicy", "Bypass",
                        "-File", scriptPath
                );
                pb.inheritIO();
                Process process = pb.start();
                int exitCode = process.waitFor();

                System.out.println("Script PowerShell finalizado com código: " + exitCode);

                if (exitCode == 0 ) {

                    try(Stream<Path> arquivos = Files.list(pastaDestino)){

                        arquivos.forEach(arquivo -> {

                            try {
                                Files.deleteIfExists(arquivo);
                                System.out.printf("Arquivo removido: %s%n", arquivo);

                            } catch (IOException e) {
                                throw new RuntimeException(e);

                            }

                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }


                chave = false;

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }


        }

    }
}
