package com.almoxarifado.service;

import com.almoxarifado.entity.EventoArmario;
import com.almoxarifado.entity.Usuario;
import com.almoxarifado.enums.TipoEventoArmario;
import com.almoxarifado.repository.ArmarioRepository;
import com.pi4j.io.gpio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArmarioService {

    @Autowired
    ArmarioRepository armarioRepository;

    // GPIO setup (para LED teste, GPIO_00 = pino físico 11)
    private final GpioController gpio = GpioFactory.getInstance();
    private final GpioPinDigitalOutput pino = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "Tranca", PinState.LOW);

    public List<EventoArmario> listarEventosArmario() {
        return armarioRepository.findAll();
    }

    // Abrir armário (destrancar)
    public void abrir(Usuario usuario) {
        try {
            pino.high(); // acende LED / ativa relé
            Thread.sleep(2000); // mantém 2 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pino.low(); // desliga LED / relé
        }

        salvarEvento(usuario, TipoEventoArmario.DESTRANCA);
    }

    // Trancar armário
    public void trancar(Usuario usuario) {
        // Se usar relé/LED, aqui pode não precisar ligar nada
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
