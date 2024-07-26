package demo.controller;

import demo.model.ConversionResponse;
import demo.service.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversions")
public class ConversionController {

    @Autowired
    private ConversionService conversionService;

    @GetMapping("/query")
    public ConversionResponse processQuery(@RequestParam String query) {
        String response = conversionService.processQuery(query);
        return new ConversionResponse("Response", response);
    }
}