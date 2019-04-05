import RPi.GPIO as GPIO
import time
import string

class Morse_code(object):

    def __init__(self):
        self.morse = morse = {"a":".-", "b":"-...", "c":"-.-.", "d":"-..",
                            "e":'.', "f":"..-.", "g":"--.", "h":"....",
                            "i":"..", "j":".---", "k":"-.-", "l":".-..",
                            "m":"--", "n":"-.", "o":"---", "p":".--.",
                            "q":"--.-", "r":".-.", "s":"...", "t":"-",
                            "u":"..-", "v":"...-", "w":".--", "x":"-..-", "y":"-.--", "z":"--..", " ":"/",
                            "1":".----", "2":"..---", "3":"...--", "4":"....-", "5":".....", "6":"-....", "7":"--...",
                            "8":"---..", "9":"----.", "0":"-----"}
        self.PIN = 18

    def encode(self,msg):
        msglist = list(msg)
        morselist = ""
        for letter in msglist:
            morselist += self.morse[letter]
            morselist += " "

        return morselist

    def decode(self,morse_msg):
        morse_msg = morse_msg.split()
        result = ""
        for element in morse_msg:
            if element == "/":
                result += " "
            else:
                result += list(self.morse.keys())[list(self.morse.values()).index(element)]
        return result

    def setupPi():
        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)
        GPIO.setup(self.PIN,GPIO.OUT)

    def transmit(self, morse_msg):
        setupPi()
        for letter in morse_msg:
            if letter == ".":
                GPIO.output(18,GPIO.HIGH)
                time.sleep(1)
                GPIO.output(18,GPIO.LOW)
            if letter == "-":
                GPIO.output(18,GPIO.HIGH)
                time.sleep(2)
                GPIO.output(18,GPIO.LOW)
            if letter == " ":
                GPIO.output(18,GPIO.HIGH)
                time.sleep(3)
                GPIO.output(18,GPIO.LOW)
            if letter == "/":
                GPIO.output(18,GPIO.HIGH)
                time.sleep(0.5)
                GPIO.output(18,GPIO.LOW)

if "__main__" == __name__:
    m = Morse_code()
    msg = input("Enter Your Message: ")
    result = m.encode(msg.lower())
    print(result)
    r2 = m.decode(result)
    print(r2)
