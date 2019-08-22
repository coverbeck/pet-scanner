package org.overbeck.petscanner.model;

import java.util.Objects;

public class Dog {
    public final String id;
    public final String name;
    public final String gender;
    public final String color;
    public final String breed;
    public final String age;
    public final String weight;
    public final String imageUrl;
    public final String detailsUrl;
    public final String timeAtShelter;

    public Dog(String id, String name, String gender, String color, String breed, String age, String weight, String imageUrl,
            String detailsUrl, String timeAtShelter) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.color = color;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.imageUrl = imageUrl;
        this.detailsUrl = detailsUrl;
        this.timeAtShelter = timeAtShelter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Dog dog = (Dog)o;
        return Objects.equals(id, dog.id) && Objects.equals(name, dog.name) && Objects.equals(gender, dog.gender) && Objects
                .equals(color, dog.color) && Objects.equals(breed, dog.breed) && Objects.equals(age, dog.age) && Objects
                .equals(weight, dog.weight) && Objects.equals(imageUrl, dog.imageUrl) && Objects.equals(detailsUrl, dog.detailsUrl)
                && Objects.equals(timeAtShelter, dog.timeAtShelter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, gender, color, breed, age, weight, imageUrl, detailsUrl, timeAtShelter);
    }

    @Override
    public String toString() {
        return "Dog{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", gender='" + gender + '\'' + ", color='" + color + '\''
                + ", breed='" + breed + '\'' + ", age='" + age + '\'' + ", weight='" + weight + '\'' + ", imageUrl='" + imageUrl + '\''
                + ", detailsUrl='" + detailsUrl + '\'' + ", timeAtShelter='" + timeAtShelter + '\'' + '}';
    }

}
