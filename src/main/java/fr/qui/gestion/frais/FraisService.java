package fr.qui.gestion.frais;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FraisService {
    private final FraisRepository fraisRepository;

    @Autowired
    public FraisService(FraisRepository FraisRepository) {
        this.fraisRepository = FraisRepository;
    }
}