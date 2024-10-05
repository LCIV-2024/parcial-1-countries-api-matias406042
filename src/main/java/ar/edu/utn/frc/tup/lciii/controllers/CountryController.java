package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.RequestDto;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class CountryController {


    private final CountryService countryService;
    @Autowired
    public CountryController(CountryService ser) {
        this.countryService = ser;
    }

    @GetMapping("/countries")
    public CountryDTO[] getCountries(@RequestParam(required = false,defaultValue = "") String name,
                                     @RequestParam(required = false,defaultValue = "" )String code) {

        return countryService.getCountries(name,code);

    }


    @GetMapping("/countries/{continent}/continent")
    public CountryDTO[] getCountriesXContinent(@PathVariable String continent) {

        return countryService.getCountriesxCountinent(continent);

    }


    @GetMapping("/countries/{languague}/languague")
    public CountryDTO[] getCountriesXLanguague(@PathVariable String languague) {

        return countryService.getCountriesxLanguague(languague);

    }
    @GetMapping("/api/countries/most-borders")
    public CountryDTO[] getCountriesMostBorders() {

        return countryService.countrymostBorders();

    }

    @PostMapping("/api/countries")
    public CountryDTO[] getCountriesxCant(@RequestBody RequestDto r) {

        return countryService.getAndPostCountries(r);

    }

}