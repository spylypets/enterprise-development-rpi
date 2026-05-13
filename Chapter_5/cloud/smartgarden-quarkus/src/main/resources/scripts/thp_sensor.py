#!/usr/bin/python

import math
import sys
import time
import datetime as dt
import smbus2
import bme280
import json

# BME280 sensor address (default address)
address = 0x76

# Initialize I2C bus
bus = smbus2.SMBus(1)

# Load calibration parameters
calibration_params = bme280.load_calibration_params(bus, address)

def celsius_to_fahrenheit(celsius):
    return (celsius * 9/5) + 32

def main():

    try:
        # Read sensor data
        data = bme280.sample(bus, address, calibration_params)

        # Extract temperature, pressure, and humidity
        temperature_celsius = data.temperature
        pressure = data.pressure
        humidity = data.humidity

        # Convert temperature to Fahrenheit
        temperature_fahrenheit = celsius_to_fahrenheit(temperature_celsius)

        # Get the current time
        timestamp = dt.datetime.now().strftime("%Y-%m-%d %H:%M:%S")

        # Output the readings
        data = {"temperature": "{:.1f}".format(temperature_celsius), "pressure": "{:.2f}".format(pressure), "humidity": "{:.1f}".format(humidity), "created": timestamp}
        print(json.dumps(data))

    except KeyboardInterrupt:
        print('Program stopped')

    except Exception as e:

        print('An unexpected error occurred:', str(e))

if __name__ == '__main__':
    main()
