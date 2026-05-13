#!/usr/bin/python

import math
import sys
import time
import datetime as dt
import bme280
import smbus2
import json

from flask import Flask, jsonify
# Import serve from waitress AFTER installing it
from waitress import serve

app = Flask(__name__)

# BME280 sensor address (default address)
address = 0x76

# Initialize I2C bus
bus = smbus2.SMBus(1)

# Load calibration parameters
calibration_params = bme280.load_calibration_params(bus, address)

@app.route('/bme280/thp', methods=['GET'])
def get_data():
    return jsonify(get_sensor_data()), 200

def celsius_to_fahrenheit(celsius):
    return (celsius * 9/5) + 32

def get_sensor_data():
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
        return data

    except Exception as e:
        print('An unexpected error occurred:', str(e))

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
#    app.run(debug=False)
# Run Waitress server when script is executed directly
    print("Starting Waitress server on http://0.0.0.0:8088")
    serve(app, host="0.0.0.0", port=8088)
