from asyncio import sleep

from typing import Any, Dict, List

import pigpio as io

import asyncio

import os

from requests import post
import json

# Constants
global start_active
global script_active


# General functions

# TODO: Return the call tasks instead of the prints
# TODO: Return the pi class calls as types

def setModes(pi: io.pi, output_pins: List[int]) -> bool:
    """
    Set the modes for all the pins as output (value will be sent through them)


    :param pi: The current micro-controller's class
    :param input_pins: The pins that are being used on the micro-controller as a flattened list
    :return: Whether it successfully set the modes of the pins
    """
    print(output_pins)
    for pin in output_pins:
        try:
            pi.set_mode(pin, io.OUTPUT)
        except:
            return False
    print("Set all of the modes!")
    return True


def sortRGBValues(loadout: List[Dict[str, object]]) -> List[Dict[str, Any]]:
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


def getActiveLoadout(loadoutList: list):
    for loadout in loadoutList:
        if loadout["active"] == True:
            return loadout


# Main async functions


async def setLEDValues(pi: io.pi, pins: List[int], data):
    """
    Function which sets the coloring values for a finger


    :param pi: The current micro-controller's class
    :param pins: The pins that are being used on the micro-controller segregated by finger
    :param data: A Dict of the data that contains the RGB values, as well as pause and active times
    """
    if start_active != True:
        print("Returning...?")
        return

    print(f"{data}\n")
    for pin, color in zip(pins, ["red","green","blue"]):
        print(f"Set pin {pin} with value {data[color]} on data {data}")
        pi.set_PWM_dutycycle(pin, data[color])

    await sleep(data["activeTime"])
    print(f"Finished wait time for {data}")
    if data["pauseTime"] != 0:
        empty = {
            "emptied": True,
            "id": data["id"],
            "finger": data["finger"],
            "activeTime": data["pauseTime"],
            "red": 0,
            "green": 0,
            "blue": 0,
            "pauseTime": 0
        }
        loop = asyncio.get_event_loop()
        await setLEDValues(pi, pins, empty)
        loop.create_task(setLEDValues(pi, pins, data))


async def startLEDs(pi: io.pi, loadoutData: Dict[str, Any], allPins: List[List[int]]):
    """
    Function which starts the coloring on the fingers


    :param pi: The current micro-controller's class
    :param loadoutData: The data for all the loadouts
    :param allPins: The pins that are being used on the micro-controller segregated by finger
    """
    if start_active != True:
        print("Returning...?")
        return

    loop = asyncio.get_event_loop()

    empty = {
        "red": 0,
        "green": 0,
        "blue": 0,
        "pauseTime": 0,
        "activeTime": 0
    }
    sorted_loadout = sortRGBValues(loadoutData["rgbValues"])
    for pinsFinger, RGBValue, pauseTime in zip(allPins, sorted_loadout, loadoutData["pauseValues"]):
        print(f"Setting value for pins: {pinsFinger} with RGBValue: {RGBValue} and pause {pauseTime}")
        loop.create_task(setLEDValues(pi, pinsFinger, RGBValue))
        await sleep(pauseTime)

    if 0 in loadoutData["pauseValues"]:
        for pinsFinger in allPins:
            print(f"Setting the pause values for {pinsFinger}")
            loop.create_task(setLEDValues(pi, pinsFinger, empty))

        print("Resetting back!")
        loop.create_task(startLEDs(pi, loadoutData, allPins))


async def start(pi: io.pi, serialNumber: str, allPins: List[List[int]]):
    """
    The real "main" of the script


    :param pi: The current micro-controller's class
    :param serialNumber: The serial number of the Raspberry Pi for the back-end to recognize
    :param allPins: The pins that are being used on the micro-controller segregated by finger
    """
    global start_active

    account_data = post("http://34.107.52.197:8080/api/account/loadouts", headers={"Content-Type": "application/json"},
                        data=json.dumps({"serialNumber": serialNumber}))
    data = json.loads(account_data.content)
    print(data)
    loop = asyncio.get_event_loop()
    loop.create_task(startLEDs(pi, getActiveLoadout(data), allPins))
    print("Called the startLEDs again!")
    while script_active:
        await sleep(10)
        new_loadout = post("http://34.107.52.197:8080/api/account/change", headers={"Content-Type": "application/json"},
                           data=json.dumps({"serialNumber": serialNumber}))
        print(new_loadout)
        new_data = json.loads(new_loadout.content)
        if new_data["active"] == True:
            start_active = False
            pending = asyncio.all_tasks()

            notGathered = []
            for task in pending:
                if task.get_stack()[0].f_code.co_name in ["<module>", "main"]:
                    notGathered.append(task)

            for task in notGathered:
                pending.remove(task)

            await asyncio.gather(*pending, loop=loop)
            print("Finished all tasks!")
            await sleep(2)
            print("Seems like it!!!")
            start_active = True

            loop = asyncio.get_event_loop()
            print("Called the startLEDs")
            loop.create_task(startLEDs(pi, getActiveLoadout(new_data["loadoutList"]), allPins))
            # Not needed since not using Android and no need to tell the app that it reset the values
            # post("http://34.107.52.197:8080/api/account/changed", headers={"Content-Type": "application/json"},
            #      data={"serialNumber": serialNumber})


async def main():
    """
    Main function for starting up the script


    :return:
    """
    print("calls script")
    global script_active
    global start_active
    script_active = True
    start_active = True
    pi = io.pi()
    print("got the pi!")
    print(pi)
    pins = []
    if os.path.isfile("./settings.json"):
        with open("./settings.json", "r+") as f:
            tmpData = json.load(f)
        pins = tmpData["pins"]
        serialNumber = tmpData["serialNumber"]
    else:
        print("Write them in this format: redPin greenPin bluePin")
        for finger in range(1, 6):
            pinsString = input(f"Pins for finger {finger}:")
            pins.append([int(pin) for pin in pinsString.split(" ")])
        serialNumber = input("Please enter the serial number of the device:")
        with open("./settings.json", "w+") as f:
            f.write(json.dumps({"pins": pins, "serialNumber": serialNumber}))

    flattened_pins = [item for sublist in pins for item in sublist]

    if not setModes(pi, flattened_pins):
        print("Something went wrong with the mode setting! Please try again!")
        return

    loop = asyncio.get_event_loop()
    loop.create_task(start(pi, serialNumber, pins))
    while script_active:
        print("Working!")
        await sleep(10)


if __name__ == "__main__":
    asyncio.run(main())
