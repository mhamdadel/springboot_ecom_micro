package ecommercemicroservices.inventory.listener;


import ecommercemicroservices.inventory.InventoryApplication;
import ecommercemicroservices.inventory.model.Inventory;
import ecommercemicroservices.inventory.repository.InventoryRepository;
import ecommercemicroservices.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@EnableKafka
public class KafkaListenerManager {
    private static final Logger LOG = LoggerFactory.getLogger(InventoryApplication.class);

    @Autowired
    private final InventoryService inventoryService;

    @Autowired
    private final InventoryRepository inventoryRepository;

    public KafkaListenerManager(InventoryService inventoryService, InventoryRepository inventoryRepository) {
        this.inventoryService = inventoryService;
        this.inventoryRepository = inventoryRepository;
    }

    @KafkaListener(id = "orders", topics = "orders", groupId = "stock")
    public void onEvent(Inventory inventory) {
        LOG.info("Received: {}" , inventory);
//        if (inventory.getStatus().equals("NEW"))
//            inventoryService.reserve(inventory);
//        else
//            inventoryService.confirm(inventory);
    }
}