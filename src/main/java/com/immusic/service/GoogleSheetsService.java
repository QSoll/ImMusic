package com.immusic.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.musicosapp.model.Artista;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço para integração com Google Sheets API
 * Gerencia cadastro e consulta de artistas na planilha
 */
@Service
public class GoogleSheetsService {

    private static final String APPLICATION_NAME = "I'MUSIC Platform";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    
    // Escopo de permissões necessárias
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    @Value("${google.sheets.spreadsheet.id:}")
    private String spreadsheetId;
    
    @Value("${google.sheets.range:Artistas!A:H}")
    private String range;

    private Sheets sheetsService;
    private boolean serviceInitialized = false;

    /**
     * Inicializa o serviço após a construção do bean
     */
    @PostConstruct
    public void init() {
        try {
            initializeSheetsService();
            createHeaderIfNeeded();
            serviceInitialized = true;
            System.out.println("✅ Google Sheets Service inicializado com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar Google Sheets Service: " + e.getMessage());
            System.err.println("⚠️  O sistema funcionará em modo offline.");
        }
    }

    /**
     * Cria as credenciais de autorização
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Carrega o arquivo de credenciais
        InputStream in = GoogleSheetsService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Arquivo de credenciais não encontrado: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Constrói o fluxo de autorização
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Inicializa o serviço do Google Sheets
     */
    private void initializeSheetsService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Cria o cabeçalho da planilha se necessário
     */
    private void createHeaderIfNeeded() {
        if (!serviceInitialized && sheetsService != null && !spreadsheetId.isEmpty()) {
            try {
                // Verifica se já existe dados na planilha
                ValueRange response = sheetsService.spreadsheets().values()
                        .get(spreadsheetId, "Artistas!A1:H1")
                        .execute();
                
                List<List<Object>> values = response.getValues();
                if (values == null || values.isEmpty()) {
                    // Cria o cabeçalho
                    List<List<Object>> headerValues = Arrays.asList(
                        Arrays.asList("ID", "Nome", "Estilo", "Especialidade", "Link", "Biografia", "Foto", "Data Cadastro")
                    );
                    
                    ValueRange headerRange = new ValueRange().setValues(headerValues);
                    sheetsService.spreadsheets().values()
                            .update(spreadsheetId, "Artistas!A1:H1", headerRange)
                            .setValueInputOption("RAW")
                            .execute();
                    
                    System.out.println("📊 Cabeçalho criado na planilha Google Sheets");
                }
            } catch (Exception e) {
                System.err.println("⚠️  Erro ao criar cabeçalho: " + e.getMessage());
            }
        }
    }

    /**
     * Salva um artista na planilha do Google Sheets
     */
    public boolean salvarArtista(Artista artista) {
        if (!serviceInitialized || artista == null || !artista.isValid()) {
            System.err.println("⚠️  Serviço não inicializado ou artista inválido");
            return false;
        }

        try {
            List<List<Object>> values = Arrays.asList(
                Arrays.asList((Object[]) artista.toStringArray())
            );

            ValueRange body = new ValueRange().setValues(values);
            AppendValuesResponse result = sheetsService.spreadsheets().values()
                    .append(spreadsheetId, range, body)
                    .setValueInputOption("RAW")
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();

            System.out.println("✅ Artista salvo na planilha: " + artista.getNome());
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar artista: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca todos os artistas da planilha
     */
    public List<Artista> buscarTodosArtistas() {
        if (!serviceInitialized) {
            return getArtistasDefault();
        }

        try {
            ValueRange response = sheetsService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            
            List<List<Object>> values = response.getValues();
            if (values == null || values.size() <= 1) { // <= 1 porque a primeira linha é o cabeçalho
                return getArtistasDefault();
            }

            List<Artista> artistas = new ArrayList<>();
            // Pula a primeira linha (cabeçalho)
            for (int i = 1; i < values.size(); i++) {
                List<Object> row = values.get(i);
                String[] data = row.stream()
                        .map(Object::toString)
                        .toArray(String[]::new);
                
                Artista artista = Artista.fromStringArray(data);
                if (artista != null && artista.isValid()) {
                    artistas.add(artista);
                }
            }

            System.out.println("📊 Carregados " + artistas.size() + " artistas da planilha");
            return artistas;

        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar artistas: " + e.getMessage());
            return getArtistasDefault();
        }
    }

    /**
     * Busca artistas por nome
     */
    public List<Artista> buscarPorNome(String nome) {
        List<Artista> todosArtistas = buscarTodosArtistas();
        return todosArtistas.stream()
                .filter(artista -> artista.getNome().toLowerCase()
                        .contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Busca artistas por estilo musical
     */
    public List<Artista> buscarPorEstilo(String estilo) {
        List<Artista> todosArtistas = buscarTodosArtistas();
        return todosArtistas.stream()
                .filter(artista -> artista.getEstilo().equalsIgnoreCase(estilo))
                .collect(Collectors.toList());
    }

    /**
     * Busca artistas com filtros combinados
     */
    public List<Artista> buscarComFiltros(String nome, String estilo) {
        List<Artista> todosArtistas = buscarTodosArtistas();
        
        return todosArtistas.stream()
                .filter(artista -> {
                    boolean matchNome = nome == null || nome.isEmpty() || 
                            artista.getNome().toLowerCase().contains(nome.toLowerCase());
                    boolean matchEstilo = estilo == null || estilo.isEmpty() || 
                            artista.getEstilo().equalsIgnoreCase(estilo);
                    return matchNome && matchEstilo;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retorna artistas padrão para demonstração (fallback)
     */
    private List<Artista> getArtistasDefault() {
        List<Artista> artistasDemo = new ArrayList<>();
        
        artistasDemo.add(new Artista("João Silva", "Rock", "Guitarra", 
                "https://instagram.com/joaorock", 
                "Guitarrista com 10 anos de experiência em bandas de rock alternativo.", 
                ""));
        
        artistasDemo.add(new Artista("Maria Santos", "MPB", "Vocal", 
                "https://instagram.com/mariampb", 
                "Cantora e compositora de MPB, com influências de Elis Regina.", 
                ""));
        
        artistasDemo.add(new Artista("DJ Beats", "Eletrônica", "DJ", 
                "https://soundcloud.com/djbeats", 
                "DJ e produtor especializado em música eletrônica e house music.", 
                ""));
        
        artistasDemo.add(new Artista("Carlos Acoustic", "Folk", "Violão", 
                "https://youtube.com/carlosacoustic", 
                "Violonista fingerstyle com repertório diverso.", 
                ""));
        
        System.out.println("📝 Carregando artistas de demonstração (modo offline)");
        return artistasDemo;
    }

    /**
     * Verifica se o serviço está funcionando
     */
    public boolean isServiceAvailable() {
        return serviceInitialized && sheetsService != null && !spreadsheetId.isEmpty();
    }

    /**
     * Retorna informações sobre o status do serviço
     */
    public String getServiceStatus() {
        if (isServiceAvailable()) {
            return "✅ Conectado ao Google Sheets";
        } else if (spreadsheetId.isEmpty()) {
            return "⚠️  ID da planilha não configurado";
        } else {
            return "❌ Serviço offline - usando dados de demonstração";
        }
    }
}