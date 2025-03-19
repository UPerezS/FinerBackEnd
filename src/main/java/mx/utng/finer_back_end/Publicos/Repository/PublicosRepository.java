package mx.utng.finer_back_end.Publicos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.UsuarioDocumento;

@Repository
public interface PublicosRepository extends JpaRepository<UsuarioDocumento, Long> {

    @Query("SELECT u.correo FROM UsuarioDocumento u WHERE u.idUsuario = :idUsuario")
    String findCorreoById(Long idUsuario);
}
