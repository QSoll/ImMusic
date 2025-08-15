package com.musicosapp.controller;

import com.musicosapp.model.Artista;
import com.musicosapp.service.GoogleSheetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller principal do I'MUSIC
 * Gerencia as rotas da aplicação e APIs REST
 */
@Controller
public class ArtistaController {

    private static final Logger logger = LoggerFactory.getLogger(ArtistaController.class);

    @Autowired
    private GoogleSheetsService googleSheetsService;

    /**
     * Página principal da aplicação
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("serviceStatus", googleSheetsService.getServiceStatus());
        return "index";
    }

    /**
     * API REST - Cadastrar novo artista
     */
    @PostMapping("/api/artistas")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cadastrarArtista(@RequestBody Artista artista) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (artista == null || !artista.isValid()) {
                response.put("success", false);
                response.put("message", "Dados do artista são inválidos. Nome, estilo e especialidade são obrigatórios.");
                return ResponseEntity.badRequest().body(response);
            }

            boolean salvou = googleSheetsService.salvarArtista(artista);

            if (salvou) {
                response.put("success", true);
                response.put("message", "Artista cadastrado com sucesso!");
                response.put("artista", artista);
                logger.info("✅ Novo artista cadastrado: {}", artista.getNome());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Erro ao salvar artista. Tente novamente.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro interno: " + e.getMessage());
            logger.error("❌ Erro ao cadastrar artista", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * API REST - Buscar todos os artistas
     */
    @GetMapping("/api/artistas")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> buscarTodosArtistas() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Artista> artistas = googleSheetsService.buscarTodosArtistas();

            response.put("success", true);
            response.put("total", artistas.size());
            response.put("artistas", artistas);
            response.put("serviceStatus", googleSheetsService.getServiceStatus());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao buscar artistas: " + e.getMessage());
            response.put("artistas", List.of()); // Evita chamada duplicada
            logger.error("❌ Erro ao buscar todos os artistas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * API REST - Buscar artistas com filtros
     */
    @GetMapping("/api/artistas/buscar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> buscarArtistasComFiltros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String estilo) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<Artista> artistas;

            if ((nome == null || nome.isEmpty()) && (estilo == null || estilo.isEmpty())) {
                artistas = googleSheetsService.buscarTodosArtistas();
            } else {
                artistas = googleSheetsService.buscarComFiltros(nome, estilo);
            }

            response.put("success", true);
            response.put("total", artistas.size());
            response.put("artistas", artistas);
            response.put("filtros", Map.of(
                    "nome", nome != null ? nome : "",
                    "estilo", estilo != null ? estilo : ""
            ));

            logger.info("🔍 Busca realizada - Total encontrado: {}", artistas.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro na busca: " + e.getMessage());
            response.put("artistas", List.of());
            logger.error("❌ Erro na busca com filtros", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * API REST - Buscar artista por ID
     */
    @GetMapping("/api/artistas/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> buscarArtistaPorId(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Artista> todosArtistas = googleSheetsService.buscarTodosArtistas();

            Artista artistaEncontrado = todosArtistas.stream()
                    .filter(artista -> id.equals(artista.getId()))
                    .findFirst()
                    .orElse(null);

            if (artistaEncontrado != null) {
                response.put("success", true);
                response.put("artista", artistaEncontrado);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Artista não encontrado com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao buscar artista: " + e.getMessage());
            logger.error("❌ Erro ao buscar artista por ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * API REST - Obter estatísticas da plataforma
     */
    @GetMapping("/api/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Artista> todosArtistas = googleSheetsService.buscarTodosArtistas();

            Map<String, Long> porEstilo = todosArtistas.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            Artista::getEstilo,
                            java.util.stream.Collectors.counting()
                    ));

            Map<String, Long> porEspecialidade = todosArtistas.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            Artista::getEspecialidade,
                            java.util.stream.Collectors.counting()
                    ));

            response.put("success", true);
            response.put("totalArtistas", todosArtistas.size());
            response.put("artistasPorEstilo", porEstilo);
            response.put("artistasPorEspecialidade", porEspecialidade);
            response.put("serviceStatus", googleSheetsService.getServiceStatus());
            response.put("serviceAvailable", googleSheetsService.isServiceAvailable());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao obter estatísticas: " + e.getMessage());
            logger.error("❌ Erro ao obter estatísticas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * API REST - Health check da aplicação
     */
    @GetMapping("/api/health")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();

        response.put("status", "UP");
        response.put("application", "I'MUSIC Platform");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("googleSheetsService", googleSheetsService.getServiceStatus());

        return ResponseEntity.ok(response);
    }

    /**
     * Página de administração
     */
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("serviceStatus", googleSheetsService.getServiceStatus());
        model.addAttribute("isServiceAvailable", googleSheetsService.isServiceAvailable());

        try {
            List<Artista> artistas = googleSheetsService.buscarTodosArtistas();
            model.addAttribute("totalArtistas", artistas.size());
            model.addAttribute("artistas", artistas);
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar artistas: " + e.getMessage());
            logger.error("❌ Erro na página de administração", e);
        }

        return "admin";
    }
}
