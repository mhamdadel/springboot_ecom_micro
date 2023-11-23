package ecommercemicroservices.inventory.service;

import ecommercemicroservices.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;


    public Boolean isInStock(String code) {
        return inventoryRepository.findByCode(code).isPresent();
    }

}
