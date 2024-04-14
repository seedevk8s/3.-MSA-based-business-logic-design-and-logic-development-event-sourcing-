package co.kr.ideacube.inventory.controller;

import co.kr.ideacube.dto.InventoryRequestDTO;
import co.kr.ideacube.dto.InventoryResponseDTO;
import co.kr.ideacube.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("inventory")
public class InventoryController {

    @Autowired
    private InventoryService service;

    @PostMapping("/deduct")
    public InventoryResponseDTO deduct(@RequestBody final InventoryRequestDTO requestDTO){
        return this.service.deductInventory(requestDTO);
    }

    @PostMapping("/add")
    public void add(@RequestBody final InventoryRequestDTO requestDTO){
        this.service.addInventory(requestDTO);
    }

}
