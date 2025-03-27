package mx.utng.finer_back_end.Publicos.Implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;
import mx.utng.finer_back_end.Publicos.Dao.VerCategoriasDao;
import mx.utng.finer_back_end.Publicos.Documentos.VerCategoriasDTO;
import mx.utng.finer_back_end.Publicos.Services.VerCategoriaService;
@Service

public class VerCategoriaImplement  implements VerCategoriaService{
    
    @Autowired
    private VerCategoriasDao verCategoriasDao;
   
    @Override
    @Transactional
    public List<VerCategoriasDTO> obtenerCategorias() {
        List<Object[]> resultados = verCategoriasDao.obtenerCategorias();
        List<VerCategoriasDTO> detalles = new ArrayList<>();
        
        for (Object[] row : resultados) {
            VerCategoriasDTO verCategoriaDetalle = new VerCategoriasDTO(
                (Integer) row[0],  //IdCategoria
                (String) row[1],   // descripcion
                (String) row[2] //IdCategoria
            );
            detalles.add(verCategoriaDetalle);
        }
        
        return detalles;
    }
}




