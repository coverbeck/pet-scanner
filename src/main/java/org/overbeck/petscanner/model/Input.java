package org.overbeck.petscanner.model;

public class Input {
    public Options options;
    public Shelter[] shelters;
    public String[] phoneNumbers;

    public static class Options {
        public String frequencyInMinutes;
    }

    public static class Shelter {
        private String name;
        private String url;

        public Shelter() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
