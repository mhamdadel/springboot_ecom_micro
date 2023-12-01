package ecommercemicroservices.inventory.controller;

import ecommercemicroservices.inventory.service.InventoryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private InventoryService inventoryService;

    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean isInStock(@RequestParam("codes") @NotNull List<String> codes) {
        codes.forEach((String code) -> {
            if (!inventoryService.isInStock(code)) {
                throw new IllegalArgumentException(new StringBuilder().append("Product with code").append(code).append("not exists").toString());
            }
        });
        return true;
    }
}
