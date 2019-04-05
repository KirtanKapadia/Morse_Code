
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







if "__main__" == __name__:
    m = Morse_code()
    msg = input("Enter Your Message: ")
    result = m.encode(msg.lower())
    print(result)
    r2 = m.decode(result)
    print(r2)
