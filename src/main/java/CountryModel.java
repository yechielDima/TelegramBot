import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class CountryModel {
        private String name;
        private String nativeName;
        private String capital;
        private int population;
        private String alpha2Code;
        private String alpha3Code;
        private double area;
        private List<String> borders;
        private List<Language> languages;


        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getNativeName() {
            return nativeName;
        }

        public void setNativeName(String nativeName) {
            this.nativeName = nativeName;
        }
        public String getCapital() {
            return capital;
        }
        public void setCapital(String capital) {
            this.capital = capital;
        }
        public int getPopulation() {
            return population;
        }
        public void setPopulation(int population) {
            this.population = population;
        }
        public List<Language> getLanguages() {
            return languages;
        }
        public void setLanguages(List<Language> languages) {
            this.languages = languages;
        }
        public String getAlpha2Code() {
            return alpha2Code;
        }

        public void setAlpha2Code(String alpha2Code) {
            this.alpha2Code = alpha2Code;
        }

        public String getAlpha3Code() {
            return alpha3Code;
        }

        public void setAlpha3Code(String alpha3Code) {
            this.alpha3Code = alpha3Code;
        }

        public double getArea() {
            return area;
        }

        public void setArea(double area) {
            this.area = area;
        }
        public String getBorders() {
            StringBuilder toReturn = new StringBuilder();
            for (String border : this.borders) {
                toReturn.append(border).append(", ");
            }
            return String.valueOf(toReturn);
        }
        public void setBorders(List<String> borders) {
            this.borders = borders;
        }



        public String[] getFullInformationAboutCountry(){
            return new String[]{"name: " + this.name
                    , "native name: " + this.nativeName
                    , "capital: " + this.capital
                    , "language: " + this.languages
                    , "population: " + this.population
                    , "area: " + this.area
                    , "alpha 2 Code: " + this.alpha2Code
                    , "alpha 3 Code: " + this.alpha3Code
                    , "borders: " + getBorders()
            };
        }

    }