package com.almoxarifado.service;

import com.almoxarifado.entity.EventoArmario;
import com.almoxarifado.entity.Usuario;
import com.almoxarifado.enums.TipoEventoArmario;
import com.almoxarifado.repository.ArmarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.io.IOException;

@Service
public class ArmarioService {

    @Autowired
    ArmarioRepository armarioRepository;

    // GPIO info
    private static final int CHIP = 0;      // gpiochip0
    private static final int LINE = 21;     // GPIO que controla a tranca

    // Método para setar GPIO usando gpioset
    private void setPin(boolean high) throws IOException, InterruptedException {
        String value = high ? "1" : "0";
        ProcessBuilder pb = new ProcessBuilder(
                "gpioset",
                String.valueOf(CHIP),
                LINE + "=" + value
        );
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Erro ao executar gpioset, código: " + exitCode);
        }
    }

    public java.util.List<EventoArmario> listarEventosArmario() {
        return armarioRepository.findAll();
    }

    // Abrir armário (destrancar)
    public void abrir(Usuario usuario) {
        try {
            setPin(true);  // ativa pino (HIGH)
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        salvarEvento(usuario, TipoEventoArmario.DESTRANCA);
    }

    // Trancar armário
    public void trancar(Usuario usuario) {
        try {
            setPin(false); // desativa pino (LOW)
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        salvarEvento(usuario, TipoEventoArmario.TRANCA);
    }

    private void salvarEvento(Usuario usuario, TipoEventoArmario tipo) {
        EventoArmario evento = new EventoArmario();
        evento.setUsuario(usuario);
        evento.setTipo(tipo);
        evento.setDataHora(LocalDateTime.now());
        armarioRepository.save(evento);
    }
}
