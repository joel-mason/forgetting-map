package com.joeldavidmason.forgettingmap.controller.impl;

import com.joeldavidmason.forgettingmap.controller.CacheController;
import com.joeldavidmason.forgettingmap.model.StringCacheBody;
import com.joeldavidmason.forgettingmap.service.impl.StringCacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("api/v1/string")
public class StringCacheController implements CacheController<StringCacheBody> {

    private final StringCacheService stringCacheService;

    public StringCacheController(StringCacheService stringCacheService) {
        this.stringCacheService = stringCacheService;
    }

    public ResponseEntity<StringCacheBody> retrieve(@PathVariable int id) {
        return ResponseEntity.ok(stringCacheService.retrieve(id));
    }

    public ResponseEntity<Void> add(@RequestBody StringCacheBody body) {
        stringCacheService.add(body);
        return ResponseEntity.created(URI.create("api/v1/uuid/retrieve/" + body.getId())).build();
    }

}
