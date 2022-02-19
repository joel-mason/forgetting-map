package com.joeldavidmason.forgettingmap.controller.impl;

import com.joeldavidmason.forgettingmap.controller.CacheController;
import com.joeldavidmason.forgettingmap.model.UUIDCacheBody;
import com.joeldavidmason.forgettingmap.service.impl.UUIDCacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("api/v1/uuid")
public class UUIDCacheController implements CacheController<UUIDCacheBody> {

    private final UUIDCacheService uuidCacheService;

    public UUIDCacheController(UUIDCacheService uuidCacheService) {
        this.uuidCacheService = uuidCacheService;
    }

    public ResponseEntity<UUIDCacheBody> retrieve(@PathVariable int id) {
        return ResponseEntity.ok(uuidCacheService.retrieve(id));
    }

    public ResponseEntity<Void> add(@RequestBody UUIDCacheBody body) {
        uuidCacheService.add(body);
        return ResponseEntity.created(URI.create("api/v1/uuid/retrieve/" + body.getId())).build();
    }

}
