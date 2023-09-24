package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastrarPetDto;
import br.com.alura.adopet.api.dto.DadosDetalhesPet;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {
    @Autowired
    private PetRepository repository;

    @Autowired
    private AbrigoService abrigoService;

    public List<DadosDetalhesPet> listarTodosDisponiveis() {
        return repository
                .findAllByAdotadoFalse()
                .stream()
                .map(DadosDetalhesPet::new)
                .toList();
    }

    public void cadastrarPet(CadastrarPetDto dto, Abrigo abrigo) {
        try {
            Pet pet = new Pet(dto, abrigo);
            pet.setAbrigo(abrigo);
            pet.setAdotado(false);
            abrigo.getPets().add(pet);
        } catch (EntityNotFoundException enfe) {
            throw enfe;
        }
    }
}
