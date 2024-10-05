package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.RequestDto;
import ar.edu.utn.frc.tup.lciii.entities.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.ListTokenSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {




        @Autowired
        private final CountryRepository countryRepository;
        @Autowired
        private final RestTemplate restTemplate;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }

        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                var cBuild = Country.builder()
                        .name((String) nameData.get("common"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .build();
                if(countryData.get("languages")!=null){
                        cBuild.setLanguages((Map<String, String>) countryData.get("languages"));
                }
                if(countryData.get("cca3")!=null) {
                        cBuild.setCode((String) countryData.get("cca3"));
                }
                if(countryData.get("borders")!=null) {
                        cBuild.setBorders((List<String>) countryData.get("borders"));
                }
                return cBuild;
        }


        private CountryDTO mapToDTO(Country country) {
                var r = new  CountryDTO();
                r.setCode(country.getCode());
                r.setName(country.getName());
                return r;
        }
        public  CountryDTO[] getCountries(String name, String code){
                var countries = getAllCountries();
                var dtos = new ArrayList<CountryDTO>();
                if(name!=null && !name.isEmpty()){

                        for(var country : countries){
                                if(country.getName().equals(name)){
                                        dtos.add( mapToDTO(country));
                                }
                        }
                }
                else if(code!=null && !code.isEmpty()){
                        for(var country : countries){
                                if(country.getCode().equals(code)){
                                        dtos.add( mapToDTO(country));
                                }
                        }
                }
                else{

                        for(var country : countries){
                             dtos.add( mapToDTO(country));

                        }
                }
                return dtos.toArray(new CountryDTO[dtos.size()]);



        }

        public CountryDTO[] getCountriesxCountinent(String continent){
                var countries = getAllCountries();

                var dtos = new ArrayList<CountryDTO>();
                if(continent!=null && !continent.isEmpty()){
                        for(var country : countries){
                                if(country.getRegion().equals(continent)){
                                        dtos.add( mapToDTO(country));
                                }
                        }
                }
                return dtos.toArray(new CountryDTO[dtos.size()]);

        }


       public CountryDTO[] getCountriesxLanguague(String language){

                var countries = getAllCountries();
                  var dtos = new ArrayList<CountryDTO>();

            if(language!=null && !language.isEmpty()){
                    for(var country : countries){
                            for (String l: country.getLanguages().values()){
                                    if(l.equals(language)){
                                            dtos.add( mapToDTO(country));
                                             break;
                                    }
                            }
                    }
            }
            return dtos.toArray(new CountryDTO[dtos.size()]);
       }

       public CountryDTO[] countrymostBorders(){

                var countries = getAllCountries();
                var dtos = new ArrayList<CountryDTO>();
                if(countries!=null && !countries.isEmpty()  ){
                        var mostBordersCountry = new Country();
                        mostBordersCountry.setBorders(new ArrayList<String>());
                        for(var country : countries) {
                                if (country.getBorders() != null) {
                                        if (country.getBorders().size() > mostBordersCountry.getBorders().size() ) {
                                                mostBordersCountry = country;
                                        }
                                }
                        }

                        dtos.add( mapToDTO(mostBordersCountry));
                }

                return dtos.toArray(new CountryDTO[dtos.size()]);
       }

       public CountryDTO[] getAndPostCountries(RequestDto num) {
                var countries = getAllCountries();
                Collections.shuffle(countries);
                var toSave = new ArrayList<Country>();
                var response = new ArrayList<CountryDTO>();
             for (int i = 0; i < num.getAmountOfCountryToSave() ; i++) {

                     toSave.add(countries.get(i));
             }



                     for (Country country : toSave) {
                             var cEntity = new CountryEntity();
                             cEntity.setArea(String.valueOf(country.getArea()));
                             cEntity.setCodigo(country.getCode());
                             cEntity.setPoblacion(String.valueOf(country.getPopulation()));
                             cEntity.setNombre(country.getName());
                             countryRepository.save(cEntity);
                     }
             for (Country country : toSave) {
                     response.add( mapToDTO(country));
             }

                return response.toArray(new CountryDTO[response.size()]);


     }

}
