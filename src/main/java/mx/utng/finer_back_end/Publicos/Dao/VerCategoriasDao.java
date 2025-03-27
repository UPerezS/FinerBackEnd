package mx.utng.finer_back_end.Publicos.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mx.utng.finer_back_end.Documentos.CategoriaDocumento;

@Repository
public interface VerCategoriasDao extends CrudRepository <CategoriaDocumento, Integer>{
       @Query(value = "SELECT * FROM obtener_categorias()", nativeQuery = true)
       List<Object[]> obtenerCategorias();




    
} 
    

