import pigpio as io

import json

def main():
    pi = io.pi()
    with open("./settings.json") as f:
        data = json.load(f)
    flattened_pins = [item for sublist in data["pins"] for item in sublist]
    for pin in flattened_pins:
        pi.set_PWM_dutycycle(pin, 0)

    print("All done!")

if __name__ == "__main__":
    main()