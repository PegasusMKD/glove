from asyncio import sleep
import time
from typing import Any, Dict, List

import pigpio as io

from requests import post

# Constants
script_active = True
start_active = True


# General functions
def setModes(pi: io.pi, output_pins: list[int]) -> bool:
    """
    Set the modes for all the pins as output (value will be sent through them)


    :param pi: The current micro-controller's class
    :param input_pins: The pins that are being used on the micro-controller as a flattened list
    :return: Whether it successfully set the modes of the pins
    """
    for pin in output_pins:
        try:
            pi.set_mode(pin, io.OUTPUT)
        except:
            return False
    return True


def sortRGBValues(loadout: list[Dict[str, object]]) -> list[Dict[str, Any]]:
    """
    Function for sorting the loadouts into the order of:\n
    [{finger: 1,...}, {finger: 2,...}...]


    :param loadout: the loadouts that should be sorted
    :return: a list of the sorted loadouts
    """
    sorted_loadout = []
    for finger in range(1, 6):
        for load in loadout:
            if load["finger"] == finger:
                sorted_loadout.append(load)
                break

    return sorted_loadout


# Main async functions

async def setLEDValues(pi: io.pi, pins: list[int], data: Dict[str, Any]):
    """
    Function which sets the coloring values for a finger


    :param pi: The current micro-controller's class
    :param pins: The pins that are being used on the micro-controller segregated by finger
    :param data: A Dict of the data that contains the RGB values, as well as pause and active times
    """
    for pin, color in zip(pins[:-1], data.keys()):
        if color in ["pauseTime", "finger"]:
            continue
        pi.set_PWM_dutycycle(pin, data[color])

    await sleep(data["activeTime"])
    if data["pauseTime"] != 0:
        empty = {
            "red": 0,
            "green": 0,
            "blue": 0,
            "pauseTime": 0,
            "activeTime": data["pauseTime"]
        }
        await setLEDValues(pi, pins, empty)
        setLEDValues(pi, pins, data)


async def startLEDs(pi: io.pi, loadoutData: Dict[str, Any], allPins: List[List[int]]):
    """
    Function which starts the coloring on the fingers


    :param pi: The current micro-controller's class
    :param loadoutData: The data for all the loadouts
    :param allPins: The pins that are being used on the micro-controller segregated by finger
    """
    empty = {
        "red": 0,
        "green": 0,
        "blue": 0,
        "pauseTime": 0,
        "activeTime": 0
    }
    sorted_loadout = sortRGBValues(loadoutData["loadouts"])
    for pinsFinger, RGBValue, pauseTime in zip(allPins, sorted_loadout, loadoutData["pauseValues"]):
        setLEDValues(pi, pinsFinger, RGBValue)
        await sleep(pauseTime)

    if 0 not in loadoutData["pauseValues"]:
        for pinsFinger in allPins:
            setLEDValues(pi, pinsFinger, empty)

        startLEDs(pi, loadoutData, allPins)


async def start(pi: io.pi, serialNumber: str, allPins: list[list[int]]):
    """
    The real "main" of the script


    :param pi: The current micro-controller's class
    :param serialNumber: The serial number of the Raspberry Pi for the back-end to recognize
    :param allPins: The pins that are being used on the micro-controller segregated by finger
    """
    account_data = post("http://localhost:8080/api/account/", headers={"Content-Type": "application/json"},
                        data={"serialNumber": serialNumber})
    startLEDs(pi, account_data["loadoutList"], allPins)




async def main():
    pi = io.pi()
    pins = []
    print("Write them in this format: redPin greenPin bluePin")
    for finger in range(1, 6):
        pinsString = input(f"Pins for finger {finger}:")
        pins.append([int(pin) for pin in pinsString.split(" ")])

    flattened_pins = [item for sublist in pins for item in sublist]
    if not setModes(pi, flattened_pins):
        return "Something went wrong with the mode setting! Please try again!"

    serialNumber = input("Please enter the serial number of the device:")
    start(pi, serialNumber, pins)
    while True:
        print("Working!")
        await sleep(10000)


if __name__ == "__main__":
    main()
