package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AbrigoDto;
import br.com.alura.adopet.api.dto.CadastrarAbrigoDto;
import br.com.alura.adopet.api.dto.CadastrarPetDto;
import br.com.alura.adopet.api.dto.DadosDetalhesPet;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AbrigoService {
    @Autowired
    private AbrigoRepository repository;

    @Autowired
    private PetRepository petRepository;

    public List<AbrigoDto> listar() {
        return repository
                .findAll()
                .stream()
                .map(AbrigoDto::new)
                .toList();
    }

    public void cadastrar(CadastrarAbrigoDto dto) {
        boolean jaCadastrado = repository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email());

        if (jaCadastrado) {
            throw new ValidacaoException("Dados já cadastrados para outro abrigo!");
        }

        Abrigo abrigo = new Abrigo(dto);
        repository.save(abrigo);
    }

    public List<DadosDetalhesPet> listarPets(String idOuNome) {
        Abrigo abrigo = carregarAbrigo(idOuNome);

        return petRepository
                .findByAbrigo(abrigo)
                .stream()
                .map(DadosDetalhesPet::new)
                .toList();

//        List<DadosDetalhesPet> petsDtos = new ArrayList<>();
//        List<Pet> pets = new ArrayList<>();
//        if (isSearchById(idOuNome)) {
//            Long id = Long.parseLong(idOuNome);
//            pets = repository.getReferenceById(id).getPets();
//        } else {
//            pets = repository.findByNome(idOuNome).getPets();
//        }
//        pets.forEach(p -> petsDtos.add(new DadosDetalhesPet(p)));
//        return petsDtos;
    }



    public Abrigo carregarAbrigo(String idOuNome) {
        if (isSearchById(idOuNome)) {
            Long id = Long.parseLong(idOuNome);
            return repository.getReferenceById(id);
        } else {
            return repository.findByNome(idOuNome);
        }
    }

    private boolean isSearchById(String idOuNome) {
        try {
            Long.parseLong(idOuNome);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
