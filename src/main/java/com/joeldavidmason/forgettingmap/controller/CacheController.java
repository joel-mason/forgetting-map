package com.joeldavidmason.forgettingmap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


public interface CacheController<T> {

    @GetMapping("/retrieve/{id}")
    public ResponseEntity<T> retrieve(@PathVariable int id);

    @PostMapping("/add")
    public ResponseEntity<Void> add(@RequestBody T body);
}
