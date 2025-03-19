package mx.utng.finer_back_end.Administrador.Dao;

import org.springframework.stereotype.Repository;
import mx.utng.finer_back_end.Documentos.CursoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CursoDao extends JpaRepository<CursoDocumento, Long> {
    
}
