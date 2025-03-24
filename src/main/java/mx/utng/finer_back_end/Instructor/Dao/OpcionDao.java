package mx.utng.finer_back_end.Instructor.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mx.utng.finer_back_end.Documentos.OpcionDocumento;

public interface OpcionDao extends JpaRepository<OpcionDocumento, Integer> {

    @Query(value = "SELECT agregar_opcion(:p_id_pregunta, :p_texto_opcion, :p_verificar)", nativeQuery = true)
    Integer agregarOpcion(@Param("p_id_pregunta") Integer idPregunta,
                          @Param("p_texto_opcion") String textoOpcion, 
                          @Param("p_verificar") Boolean verificar);  // Cambiar "correcta" por "verificar"
}
