package mx.utng.finer_back_end.Administrador.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.UsuarioDocumento;

@Repository
public interface AdministradorDao extends JpaRepository<UsuarioDocumento, Integer>{
    
    @Query(value = "SELECT * FROM  obtener_alumnos()", nativeQuery= true)
    List<Object[]> getAlumnos();

    @Query(value = "SELECT * FROM  obtener_instructores()", nativeQuery= true)
    List<Object[]> getInstructores();
}
