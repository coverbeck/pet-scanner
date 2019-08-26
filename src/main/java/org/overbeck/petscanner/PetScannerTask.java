package org.overbeck.petscanner;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.common.collect.Sets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.overbeck.petscanner.model.Dog;
import org.overbeck.petscanner.model.Input;

public class PetScannerTask extends TimerTask {

    private final Input.Shelter[] shelters;
    private final AmazonSNSClient amazonSNSClient;
    private final String[] phoneNumbers;

    public PetScannerTask(Input input) {
        this.shelters = input.shelters;
        phoneNumbers = input.phoneNumbers;
        amazonSNSClient = new AmazonSNSClient();
    }

    @Override
    public void run() {
        Arrays.stream(shelters).forEach(shelter -> {
            try {
                final Document doc = Jsoup.connect(shelter.getUrl()).get();
                final String baseUrl = App.baseUrl(shelter.getUrl());
                final Element element = doc.select("table.ResultsTable").first();
                final Indexes indexes = calculateIndexes(element);
                Set<Dog> mostRecentDogs = element.select("tr").stream().skip(1) // First row is a header column
                        .map(e -> {
                            Elements tds = e.select("td");
                            return new Dog(getTdText(tds, indexes.ID_INDEX), getTdText(tds, indexes.NAME_INDEX), getTdText(tds, indexes.GENDER_INDEX),
                                    getTdText(tds, indexes.COLOR_INDEX), getTdText(tds, indexes.BREED_INDEX), getTdText(tds, indexes.AGE_INDEX),
                                    getTdText(tds, indexes.WEIGHT_INDEX), getImageUrl(tds.get(indexes.DETAILS_INDEX), baseUrl),
                                    getDetailsUrl(tds.get(indexes.DETAILS_INDEX), baseUrl), getTdText(tds, indexes.TIME_AT_SHELTER_INDEX));
                        }).collect(Collectors.toSet());
                final Set<Dog> knownDogsForShelter = App.knownDogs.get(shelter.getName());
                Set<Dog> difference = newDogs(mostRecentDogs, knownDogsForShelter);
                if (difference.size() == 0) {
                    System.out.println("No new dogs!");
                } else {
                    sendMessage(difference);
                    difference.stream().sorted(Comparator.comparing(dog -> dog.timeAtShelter)).forEach(dog -> System.out.println(dog));
                    App.knownDogs.put(shelter.getName(), mostRecentDogs);
                }

            } catch (Exception ex) {
                System.err.println("Error reading " + shelter.getName());
                ex.printStackTrace();
            }
        });
    }

    public static Indexes calculateIndexes(Element table) {
        final Indexes indexes = new Indexes();
        final Element headerRow = table.selectFirst("tr");
        final Elements tds = headerRow.select("td");
        IntStream.range(0, tds.size())
                .forEach(index -> {
                    final String text = tds.get(index).text().toLowerCase();
                    if (text.contains("gender")) {
                        indexes.GENDER_INDEX = index;
                    } else if (text.contains("color")) {
                        indexes.COLOR_INDEX = index;
                    } else if (text.contains("breed")) {
                        indexes.BREED_INDEX = index;
                    } else if (text.contains("age")) {
                        indexes.AGE_INDEX = index;
                    } else if (text.contains("time")) {
                        indexes.TIME_AT_SHELTER_INDEX = index;
                    } else if (text.contains("weight")) {
                        indexes.WEIGHT_INDEX = index;
                    } else if (text.contains("id")) {
                        indexes.ID_INDEX = index;
                    } else if (text.contains("name")) {
                        indexes.NAME_INDEX = index;
                    } else if (text.contains("picture")) {
                        indexes.DETAILS_INDEX = index;
                    }
                });
        return indexes;
    }

    private void sendMessage(Set<Dog> newDogs) {
        final String names = dogsForSms(newDogs);
        final String message = textMessage("Found " + newDogs.size() + " new dog" + (newDogs.size() > 1 ? "s" : "") + ": " + names);

        final Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Promotional") //Sets the type to promotional.
                .withDataType("String"));
        smsAttributes
                .put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue("PetScanner") //The sender ID shown on the device.
                        .withDataType("String"));
        Arrays.stream(phoneNumbers).forEach(phoneNumber -> {
            PublishResult result = amazonSNSClient
                    .publish(new PublishRequest().withMessage(message).withPhoneNumber(phoneNumber).withMessageAttributes(smsAttributes));
            System.out.println(result); // Prints the message ID.
        });
    }

    private String dogsForSms(Set<Dog> newDogs) {
        return newDogs.stream()
                .map(dog -> dog.name + ' ' + dog.breed)
                .collect(Collectors.joining(","));
    }

    private String textMessage(String message) {
        if (message.length() > 140) {
            return message.substring(0, 137) + "...";
        }
        return message;
    }

    private String getTdText(Elements tds, Integer index) {
        if (index == null) {
            return null;
        }
        return tds.get(index).text();
    }

    private String getImageUrl(Element td, String baseUrl) {
        return absoluteUrl(td, "a", "href", baseUrl);
    }

    private String getDetailsUrl(Element td, String baseUrl) {
        return absoluteUrl(td, "img", "src", baseUrl);
    }

    private String absoluteUrl(Element td, String selector, String attr, String baseUrl) {
        final String url = td.select(selector).attr(attr);
        // A less that thorough determination
        if (!url.startsWith("http")) {
            return baseUrl + url;
        }
        return url;
    }

    private Set<Dog> newDogs(Set<Dog> mostRecentDogs, Set<Dog> knownDogs) {
        if (knownDogs == null) {
            return mostRecentDogs;
        }
        return Sets.difference(mostRecentDogs, knownDogs);
    }
    
    public static class Indexes {
        public  Integer DETAILS_INDEX;
        public  Integer ID_INDEX;
        public  Integer NAME_INDEX;
        public  Integer GENDER_INDEX;
        public  Integer COLOR_INDEX;
        public  Integer BREED_INDEX;
        public  Integer AGE_INDEX;
        public  Integer WEIGHT_INDEX;
        public  Integer TIME_AT_SHELTER_INDEX;

    }
}
