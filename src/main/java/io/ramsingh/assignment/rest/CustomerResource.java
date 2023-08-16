package io.ramsingh.assignment.rest;

import io.ramsingh.assignment.model.CustomerDTO;
import io.ramsingh.assignment.service.CustomerService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable final Long uuid) {
        return ResponseEntity.ok(customerService.get(uuid));
    }

    @PostMapping
    public ResponseEntity<Long> createCustomer(@RequestBody @Valid final CustomerDTO customerDTO) {
        final Long createdUuid = customerService.create(customerDTO);
        return new ResponseEntity<>(createdUuid, HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Long> updateCustomer(@PathVariable final Long uuid,
            @RequestBody @Valid final CustomerDTO customerDTO) {
        customerService.update(uuid, customerDTO);
        return ResponseEntity.ok(uuid);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable final Long uuid) {
        customerService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

}
