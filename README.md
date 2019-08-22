# Pet Scanner

Queries Pet Harbor for adoptable dogs and sends out a text
when a new dog or dogs are available.

## Instructions

You need AWS credentials with SNS permissions.

Use the input.json in src/main/resources as a template. At
the very least you'll probably want to update with phone numbers.
Phone numbers should be of the format `+1XXXXXXXXXX`, where `XXXXXXXXXX`
is a US number with area code. Probably will work for other countries,
but I have not tried it.

```shell script
# Assuming you are in the /home/ubuntu directory with an input.json
# and the host has a its AWS credentials in /home/ubuntu/.aws

docker run -v /home/ubuntu/input.json:/input.json -v /home/ubuntu/.aws:/root/.aws <image name> /input.json
```

## TODO

* Determine columns dynamically based on first row. Different
shelters present data in different layouts; the current
hard-coded indexes will not work for all shelters.