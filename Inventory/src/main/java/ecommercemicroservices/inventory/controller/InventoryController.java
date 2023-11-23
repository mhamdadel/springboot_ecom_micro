package ecommercemicroservices.inventory.controller;

import ecommercemicroservices.inventory.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean isInStock(@PathVariable("code") String code) {

        return null;
    }
}
