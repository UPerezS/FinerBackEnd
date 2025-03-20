package mx.utng.finer_back_end.Administrador.Dao;

import mx.utng.finer_back_end.Documentos.CategoriaDocumento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaDao extends JpaRepository<CategoriaDocumento, Integer> {
    // Métodos adicionales si es necesario
}
